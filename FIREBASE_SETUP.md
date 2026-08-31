# Soulmate (Jodi) — Firebase Backend Setup Guide

This build is wired to Firebase project **soulmate-a511d** via the shipped
`app/google-services.json` (applicationId `com.soulmate.app`).

Follow these one-time console steps to bring the real backend fully online.

---

## 1. Authentication (Firebase Console → Authentication)

| Provider | Why | Action |
|----------|-----|--------|
| **Phone** | Login OTP + Deactivate/Delete OTP verification (SMS) | Enable "Phone". For local testing add `+91 98765 43210` as a test number with code `123456` |
| **Email Link (passwordless)** | "Email OTP" verification in Settings | Enable "Email link (passwordless, sign-in without password)" |
| **Google** | "Continue with Google" button on Login | Enable "Google". **Add your debug & release SHA-1 fingerprints** in Project Settings → Your Android app (`com.soulmate.app`) |

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

- `applicationId` → `com.soulmate.app` (matches the JSON client)
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
