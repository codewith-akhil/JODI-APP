# Soulmate (Jodi) Matrimony Android Application
### Complete System Architecture, Features & Screen Documentation

---

## 1. Executive Summary & Overview

**Soulmate (Jodi) Matrimony** is a next-generation Android matrimonial and relationship platform engineered with modern Android technologies (Kotlin, Jetpack Compose, Material 3, Room, Kotlin Coroutines & Flow, Retrofit). Designed for high trust, authenticity, and cultural resonance, the application addresses key pain points in traditional matchmaking platforms by integrating **AI-driven Biometric Face Liveness Verification**, **Government ID / DigiLocker Verification**, a **10-Porutham & 36-Guna Vedic Astrological Matchmaking Engine**, and **Instant Membership Upgrades via Razorpay Gateway**.

---

## 2. Technical Stack & Architecture

| Layer / Component | Technology / Library |
| :--- | :--- |
| **Language** | Kotlin 2.0+ (100% Kotlin Coroutines & StateFlow) |
| **UI Toolkit** | Jetpack Compose with Material 3 (M3) Design System |
| **Architecture** | MVVM (Model-View-ViewModel) + Clean Architecture Principles |
| **Local Database** | Room SQLite Persistence with type converters |
| **Image Loading** | Coil Compose with asynchronous memory & disk caching |
| **Payment Gateway** | Razorpay Android SDK & Verification Engine (UPI, Cards, NetBanking) |
| **Astro Algorithm** | 10-Poruthams & 36-Guna Ashtakoota Astro-Compatibility Engine |
| **Biometric & Verification** | AI Face Liveness Detection, DigiLocker Document Verification |
| **Navigation** | State-driven Type-safe Compose Screen Routing |

---

## 3. Complete Application Pages & Screen Directory

### 3.1 Splash & 3D Interactive Loading Screen (`SplashScreen`)
- **Visuals**: Fullscreen immersive 3D-perspective canvas featuring dynamic interlocking rotating wedding rings, glowing sacred geometry halos, and drifting 3D celestial star particles.
- **Actions**: One-tap entry to onboarding / language selection.
- **Design Tokens**: Deep Burgundy (`#5C061A`), Royal Purple (`#3B0A45`), Crimson Red (`#8B0000`), and Warm Gold accents.

### 3.2 Multilingual Selection Screen (`LanguageSelectionScreen`)
- **Supported Languages**: English, Malayalam (മലയാളം), Tamil (தமிழ்), Hindi (हिन्दी), Telugu (తెలుగు), Kannada (ಕನ್ನಡ), Marathi (मराठी), and Bengali (বাংলা).
- **Features**: Interactive cards displaying regional flags, native scripts, and real-time localized string adaptation.

### 3.3 Authentication & Mobile OTP Login (`LoginScreen` & `OtpVerificationScreen`)
- **Login Methods**: Mobile Number + instant 6-digit OTP verification, with Google One-Tap integration.
- **Resend & Security**: Countdown timer for OTP resend, auto-focus input cells, and auto-detect mechanism.

### 3.4 5-Step First-Time Profile Creation Wizard (`ProfileCreationScreen`)
1. **Step 1 - Basic & Lifestyle Details**: Full Name, Gender, Date of Birth, Height, Mother Tongue, Marital Status, Diet (Veg/Non-Veg), Habits.
2. **Step 2 - Horoscope & Vedic Astro Details**: Religion, Caste, Gothram, Star (Nakshatra), Rasi (Moon Sign), Dosham status (Chevvai/Manglik, Raghu/Kethu).
3. **Step 3 - Education & Career**: Highest Qualification, College/University, Profession, Employer Name, Annual Income Bracket, Work Location.
4. **Step 4 - Family & Roots**: Father's Occupation, Mother's Occupation, Siblings, Family Type (Nuclear/Joint), Family Values, Native Ancestral Location.
5. **Step 5 - Partner Preferences & Bio**: Expected Age Range, Height, Preferred Education, Preferred Caste/Religion, Location Preferences, and Personal About Me Bio.
- **Mandatory Photo Upload**: Multi-photo selector with preview tags.

### 3.5 Discovery Feed & Matches (`DiscoveryFeedView` in `HomeScreen.kt`)
- **Hero Carousel**: Verified Featured Profiles with high-resolution image galleries.
- **Astro Match Tags**: Real-time compatibility badge showing Porutham count (e.g. `9/10 Porutham • 94% Match`).
- **Interactive Match Cards**: Quick actions for Send Interest (💍), Shortlist (❤️), View Full Biodata, and Direct Chat.
- **Filter Sheet**: Advanced criteria filtering by Age, Religion, Caste, Star Nakshatra, Location, Education, and Income.

### 3.6 Profile Details Screen (`ProfileDetailScreen.kt`)
- **Photo Gallery Carousel**: Smooth touch carousel with page indicators and full-size view.
- **Trust Score Banner**: Verified badge indicator with real-time calculated Trust Score.
- **10-Porutham Astrological Breakdown**: Complete breakdown of Dina, Gana, Mahendra, Stree Deergha, Yoni, Rasi, Rasiyadhipathi, Vashya, Rajju, and Vedha with match indicators and explanations.
- **Biodata Sections**: Structured sections for Basic Details, Career & Education, Family Background, and Partner Expectations.
- **Quick Action Bar**: Floating bottom bar with Chat, Direct Family Phone Contact Unlock, and Send Interest.

### 3.7 Verification & Trust Center (`VerificationScreen`, `FaceVerificationScreen`, `GovtIdVerificationScreen`)
- **Trust Score Meter**: Visual circular score indicator from 0% to 100%.
- **Live AI Face Liveness**: 3-step interactive biometric detection:
  1. Center face within circular target
  2. Blink eyes to prove liveliness
  3. Smile naturally for biometric feature extraction (99.4% confidence score)
- **Government ID Verification**: Upload Aadhaar, Passport, Driving License, or Voter ID for DigiLocker validation and Verified Seal.

### 3.8 Real-Time Chat & Communications (`InboxView` & `ChatDetailScreen`)
- **Tabs**: Active Chats, Received Interests, Sent Interests, and Profile Visitors.
- **Chat Features**: Live typing indicators, message delivery status (✓✓), suggested prompt pills (e.g. "Can we match our horoscope charts?"), and voice/video calling entry points.
- **Safety Banner**: Built-in privacy and anti-fraud advisories.

### 3.9 Soulmate Premium Membership & Razorpay Checkout (`MembershipScreen.kt`)
- **Plans**:
  - **Silver (1 Month)**: ₹999 — 25 Verified Phone Numbers, Basic Matchmaking.
  - **Gold (3 Months - Most Popular)**: ₹1,999 — Unlimited Phone Numbers, 10-Porutham Reports, Priority Placement.
  - **Platinum (6 Months)**: ₹3,499 — Dedicated Relationship Manager, Instant WhatsApp Connect, Astrologer Consultation.
- **Razorpay Modal Checkout**: Supports Google Pay, PhonePe, Paytm UPI, Credit/Debit Cards, and NetBanking with secure order creation and signature verification.

### 3.10 Photo Management & Avatar Editor (`PhotoManagerScreen.kt` & `EditProfileScreen.kt`)
- Multi-photo upload manager with Primary Photo badge, privacy watermarking, and profile edit form for updating bio, career, and horoscope data.

---

## 4. Key Engine Implementations

### 4.1 Matchmaking & 10-Porutham Engine (`MatchmakingEngine.kt`)
Computes astrological harmony between two individuals using traditional South Indian and Vedic rules:
- **Dina Porutham**: Health and prosperity calculated from Nakshatra distance.
- **Gana Porutham**: Temperament harmony (Deva, Manushya, Rakshasa).
- **Mahendra Porutham**: Lineage and family well-being.
- **Stree Deergha**: Longevity and happiness.
- **Yoni Porutham**: Physical compatibility and intimacy.
- **Rasi Porutham**: Emotional bonding and lunar alignment.
- **Rasiyadhipathi Porutham**: Planetary friendship of ruling lords.
- **Vasya Porutham**: Mutual attraction and respect.
- **Rajju Porutham**: Marital longevity and mangalya strength.
- **Vedha Porutham**: Absence of astrological friction.

### 4.2 Razorpay Payment Gateway & Order Lifecycle (`RazorpayCheckoutManager.kt`)
1. User selects a subscription plan (Silver / Gold / Platinum).
2. App generates a secure Razorpay Order (`order_rzp_XXXXX`).
3. Razorpay payment sheet displays UPI apps, cards, and banking channels.
4. On payment completion, the cryptographic signature is verified.
5. Plan benefits are instantly unlocked and saved to local state/database.

---

## 5. Security & Privacy Safeguards
- **Screenshot & Privacy Protections**: Phone numbers and private assets are hidden until explicit consent or premium unlock.
- **End-to-End Chat Encryption**: Messages stored securely with local persistence.
- **Anti-Fraud Badges**: Unverified profiles receive prominent trust warning notices.

---
*(C) 2026 Soulmate Matrimony / Jodi App. Built with Google AI Studio & Jetpack Compose.*
