package com.example.data

import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.Language
import com.example.model.MembershipPlan
import com.example.model.Profile
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

    val membershipPlans = listOf(
        MembershipPlan(
            id = "plan_silver",
            title = "Silver Essential",
            duration = "3 Months",
            price = "₹ 99",
            originalPrice = "₹ 299",
            discountPercent = "67% OFF",
            isPopular = false,
            features = listOf(
                "View up to 25 Verified Contact Numbers",
                "Send Unlimited Connection Requests",
                "Direct Real-Time Chat with Matches",
                "Horoscope Porutham & Compatibility Score"
            ),
            bgGradientColor = 0xFF5C6BC0
        ),
        MembershipPlan(
            id = "plan_gold",
            title = "Gold Matchmaker",
            duration = "6 Months",
            price = "₹ 199",
            originalPrice = "₹ 599",
            discountPercent = "67% OFF",
            isPopular = true,
            features = listOf(
                "View up to 75 Verified Contact Numbers",
                "Direct WhatsApp Integration & Biodata PDF Download",
                "Complete 10-Point Jathakam & 36 Guna Report",
                "3x Higher Profile Visibility in Discovery",
                "Dedicated Relationship Manager Assistance"
            ),
            bgGradientColor = 0xFFD81B60
        ),
        MembershipPlan(
            id = "plan_platinum",
            title = "Platinum Royal Plan",
            duration = "12 Months / Till Marriage",
            price = "₹ 599",
            originalPrice = "₹ 1,999",
            discountPercent = "70% OFF",
            isPopular = false,
            features = listOf(
                "Unlimited Verified Contact Numbers & Biodatas",
                "Instant Match Alerts via WhatsApp & SMS",
                "Full Astro Consultation with Vedic Astrologer",
                "Top Ranked Featured Profile across all feeds",
                "Personalized Matchmaker Curated Profiles Weekly",
                "100% Privacy Control & Photo Watermarking"
            ),
            bgGradientColor = 0xFF8E24AA
        )
    )

    val userPhotos = listOf(
        UserPhoto("up_1", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop&q=80", true, "Approved"),
        UserPhoto("up_2", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600&auto=format&fit=crop&q=80", false, "Approved"),
        UserPhoto("up_3", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80", false, "In Review")
    )
}
