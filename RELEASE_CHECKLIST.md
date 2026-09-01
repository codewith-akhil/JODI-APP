# JODI Soulmate — Release Checklist (Play Store)

## 1. What changed in this build (v1.1)

- Real JODI SOULMATE logo everywhere in-app; brand emerald landing screen (no fake rings graphic)
- All mock/fake data removed: profiles, chats, notifications, transactions, stories,
  photos and "my profile" now load live from Firebase (RTDB + Cloud Functions)
- Demo OTP mode (123456) fully removed — real Firebase SMS only
- reCAPTCHA web redirect fixed: App Check now seeds a fixed debug token for debug
  builds; release builds use Play Integrity. Phone verification stays in-app.
- OTP screen: system keyboard opens automatically, SMS autofill works, demo card removed
- Back button pops the in-app navigation stack; double-back-to-exit on the home screen
- Direction-aware slide transitions between screens
- Cold-start splash: brand emerald window background with the real emblem
- FCM push notifications: channels, runtime permission (Android 13+), token sync,
  and server-side push on new message / match request / match accepted
- New server function `discoverProfiles` (verified-only discovery feed)
- Referral codes issued server-side (`SM-XXXXXX`) + `applyReferralCode` callable
- Release signing via `key.properties` + `soulmate-upload-key.jks`

## 2. ONE-TIME Firebase Console steps (required)

### a) Register the release signing key (REQUIRED for Play builds)
```
Keytool SHA-1:  13:C9:CA:51:D1:47:A9:C4:92:7F:F6:51:E6:DA:BD:5A:C9:5F:C7:C5
```
Firebase Console → Project Settings → Android app `com.soulmatematrimony.app`
→ Add fingerprint → paste the SHA-1 above (and keep the existing debug SHA-1).
Then download the updated `google-services.json` and replace `app/google-services.json`.

> If you use Play App Signing, ALSO add the SHA-1 that Play shows under
> "App signing key certificate" after your first AAB upload.

### b) Register the App Check debug token (for debug/testing builds)
```
Debug token: 75a538d1cc5b4a79a364ca9452201ee2e5339d14
```
Firebase Console → App Check → Apps → `com.soulmatematrimony.app`
→ Manage debug tokens → Add the token above.
This makes Phone Auth fully silent in debug builds — no browser captcha.

### c) Install Firebase CLI and deploy the backend (once)
```bash
npm install -g firebase-tools
firebase login
firebase deploy --only functions,database
```
The new `discoverProfiles` callable and the FCM push hooks live in `functions/index.js`.
The app shows an honest empty state until `discoverProfiles` is deployed.

## 3. Build & release

- Release keystore: `soulmate-upload-key.jks` (alias `upload`)
- Passwords are in `key.properties` (gitignored — keep both files safe!)
- **BACK UP the keystore + passwords now.** Losing them means you can never
  update the app on Play (unless you enrol in Play App Signing key reset).

```bash
./gradlew assembleRelease   # app-release.apk
./gradlew bundleRelease     # app-release.aab  ← upload this to Play Console
```

## 4. Play Store notes

- `versionCode` / `versionName`: bump in `app/build.gradle.kts` for every release
- Data safety form: app collects phone number, photos, gov-ID (verification), DOB
- Privacy policy + Terms screens are built into the app (Legal pages)
- FCM: notification permission is requested at first launch (Android 13+)
