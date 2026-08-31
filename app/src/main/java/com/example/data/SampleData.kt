package com.example.data

import com.example.model.AppNotification
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.FaqItem
import com.example.model.Language
import com.example.model.MembershipPlan
import com.example.model.Profile
import com.example.model.SuccessStory
import com.example.model.TransactionRecord
import com.example.model.UserPhoto

object SampleData {

    val languages = listOf(
        Language("ml", "Malayalam", "മലയാളം", "KL", "നിങ്ങൾക്ക് അനുയോജ്യമായ പങ്കാളിയെ കണ്ടെത്തൂ"),
        Language("ta", "Tamil", "தமிழ்", "TN", "உங்களுக்கான வாழ்க்கை துணையை கண்டறியுங்கள்"),
        Language("hi", "Hindi", "हिन्दी", "IN", "अपने लिए सही जीवनसाथी खोजें"),
        Language("kn", "Kannada", "ಕನ್ನಡ", "KA", "ನಿಮ್ಮ ಜೀವನ ಸಂಗಾತಿಯನ್ನು ಕಂಡುಕೊಳ್ಳಿ"),
        Language("te", "Telugu", "తెలుగు", "AP", "మీ సరైన జీవిత భాగస్వామిని కనుగొనండి"),
        Language("bn", "Bengali", "বাংলা", "WB", "আপনার উপযুক্ত জীবনসঙ্গী খুঁজুন"),
        Language("mr", "Marathi", "मराठी", "MH", "आपला योग्य जोडीदार शोधा"),
        Language("gj", "Gujarati", "ગુજરાતી", "GJ", "તમારો યોગ્ય જીવનસાથી શોધો"),
        Language("or", "Odia", "ଓଡ଼ିଆ", "OD", "ଆପଣଙ୍କ ଉପଯୁକ୍ତ ଜୀବନସାଥୀ ଖୋଜନ୍ତୁ"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ", "PB", "ਆਪਣਾ ਜੀਵਨ ਸਾਥੀ ਲੱਭੋ"),
        Language("en", "English", "English", "EN", "Find your perfect life partner")
    )

    val profiles = listOf(
        Profile(
            id = "SOULMATE_101",
            name = "Ananya Menon",
            age = 26,
            height = "5 ft 4 in (163 cm)",
            gender = "Female",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop&q=80"
            ),
            verified = true,
            trustScore = 98,
            education = "B.Tech in Computer Science",
            college = "College of Engineering, Trivandrum (CET)",
            profession = "Senior Software Engineer",
            company = "Infosys Limited",
            annualIncome = "₹ 14 - 18 Lakhs / Year",
            city = "Ernakulam",
            district = "Kochi",
            state = "Kerala",
            nativePlace = "Thrissur, Kerala",
            religion = "Hindu",
            caste = "Nair",
            gothram = "Vishwamitra",
            starNakshatra = "Rohini",
            rasi = "Rishabham (Taurus)",
            dosham = "No Dosham (Shudha Jathakam)",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam",
            diet = "Vegetarian / Eggetarian",
            drinking = "No",
            smoking = "No",
            bio = "Warm-hearted, ambitious, and deeply connected to traditional values while enjoying modern lifestyle. Love classical dance (Bharatanatyam), reading fiction, and weekend road trips.",
            familyFather = "Retired Bank Chief Manager (SBI)",
            familyMother = "High School Teacher (Govt)",
            familySiblings = "1 Younger Brother (Studying MBA)",
            familyType = "Nuclear, Upper Middle Class",
            partnerAgeRange = "27 - 31 Yrs",
            partnerHeightRange = "5 ft 8 in to 6 ft 2 in",
            partnerEducation = "B.Tech / MBA / MS / Medical",
            partnerLocation = "Kochi, Bangalore, Trivandrum, or Abroad",
            partnerCaste = "Hindu - Nair (Open to other Hindu sects)",
            joinedDaysAgo = 1
        ),
        Profile(
            id = "SOULMATE_102",
            name = "Dr. Rahul Varghese",
            age = 29,
            height = "5 ft 11 in (180 cm)",
            gender = "Male",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80"
            ),
            verified = true,
            trustScore = 96,
            education = "MBBS, MD (General Medicine)",
            college = "Government Medical College, Kozhikode",
            profession = "Consultant Physician",
            company = "Aster Medcity",
            annualIncome = "₹ 24 - 30 Lakhs / Year",
            city = "Kozhikode",
            district = "Kozhikode",
            state = "Kerala",
            nativePlace = "Kottayam, Kerala",
            religion = "Christian",
            caste = "Syrian Catholic (RC)",
            gothram = "N/A",
            starNakshatra = "N/A",
            rasi = "N/A",
            dosham = "None",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam",
            diet = "Non-Vegetarian",
            drinking = "Socially",
            smoking = "No",
            bio = "Doctor passionate about patient care and medical research. Enjoys playing football, acoustic guitar, and traveling to historical places across India.",
            familyFather = "Orthopedic Surgeon (Private Hospital)",
            familyMother = "Professor of Chemistry",
            familySiblings = "1 Elder Sister (Married, settled in UK)",
            familyType = "Joint, Affluent",
            partnerAgeRange = "24 - 28 Yrs",
            partnerHeightRange = "5 ft 2 in to 5 ft 8 in",
            partnerEducation = "MBBS, BDS, Masters, or Professional Degree",
            partnerLocation = "Kerala or Bangalore",
            partnerCaste = "Christian (RC / Jacobite / Marthomite)",
            joinedDaysAgo = 3
        ),
        Profile(
            id = "SOULMATE_103",
            name = "Meera Krishnan",
            age = 25,
            height = "5 ft 3 in (160 cm)",
            gender = "Female",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=600&auto=format&fit=crop&q=80"
            ),
            verified = true,
            trustScore = 95,
            education = "Chartered Accountant (CA)",
            college = "ICAI Chennai",
            profession = "Financial Analyst",
            company = "Deloitte India",
            annualIncome = "₹ 16 - 20 Lakhs / Year",
            city = "Trivandrum",
            district = "Thiruvananthapuram",
            state = "Kerala",
            nativePlace = "Palakkad, Kerala",
            religion = "Hindu",
            caste = "Brahmin (Iyer)",
            gothram = "Kashyapa",
            starNakshatra = "Ashwathi",
            rasi = "Mesham (Aries)",
            dosham = "No Dosham",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam / Tamil",
            diet = "Strict Vegetarian",
            drinking = "No",
            smoking = "No",
            bio = "Analytical yet creative CA with a deep appreciation for Carnatic music and yoga. Believes in mutual respect, laughter, and building a balanced, joyful home.",
            familyFather = "Senior Advocate, High Court",
            familyMother = "Homemaker",
            familySiblings = "None (Only Child)",
            familyType = "Nuclear, Traditional",
            partnerAgeRange = "26 - 30 Yrs",
            partnerHeightRange = "5 ft 7 in +",
            partnerEducation = "CA / MBA / B.Tech / Civil Services",
            partnerLocation = "Kerala, Chennai, Bangalore, or Mumbai",
            partnerCaste = "Hindu - Brahmin (Iyer / Iyengar / Namboothiri)",
            joinedDaysAgo = 4
        ),
        Profile(
            id = "SOULMATE_104",
            name = "Arjun Pillai",
            age = 28,
            height = "6 ft 0 in (183 cm)",
            gender = "Male",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600&auto=format&fit=crop&q=80"
            ),
            verified = true,
            trustScore = 99,
            education = "M.S. in Data Analytics",
            college = "National University of Singapore (NUS)",
            profession = "Lead Product Manager",
            company = "Swiggy / Tech Unicorn",
            annualIncome = "₹ 35 - 45 Lakhs / Year",
            city = "Kochi",
            district = "Ernakulam",
            state = "Kerala",
            nativePlace = "Alappuzha, Kerala",
            religion = "Hindu",
            caste = "Ezhava",
            gothram = "Shiva",
            starNakshatra = "Makayiram (Mrigashira)",
            rasi = "Midhunam (Gemini)",
            dosham = "No Dosham",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam",
            diet = "Non-Vegetarian",
            drinking = "No",
            smoking = "No",
            bio = "Product enthusiast, badminton player, and coffee aficionado. Looking for an energetic, kind-hearted partner who loves exploring new cuisines and cultural festivals.",
            familyFather = "Government Engineer (PWD - Retd)",
            familyMother = "Principal, Higher Secondary School",
            familySiblings = "1 Younger Sister (Architect)",
            familyType = "Nuclear, Progressive",
            partnerAgeRange = "24 - 28 Yrs",
            partnerHeightRange = "5 ft 3 in to 5 ft 9 in",
            partnerEducation = "Any Graduate / Post Graduate",
            partnerLocation = "Kerala, Bangalore, or Remote",
            partnerCaste = "Open to all Hindu communities",
            joinedDaysAgo = 1
        ),
        Profile(
            id = "SOULMATE_105",
            name = "Fathima Noor",
            age = 24,
            height = "5 ft 5 in (165 cm)",
            gender = "Female",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop&q=80"
            ),
            verified = true,
            trustScore = 94,
            education = "M.Arch (Urban Planning)",
            college = "NIT Calicut",
            profession = "Architectural Designer",
            company = "Studio Habitat Architecture",
            annualIncome = "₹ 10 - 14 Lakhs / Year",
            city = "Malappuram",
            district = "Malappuram",
            state = "Kerala",
            nativePlace = "Kozhikode, Kerala",
            religion = "Muslim",
            caste = "Sunni",
            gothram = "N/A",
            starNakshatra = "N/A",
            rasi = "N/A",
            dosham = "None",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam",
            diet = "Halal Non-Vegetarian",
            drinking = "No",
            smoking = "No",
            bio = "Dedicated architect designing sustainable homes. Practicing Islamic values with a contemporary mindset. Love gardening, sketching, and calligraphy.",
            familyFather = "Business Owner (Textile & Exports)",
            familyMother = "Homemaker",
            familySiblings = "2 Elder Brothers (Running Family Business)",
            familyType = "Joint Family",
            partnerAgeRange = "26 - 30 Yrs",
            partnerHeightRange = "5 ft 8 in +",
            partnerEducation = "B.Tech / MBA / Masters / Doctor",
            partnerLocation = "Kerala or UAE / GCC",
            partnerCaste = "Muslim (Sunni / Any)",
            joinedDaysAgo = 2
        )
    )

    val sampleChats = listOf(
        ChatMessage("msg_1", "SOULMATE_101", "Namaskaram! Thank you for connecting on Soulmate 😊", "10:30 AM", false),
        ChatMessage("msg_2", "SOULMATE_101", "Namaskaram Ananya! Very happy to connect with you.", "10:32 AM", true),
        ChatMessage("msg_3", "SOULMATE_101", "I noticed our horoscope nakshatras (Rohini & Makayiram) match very well (10/10 Porutham)!", "10:35 AM", false),
        ChatMessage("msg_4", "SOULMATE_101", "That is wonderful to know! My family also checked the Jathakam details.", "10:40 AM", true),
        ChatMessage("msg_5", "SOULMATE_101", "Would your family like to speak with my parents this coming Sunday?", "10:42 AM", false)
    )

    val sampleThreads = listOf(
        ChatThread(
            profile = profiles[0],
            lastMessage = "Would your family like to speak with my parents this Sunday?",
            lastMessageTime = "10:42 AM",
            unreadCount = 1,
            isOnline = true
        ),
        ChatThread(
            profile = profiles[1],
            lastMessage = "Dr. Rahul sent an interest in your profile.",
            lastMessageTime = "Yesterday",
            unreadCount = 0,
            isOnline = false
        ),
        ChatThread(
            profile = profiles[2],
            lastMessage = "Thanks for the message. Let me review your biodata.",
            lastMessageTime = "2 days ago",
            unreadCount = 0,
            isOnline = true
        )
    )

    // Exactly TWO membership plans (business rule #9):
    // FREE  -> ₹0 / month  (10 match requests/day, 1 message user/day)
    // PREMIUM -> ₹99 / month, valid 30 days (unlimited matching & messaging)
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
            bgGradientColor = 0xFF5C6BC0
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
            bgGradientColor = 0xFFD81B60
        )
    )

    val userPhotos = listOf(
        UserPhoto("up_1", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop&q=80", true, "Approved"),
        UserPhoto("up_2", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600&auto=format&fit=crop&q=80", false, "Approved"),
        UserPhoto("up_3", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80", false, "In Review")
    )

    // ---------- Seed data for the new feature pages ----------

    val sampleNotifications = listOf(
        AppNotification("ntf_1", "INTEREST", "New Interest Received", "Dr. Rahul Varghese (29, Kozhikode) sent you an interest. Review and respond now.", "5 min ago", profileId = "SOULMATE_102"),
        AppNotification("ntf_2", "MESSAGE", "New Message from Ananya", "\"Would your family like to speak with my parents this Sunday?\"", "22 min ago", profileId = "SOULMATE_101"),
        AppNotification("ntf_3", "VISITOR", "Your Profile Was Viewed", "Meera Krishnan viewed your full biodata today.", "1 hr ago", profileId = "SOULMATE_103"),
        AppNotification("ntf_4", "MATCH", "9/10 Porutham Match Found!", "Arjun Pillai is a high Vedic compatibility match (94%). Explore the biodata.", "3 hrs ago", profileId = "SOULMATE_104"),
        AppNotification("ntf_5", "SYSTEM", "Complete Your Verification", "Verify your Face & Govt ID to boost your Trust Score to 100% and get 3x more views.", "Yesterday"),
        AppNotification("ntf_6", "PAYMENT", "Membership Offer Unlocked", "Gold Matchmaker plan is 67% OFF for a limited period. Upgrade to unlock direct contacts.", "2 days ago")
    )

    val sampleTransactions = listOf(
        TransactionRecord("txn_1", "Gold Matchmaker", "6 Months", "₹ 199", "pay_rzp_88213", "order_rzp_44102", "15 Aug 2026, 10:24 AM", "SUCCESS"),
        TransactionRecord("txn_2", "Silver Essential", "3 Months", "₹ 99", "pay_rzp_77120", "order_rzp_33915", "02 Mar 2026, 06:12 PM", "SUCCESS"),
        TransactionRecord("txn_3", "Platinum Royal Plan", "12 Months", "₹ 599", "pay_rzp_66501", "order_rzp_22871", "14 Feb 2026, 09:45 AM", "FAILED")
    )

    val sampleSuccessStories = listOf(
        SuccessStory(
            "story_1", "Vishnu", "Parvathy", "Kochi, Kerala", "Married on 14 Jan 2026",
            "We matched with 10/10 Poruthams on Soulmate. Our families connected over a traditional puja and everything fell into place beautifully within four months. Forever grateful!",
            100,
            "https://images.unsplash.com/photo-1519741497674-611481863552?w=600&auto=format&fit=crop&q=80"
        ),
        SuccessStory(
            "story_2", "Karthik", "Anjali", "Trivandrum, Kerala", "Married on 22 Nov 2025",
            "The verified profiles made all the difference. Both families could trust the biodata completely. We spoke for the first time on Soulmate's secure chat and the rest is history!",
            96,
            "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=600&auto=format&fit=crop&q=80"
        ),
        SuccessStory(
            "story_3", "Nikhil", "Fathima", "Kozhikode, Kerala", "Married on 8 Feb 2026",
            "Despite different communities, the astrologer consultation feature helped our families understand compatibility beyond caste. A truly modern yet traditional platform.",
            91,
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?w=600&auto=format&fit=crop&q=80"
        )
    )

    val sampleFaqs = listOf(
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
