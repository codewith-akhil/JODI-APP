package com.example.data

import com.example.model.FaqItem
import com.example.model.Language
import com.example.model.MembershipPlan

/**
 * Static app configuration — the ONLY data allowed to ship inside the binary.
 * Everything user-specific (profiles, chats, notifications, transactions,
 * photos, stories) is loaded live from Firebase Realtime Database / Functions.
 */
object AppConfig {

    val languages = listOf(
        Language("ml", "Malayalam", "മലയാളം", "KL", "നിങ്ങൾക്ക് അനുയോജ്യമായ പങ്കാളിയെ കണ്ടെത്തൂ"),
        Language("en", "English", "English", "EN", "Find your perfect life partner"),
        Language("ta", "Tamil", "தமிழ்", "TN", "உங்களுக்கான வாழ்க்கை துணையை கண்டறியுங்கள்"),
        Language("hi", "Hindi", "हिन्दी", "IN", "अपने लिए सही जीवनसाथी खोजें"),
        Language("kn", "Kannada", "ಕನ್ನಡ", "KA", "ನಿಮ್ಮ ಜೀವನ ಸಂಗಾತಿಯನ್ನು ಕಂಡುಕೊಳ್ಳಿ"),
        Language("te", "Telugu", "తెలుగు", "AP", "మీ సరైన జీవిత భాగస్వామిని కనుగొనండి"),
        Language("bn", "Bengali", "বাংলা", "WB", "আপনার উপযুক্ত জীবনসঙ্গী খুঁজুন"),
        Language("mr", "Marathi", "मराठी", "MH", "आपला योग्य जोडीदार शोधा"),
        Language("gj", "Gujarati", "ગુજરાતી", "GJ", "તમારો યોગ્ય જીવનસાથી શોધો"),
        Language("or", "Odia", "ଓଡ଼ିଆ", "OD", "ଆପଣଙ୍କ ଉପಯುಕ್ತ ଜୀବନସାଥୀ ଖୋଜନ୍ତୁ"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ", "PB", "ਆਪਣਾ ਜੀਵਨ ਸਾਥੀ ਲੱਭੋ")
    )

    // Exactly TWO membership plans (business rule #9):
    // FREE    -> ₹0          (10 match requests/day, 1 message user/day)
    // PREMIUM -> ₹99 / month (valid 30 days, unlimited matching & messaging)
    val membershipPlans = listOf(
        MembershipPlan(
            id = "plan_free",
            title = "Free",
            duration = "1 Month",
            price = "₹ 0",
            originalPrice = "₹ 0",
            discountPercent = "",
            isPopular = false,
            features = listOf(
                "Create & complete your profile",
                "Upload up to 6 profile photos",
                "Browse and search verified profiles",
                "Apply all available search filters",
                "Receive match requests & messages",
                "Send up to 10 match requests per day",
                "Message up to 1 new user per day"
            ),
            bgGradientColor = 0xFF059669
        ),
        MembershipPlan(
            id = "plan_premium_99",
            title = "Premium",
            duration = "1 Month (30 days)",
            price = "₹ 99",
            originalPrice = "₹ 299",
            discountPercent = "67% OFF",
            isPopular = true,
            features = listOf(
                "Unlimited match requests",
                "Unlimited messaging & chat",
                "No daily limits at all",
                "Verified-user matching and chat",
                "Full search & filter functionality",
                "Priority support & profile visibility"
            ),
            bgGradientColor = 0xFF0D9488
        )
    )

    val faqs = listOf(
        FaqItem(
            "How does the Trust Score work?",
            "Your Trust Score (0-100%) is computed from verifications: mobile (+20), Face liveness biometrics (+35), Government ID via DigiLocker (+35), horoscope details (+10). Higher scores appear earlier in discovery feeds and receive 3x more interests."
        ),
        FaqItem(
            "Is my phone number visible to other members?",
            "Never. Your number stays confidential until you explicitly unlock & share contact details, or accept a connection. All calls initiated through the app keep both numbers private."
        ),
        FaqItem(
            "How are profiles verified?",
            "Every profile undergoes AI Face Liveness Detection (blink & smile challenges) and Government ID validation (Aadhaar / Passport / DL / Voter ID) via DigiLocker. Look for the blue verified badge before engaging."
        ),
        FaqItem(
            "What is the refund policy for memberships?",
            "If you have not unlocked any contacts, we offer a 7-day no-questions-asked full refund. Raise a ticket via Help & Support and our team responds within 24 hours."
        ),
        FaqItem(
            "How do I delete or hide my profile?",
            "Go to Settings > Account. You can temporarily deactivate (hides you from all feeds) or permanently delete your profile with data erasure. Both actions require OTP verification for your safety."
        ),
        FaqItem(
            "How accurate is the 10-Porutham matching?",
            "Our engine follows traditional South Indian Vedic rules (Dina, Gana, Mahendra, Stree Deergha, Yoni, Rasi, Rasiyadhipathi, Vasya, Rajju, Vedha) and computes 36 Guna points, reviewed by certified astrologers for premium members."
        )
    )
}
