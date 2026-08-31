/**
 * Soulmate Matrimony — server-authoritative backend (Cloud Functions for Firebase).
 *
 * THE BACKEND IS THE FINAL AUTHORITY for (business rule #22):
 *   - verification status            - membership status & subscription expiry
 *   - daily match-request count      - daily messaging-user count
 *   - blocking / reporting           - profile & matching eligibility
 *   - account status                 - all permission checks
 *
 * Clients NEVER write restricted data directly: RTDB rules deny it
 * (see database.rules.json) and every mutation flows through these callables.
 *
 * Deploy:  firebase deploy --only functions,database
 * Config:  firebase functions:secrets:set RAZORPAY_KEY_SECRET
 *          firebase functions:secrets:set RAZORPAY_KEY_ID
 */

const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");
const crypto = require("crypto");

admin.initializeApp();
const db = admin.database();

// ---------------------------------------------------------------------------
// Configurable limits (admin can change via app_config without app release)
// ---------------------------------------------------------------------------
const DEFAULTS = {
  MAX_MATCH_REQUESTS_PER_DAY: 10,      // Free tier
  MAX_MESSAGE_USERS_PER_DAY: 1,        // Free tier (unique users)
  MAX_CHAT_MESSAGES_PER_MINUTE: 10,    // anti-spam
  MIN_REGISTRATION_AGE_YEARS: 18,
  PREMIUM_PRICE_PAISE: 9900,           // ₹99
  PREMIUM_VALIDITY_DAYS: 30,
};

async function config() {
  const snap = await db.ref("app_config/limits").get();
  return snap.exists() ? { ...DEFAULTS, ...snap.val() } : DEFAULTS;
}

const todayKey = () =>
  new Intl.DateTimeFormat("en-CA", { timeZone: "Asia/Kolkata" })
    .format(new Date()); // YYYY-MM-DD in the server timezone

const isPremium = (user) => {
  const m = user.membership || {};
  return (
    m.tier === "PREMIUM" &&
    typeof m.expiryDate === "number" &&
    m.expiryDate > Date.now()
  );
};

/** The 9-point pre-action check (business rule #17). */
async function preflight(uid, targetUid, { requireTargetVerified = true } = {}) {
  const [meSnap, targetSnap] = await Promise.all([
    db.ref(`users/${uid}`).get(),
    targetUid ? db.ref(`users/${targetUid}`).get() : Promise.resolve(null),
  ]);

  if (!meSnap.exists()) throw new HttpsError("failed-precondition", "Account not initialised.");
  const me = meSnap.val();

  // 1-3: caller account state
  const status = me.status || "INCOMPLETE";
  if (status === "BANNED") throw new HttpsError("permission-denied", "Your account has been banned.");
  if (status === "SUSPENDED") throw new HttpsError("permission-denied", "Your account is suspended.");
  if (status === "DEACTIVATED") throw new HttpsError("permission-denied", "Your account is deactivated.");
  if (status === "REJECTED") throw new HttpsError("permission-denied", "Your profile was rejected. Contact support.");

  // Verification gate (rule #3)
  const verStatus = (me.verification && me.verification.status) || "UNVERIFIED";
  if (verStatus !== "VERIFIED") {
    throw new HttpsError("permission-denied", "VERIFICATION_REQUIRED");
  }

  // 7-8: membership & daily limits resolved by each action below
  return { me, premium: isPremium(me), target: targetSnap ? targetSnap.val() : null };
}

async function assertTargetEligible(target, targetUid, { blockCheckUid } = {}) {
  if (!target || !targetUid) throw new HttpsError("invalid-argument", "Target user not found.");
  const tStatus = target.status || "INCOMPLETE";
  if (!["ACTIVE"].includes(tStatus)) {
    throw new HttpsError("failed-precondition", "This profile is not available for interaction.");
  }
  const tVer = (target.verification && target.verification.status) || "UNVERIFIED";
  if (tVer !== "VERIFIED") {
    throw new HttpsError("failed-precondition", "You can only interact with verified profiles.");
  }
  // 6: blocked in either direction
  if (blockCheckUid) {
    const a = await db.ref(`blocks/${blockCheckUid}_${targetUid}`).get();
    const b = await db.ref(`blocks/${targetUid}_${blockCheckUid}`).get();
    if (a.exists() || b.exists()) {
      throw new HttpsError("permission-denied", "You cannot interact with this member.");
    }
  }
}

// ---------------------------------------------------------------------------
// onUserCreated — initialise a new account (rule #1: one account per phone is
// additionally enforced by database.rules.json phone_index).
// ---------------------------------------------------------------------------
exports.onUserCreated = onDocumentCreated("users_rt/{uid}", async (event) => {
  // placeholder for Firestore-mirrored users; RTDB bootstrap happens client-side
  return null;
});

// ---------------------------------------------------------------------------
// sendMatchRequest (rule #10) — verified-only, duplicate-safe, daily-capped
// ---------------------------------------------------------------------------
exports.sendMatchRequest = onCall({ region: "asia-south1" }, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");
  const targetUid = String(request.data?.targetUid || "");
  const note = String(request.data?.note || "").slice(0, 300);
  if (!targetUid) throw new HttpsError("invalid-argument", "targetUid required.");
  if (targetUid === uid) throw new HttpsError("invalid-argument", "You cannot send a request to yourself.");

  const cfg = await config();
  const { me, premium } = await preflight(uid, targetUid);

  // Gender/preference compatibility guard (rule #10)
  const myGender = (me.gender || "").toUpperCase();
  const targetGender = ((await db.ref(`users/${targetUid}/gender`).get()).val() || "").toUpperCase();
  if (myGender && targetGender && myGender === targetGender) {
    throw new HttpsError("failed-precondition", "This profile does not match your partner preference.");
  }

  await assertTargetEligible((await db.ref(`users/${targetUid}`).get()).val(), targetUid, { blockCheckUid: uid });

  // Duplicate-pending / already-decided guard
  const dup = await db
    .ref("match_requests")
    .orderByChild("pairKey")
    .equalTo(`${uid}_${targetUid}`)
    .get();
  if (dup.exists()) {
    for (const [, v] of Object.entries(dup.val())) {
      if (v.status === "PENDING") throw new HttpsError("already-exists", "You already have a pending request with this member.");
      if (v.status === "ACCEPTED") throw new HttpsError("already-exists", "You are already connected with this member.");
    }
  }

  // 8: daily limit (FREE only) — server-side counter
  const day = todayKey();
  const countRef = db.ref(`users/${uid}/counters/${day}/matchRequestsSent`);
  if (!premium) {
    const sent = (await countRef.get()).val() || 0;
    if (sent >= cfg.MAX_MATCH_REQUESTS_PER_DAY) {
      throw new HttpsError(
        "resource-exhausted",
        `MATCH_LIMIT_REACHED:${cfg.MAX_MATCH_REQUESTS_PER_DAY}`
      );
    }
    await countRef.set(sent + 1);
  }

  // Persist request with full state machine
  const ref = db.ref("match_requests").push();
  await ref.set({
    id: ref.key,
    fromUid: uid,
    toUid: targetUid,
    pairKey: `${uid}_${targetUid}`,
    note,
    status: "PENDING",
    createdAt: admin.database.ServerValue.TIMESTAMP,
  });

  await db.ref(`notifications/${targetUid}`).push({
    type: "INTEREST",
    title: "New Match Request",
    body: "Someone verified sent you a match request. Review it now!",
    timeAgo: "Just now",
    isRead: false,
    profileId: uid,
    createdAt: admin.database.ServerValue.TIMESTAMP,
  });

  const remaining = premium
    ? -1
    : Math.max(0, cfg.MAX_MATCH_REQUESTS_PER_DAY - ((await countRef.get()).val() || 0));
  return { ok: true, requestId: ref.key, remainingToday: remaining };
});

// ---------------------------------------------------------------------------
// respondToMatchRequest — accept / reject / cancel (rule #10 states)
// ---------------------------------------------------------------------------
exports.respondToMatchRequest = onCall({ region: "asia-south1" }, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");
  const { requestId, action } = request.data || {};
  const allowed = ["ACCEPTED", "REJECTED", "CANCELLED"];
  if (!requestId || !allowed.includes(action)) {
    throw new HttpsError("invalid-argument", "requestId and a valid action are required.");
  }

  const ref = db.ref(`match_requests/${requestId}`);
  const snap = await ref.get();
  if (!snap.exists()) throw new HttpsError("not-found", "Request not found.");
  const req = snap.val();

  const isReceiver = req.toUid === uid;
  const isSender = req.fromUid === uid;
  if (action === "CANCELLED" && !isSender) throw new HttpsError("permission-denied", "Only the sender can cancel.");
  if (action !== "CANCELLED" && !isReceiver) throw new HttpsError("permission-denied", "Only the receiver can respond.");
  if (req.status !== "PENDING") throw new HttpsError("failed-precondition", "Request already resolved.");

  await ref.update({ status: action, respondedAt: admin.database.ServerValue.TIMESTAMP });

  if (action === "ACCEPTED") {
    // A chat thread becomes available to both verified participants
    const threadId = [req.fromUid, req.toUid].sort().join("_");
    await db.ref(`chats/${threadId}/members`).update({ [req.fromUid]: true, [req.toUid]: true });
    await db.ref(`notifications/${req.fromUid}`).push({
      type: "MATCH",
      title: "Match Request Accepted!",
      body: "Your match request was accepted. Start the conversation!",
      timeAgo: "Just now",
      isRead: false,
      profileId: req.toUid,
      createdAt: admin.database.ServerValue.TIMESTAMP,
    });
  }
  return { ok: true, status: action };
});

// ---------------------------------------------------------------------------
// sendMessage (rule #11) — verified-only, eligibility, FREE 1-user/day, spam caps
// ---------------------------------------------------------------------------
exports.sendMessage = onCall({ region: "asia-south1" }, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");
  const { targetUid, text, type = "TEXT", mediaUrl = "" } = request.data || {};
  const body = String(text || "").slice(0, 2000);
  if (!targetUid || (!body && !mediaUrl)) {
    throw new HttpsError("invalid-argument", "targetUid and text (or media) are required.");
  }

  const cfg = await config();
  const { premium } = await preflight(uid, targetUid);
  await assertTargetEligible((await db.ref(`users/${targetUid}`).get()).val(), targetUid, { blockCheckUid: uid });

  const day = todayKey();
  const dayRef = db.ref(`users/${uid}/counters/${day}`);

  // FREE: max 1 unique user per day (rule #11)
  if (!premium) {
    const usersStarted = (await dayRef.child("messageUsersStarted").get()).val() || {};
    const startedList = Object.keys(usersStarted || {});
    if (!usersStarted[targetUid] && startedList.length >= cfg.MAX_MESSAGE_USERS_PER_DAY) {
      throw new HttpsError("resource-exhausted", "MESSAGE_LIMIT_REACHED:1");
    }
  }

  const threadId = [uid, targetUid].sort().join("_");

  // Anti-spam rate limit (rule #15) — max N messages / minute / thread
  const minuteAgo = Date.now() - 60_000;
  const recent = await db
    .ref(`chats/${threadId}/messages`)
    .orderByChild("timestamp")
    .startAt(minuteAgo)
    .get();
  if (recent.numChildren() >= cfg.MAX_CHAT_MESSAGES_PER_MINUTE) {
    throw new HttpsError("resource-exhausted", "Sending too fast. Please slow down.");
  }

  // Mark the unique-user counter (FREE only)
  if (!premium) {
    await dayRef.child(`messageUsersStarted/${targetUid}`).set(true);
  }

  const msgRef = db.ref(`chats/${threadId}/messages`).push();
  await msgRef.set({
    id: msgRef.key,
    senderId: uid,
    text: body || "📷 Photo",
    type,
    mediaUrl,
    timestamp: admin.database.ServerValue.TIMESTAMP,
    isRead: false,
  });
  await db.ref(`chats/${threadId}/meta`).update({
    lastMessage: type === "TEXT" ? body : "📎 Attachment",
    lastTimestamp: admin.database.ServerValue.TIMESTAMP,
    lastSenderId: uid,
  });
  await db.ref(`chats/${threadId}/members`).update({ [uid]: true, [targetUid]: true });

  return { ok: true, messageId: msgRef.key, threadId };
});

// ---------------------------------------------------------------------------
// activatePremium (rules #19/#22) — server-side payment verification.
// Premium is NEVER activated because the client says so; the Razorpay
// signature is verified here with the secret key.
// ---------------------------------------------------------------------------
exports.activatePremium = onCall(
  { region: "asia-south1", secrets: ["RAZORPAY_KEY_ID", "RAZORPAY_KEY_SECRET"] },
  async (request) => {
    const uid = request.auth?.uid;
    if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");
    const { razorpayOrderId, razorpayPaymentId, razorpaySignature, planId } = request.data || {};
    if (!razorpayOrderId || !razorpayPaymentId || !razorpaySignature) {
      throw new HttpsError("invalid-argument", "Missing payment confirmation fields.");
    }
    if (planId && planId !== "plan_premium_99") {
      throw new HttpsError("invalid-argument", "Unknown plan.");
    }

    const cfg = await config();
    const keySecret = process.env.RAZORPAY_KEY_SECRET;
    const expected = crypto
      .createHmac("sha256", keySecret)
      .update(`${razorpayOrderId}|${razorpayPaymentId}`)
      .digest("hex");
    if (expected !== razorpaySignature) {
      await db.ref(`transactions/${uid}`).push({
        planTitle: "Premium", planDuration: "1 Month (30 days)",
        amount: "₹ 99", orderId: razorpayOrderId, paymentId: razorpayPaymentId,
        status: "FAILED", reason: "signature_mismatch",
        createdAt: admin.database.ServerValue.TIMESTAMP,
      });
      throw new HttpsError("permission-denied", "Payment verification failed.");
    }

    // Idempotency: never double-extend for the same paymentId
    const paidSnap = await db.ref(`payments/${razorpayPaymentId}`).get();
    if (paidSnap.exists()) return { ok: true, alreadyProcessed: true };

    const current = (await db.ref(`users/${uid}/membership`).get()).val() || {};
    const now = Date.now();
    const base = current.tier === "PREMIUM" && (current.expiryDate || 0) > now
      ? current.expiryDate : now;
    const expiry = base + cfg.PREMIUM_VALIDITY_DAYS * 24 * 60 * 60 * 1000;

    await db.ref(`users/${uid}/membership`).update({
      tier: "PREMIUM",
      startDate: base,
      expiryDate: expiry,
      autoRenew: true,
    });
    await db.ref(`payments/${razorpayPaymentId}`).set({
      uid, orderId: razorpayOrderId, amountPaise: cfg.PREMIUM_PRICE_PAISE,
      verifiedAt: now, plan: "PREMIUM_99",
    });
    await db.ref(`subscriptions/${uid}`).push({
      planId: "plan_premium_99", planTitle: "Premium", duration: "1 Month (30 days)",
      amount: "₹ 99", orderId: razorpayOrderId, paymentId: razorpayPaymentId,
      status: "ACTIVE", startDate: base, expiryDate: expiry,
      id: null, createdAt: now,
    });
    await db.ref(`transactions/${uid}`).push({
      planTitle: "Premium", planDuration: "1 Month (30 days)", amount: "₹ 99",
      orderId: razorpayOrderId, paymentId: razorpayPaymentId, status: "SUCCESS",
      createdAt: now,
    });
    await db.ref(`notifications/${uid}`).push({
      type: "PAYMENT", title: "Membership Activated",
      body: "Your Premium plan is now active. Unlimited matching & messaging!",
      timeAgo: "Just now", isRead: false, createdAt: now,
    });

    return { ok: true, expiryDate: expiry };
  }
);

// ---------------------------------------------------------------------------
// cancelAutoRenewal — benefits stay until expiry (rule #9 downgrades later)
// ---------------------------------------------------------------------------
exports.cancelAutoRenewal = onCall({ region: "asia-south1" }, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");
  await db.ref(`users/${uid}/membership/autoRenew`).set(false);
  return { ok: true };
});

// ---------------------------------------------------------------------------
// submitForVerification (rules #3/#4/#8) — completeness gate, then PENDING
// ---------------------------------------------------------------------------
exports.submitForVerification = onCall({ region: "asia-south1" }, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "Sign in required.");

  const [profileSnap, photosSnap] = await Promise.all([
    db.ref(`profiles/${uid}`).get(),
    db.ref(`users/${uid}/photoCount`).get(),
  ]);
  const p = profileSnap.val() || {};
  const mandatory = ["name", "dob", "gender", "profileFor", "maritalStatus", "religion", "motherTongue", "city"];
  const missing = mandatory.filter((f) => !p[f] || String(p[f]).trim() === "");
  if (missing.length) {
    throw new HttpsError("failed-precondition", `INCOMPLETE_PROFILE:${missing.join(",")}`);
  }
  const photoCount = photosSnap.val() || 0;
  if (photoCount < 1) throw new HttpsError("failed-precondition", "MIN_ONE_PHOTO_REQUIRED");

  await db.ref(`users/${uid}`).update({
    status: "PENDING_VERIFICATION",
    "verification/status": "PENDING",
  });
  return { ok: true, status: "PENDING_VERIFICATION" };
});

// ---------------------------------------------------------------------------
// ADMIN CONTROL (rule #20) — guarded by the `admin` custom claim.
// Grant via: admin.auth().setCustomUserClaims(uid, { admin: true })
// ---------------------------------------------------------------------------
function requireAdmin(request) {
  if (request.auth?.token?.admin !== true) {
    throw new HttpsError("permission-denied", "Admin access required.");
  }
  return request.auth.uid;
}

const adminSetStatus = (status) =>
  onCall({ region: "asia-south1" }, async (request) => {
    requireAdmin(request);
    const { targetUid, reason } = request.data || {};
    if (!targetUid) throw new HttpsError("invalid-argument", "targetUid required.");
    await db.ref(`users/${targetUid}/status`).set(status);
    if (reason) await db.ref(`users/${targetUid}/statusReason`).set(reason);
    return { ok: true };
  });

exports.adminVerifyProfile = onCall({ region: "asia-south1" }, async (request) => {
  requireAdmin(request);
  const { targetUid, approve } = request.data || {};
  if (!targetUid || typeof approve !== "boolean") {
    throw new HttpsError("invalid-argument", "targetUid and approve are required.");
  }
  const status = approve ? "ACTIVE" : "REJECTED";
  await db.ref(`users/${targetUid}`).update({
    status,
    "verification/status": approve ? "VERIFIED" : "REJECTED",
  });
  await db.ref(`notifications/${targetUid}`).push({
    type: "SYSTEM",
    title: approve ? "Profile Verified ✓" : "Verification Rejected",
    body: approve
      ? "Your profile is verified. You can now match and chat!"
      : "Your verification was rejected. Please re-submit valid details.",
    timeAgo: "Just now", isRead: false,
    createdAt: admin.database.ServerValue.TIMESTAMP,
  });
  return { ok: true };
});

exports.adminSuspendUser = adminSetStatus("SUSPENDED");
exports.adminBanUser = adminSetStatus("BANNED");
exports.adminReactivateUser = adminSetStatus("ACTIVE");
exports.adminDeactivateUser = adminSetStatus("DEACTIVATED");
