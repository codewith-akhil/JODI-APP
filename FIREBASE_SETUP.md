# Soulmate (Jodi) — Firebase Backend Setup Guide

This build is wired to Firebase project **soulmate-a511d** via the shipped
`app/google-services.json` (applicationId `com.soulmatematrimony.app`).

Follow these one-time console steps to bring the real backend fully online.

---

## 1. Authentication (Firebase Console → Authentication)

| Provider | Why | Action |
|----------|-----|--------|
| **Phone** | Login OTP + Deactivate/Delete OTP verification (SMS) | Enable "Phone". For local testing add `+91 98765 43210` as a test number with code `123456` |
| **Email Link (passwordless)** | "Email OTP" verification in Settings | Enable "Email link (passwordless, sign-in without password)" |
| **Google** | "Continue with Google" button on Login | Enable "Google". **Add your debug & release SHA-1 fingerprints** in Project Settings → Your Android app (`com.soulmatematrimony.app`) |

> Google Sign-In uses the OAuth Web Client (client_type 3) already present in
> the shipped google-services.json, resolved at runtime as
> `default_web_client_id`.

### Phone Auth test mode
On the emulator without Play Services, or before billing/quota is configured,
Firebase may throw on `verifyPhoneNumber`. The app **auto-falls back to demo
mode** (accepts `123456`) and shows a toast — the full flow remains testable.

---

## 2. Realtime Database (Firebase Console → Realtime Database)

URL: `https://soulmate-a511d-default-rtdb.asia-southeast1.firebasedatabase.app`

Create the database (asia-southeast1 region is pre-configured) and start with
these rules (development-friendly, owner-scoped):

```json
{
  "rules": {
    "users":             { "$uid": { ".read": "$uid === auth.uid", ".write": "$uid === auth.uid" } },
    "profiles":          { "$uid": { ".read": "auth != null",  ".write": "$uid === auth.uid" } },
    "privacy_settings":  { "$uid": { ".read": "$uid === auth.uid", ".write": "$uid === auth.uid" } },
    "notifications":     { "$uid": { ".read": "$uid === auth.uid", ".write": "auth != null" } },
    "verifications":     { "$uid": { ".read": "$uid === auth.uid", ".write": "$uid === auth.uid" } },
    "subscriptions":     { "$uid": { ".read": "$uid === auth.uid", ".write": "$uid === auth.uid" } },
    "transactions":      { "$uid": { ".read": "$uid === auth.uid", ".write": "$uid === auth.uid" } },
    "interests":         { ".read": "auth != null", ".write": "auth != null" },
    "chats":             { "$thread": { ".read": "auth != null", ".write": "auth != null" } },
    "reports":           { ".read": false, ".write": "auth != null" },
    "blocks":            { ".read": "auth != null", ".write": "auth != null" },
    "support_tickets":   { ".read": false, ".write": "auth != null" },
    "success_stories":   { ".read": true, ".write": false }
  }
}
```

### Data map
| Node | Content |
|------|---------|
| `users/{uid}` | phone, e-mail, status (ACTIVE / DEACTIVATED), last login |
| `profiles/{uid}` | full biodata (name, horoscope, career, family, partner prefs) |
| `privacy_settings/{uid}` | visibility, photo privacy, incognito, toggles |
| `notifications/{uid}/{pushId}` | interest / message / match / payment alerts |
| `interests/{pushId}` | sent interests with status |
| `chats/{threadId}/messages/{pushId}` | real-time messages (TEXT / IMAGE) |
| `subscriptions/{uid}/{pushId}` | active plan records from Razorpay flow |
| `transactions/{uid}/{pushId}` | payment receipts (SUCCESS / FAILED) |
| `verifications/{uid}` | face liveness + govt ID results & trust score |
| `reports`, `blocks`, `support_tickets`, `success_stories` | safety & support |

## 3. Storage (Firebase Console → Storage)

Bucket: `soulmate-a511d.firebasestorage.app`. Create it, then rules:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /profile_photos/{uid}/{file} {
      allow read: if auth != null;
      allow write: if request.auth.uid == uid && request.resource.size < 8 * 1024 * 1024;
    }
    match /govt_id_proofs/{uid}/{file} {
      allow read, write: if request.auth.uid == uid;   // never public
    }
    match /chat_media/{thread}/{file} {
      allow read, write: if auth != null && request.resource.size < 8 * 1024 * 1024;
    }
  }
}
```

## 4. App-side changes already applied

- `applicationId` → `com.soulmatematrimony.app` (matches the JSON client; the old `com.soulmate.app` name is reserved on Google Play)
- Dependencies: `firebase-auth`, `firebase-database`, `firebase-storage`,
  `kotlinx-coroutines-play-services`, Credential Manager (`credentials`,
  `credentials-play-services-auth`, `googleid`)
- `INTERNET` + `ACCESS_NETWORK_STATE` permissions added to the manifest
- Backend module: `network/FirebaseManager.kt` (auth, RTDB, storage)
- Google Sign-In: `network/GoogleSignInHelper.kt`
- Connectivity: `network/NetworkMonitor.kt`

## 5. Behaviour notes

- **Demo fallback everywhere**: every Firebase call is wrapped so the app
  continues locally (sample data) if the network/backend is unavailable.
- **Deactivate** → sets `users/{uid}/status = DEACTIVATED` and signs out.
- **Delete** → erases profile, notifications, privacy, subscriptions,
  transactions, verifications, then deletes the Firebase Auth account.
- **Email OTP** uses Firebase's native email-link verification
  (`verifyBeforeUpdateEmail` / `sendEmailVerification`).

## 6. App Check + Play Integrity (silent Phone Auth, no reCAPTCHA)

`SoulmateApp.kt` (registered in the Manifest as the Application class)
initializes Firebase and installs App Check **before** any Firebase service:

- **Release builds** → `PlayIntegrityAppCheckProviderFactory` — Phone Auth
  verifies silently in-app; the reCAPTCHA browser redirect never appears on
  devices with Google Play services.
- **Debug builds** → `DebugAppCheckProviderFactory` — emulators keep working.
  Copy the token logged in Logcat (filter `AppCheck`) into
  **Firebase Console → App Check → Apps → Manage debug tokens**.

### One-time console registration
1. **Firebase Console → App Check → Apps → Register** the Android app
   (`com.soulmatematrimony.app`) with the **Play Integrity** provider
   (the link also enables the Play Integrity API in Google Cloud).
2. Start in **Monitoring** mode; flip **Enforce** on for Realtime Database and
   Storage after a day or two of clean metrics.
3. The old Firebase app entry `com.soulmate.app` can be deleted — it can never
   be used on Play (package name permanently reserved by another developer).

## 7. Business Rules & Server-Side Enforcement (authoritative)

The backend is the **final authority** for verification, membership, daily
limits, blocking and eligibility (RTDB rules deny direct user writes; every
restricted mutation flows through Cloud Functions callables).

### Deploy the enforcement layer
```bash
cd JODI-APP
firebase deploy --only database        # database.rules.json
firebase deploy --only functions       # functions/ (Node 20)
firebase functions:secrets:set RAZORPAY_KEY_ID
firebase functions:secrets:set RAZORPAY_KEY_SECRET
```

### What is enforced where
| Rule | Client (UX) | Server (authority) |
|---|---|---|
| 18+ age, DOB-derived age | `completeProfileCreation` gate | `users.dobMillis` validate + functions |
| 6-digit OTP, 5-min validity, 60s resend, 5 attempts | OTP screens + VM counters | Firebase Phone Auth |
| One account per phone | `registerPhoneIndex` | `phone_index` rules (write-once) |
| Verified-only matching/chat | VM gates + verification prompt | `preflight()` in callables |
| Free 10 match requests/day | local counter mirror | `sendMatchRequest` + counters (server-written only) |
| Free 1 message user/day | local counter mirror | `sendMessage` + counters |
| Premium ₹99 / 30 days | MembershipScreen | `activatePremium` (Razorpay HMAC verified server-side) |
| Auto-downgrade after expiry | `isPremium` expiry check | tier/expiry written only by functions |
| Two plans only | SampleData (FREE ₹0 / PREMIUM ₹99) | `app_config/limits` (admin-changeable) |
| Block / report unlimited | SafetyCenter | rules (append-only) + callable states |
| Profile states | verification center | `adminVerifyProfile`, `adminSuspendUser`, `adminBanUser` … |

### Admin access
Grant an admin once: `admin.auth().setCustomUserClaims(uid, { admin: true })`
— then the `admin*` callables become usable for verification approval,
suspension, banning and moderation.
