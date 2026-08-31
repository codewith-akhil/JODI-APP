package com.example.model

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val welcomeText: String
)

data class Profile(
    val id: String,
    val name: String,
    val age: Int,
    val height: String,
    val gender: String,
    val photoUrls: List<String>,
    val verified: Boolean,
    val trustScore: Int,
    val education: String,
    val college: String,
    val profession: String,
    val company: String,
    val annualIncome: String,
    val city: String,
    val district: String,
    val state: String,
    val nativePlace: String,
    val religion: String,
    val caste: String,
    val gothram: String,
    val starNakshatra: String,
    val rasi: String,
    val dosham: String,
    val maritalStatus: String,
    val motherTongue: String,
    val diet: String,
    val drinking: String,
    val smoking: String,
    val bio: String,
    val familyFather: String,
    val familyMother: String,
    val familySiblings: String,
    val familyType: String,
    val partnerAgeRange: String,
    val partnerHeightRange: String,
    val partnerEducation: String,
    val partnerLocation: String,
    val partnerCaste: String,
    val isShortlisted: Boolean = false,
    val isConnected: Boolean = false,
    val joinedDaysAgo: Int = 2
)

data class ChatMessage(
    val id: String,
    val profileId: String,
    val message: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val isRead: Boolean = true
)

data class ChatThread(
    val profile: Profile,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

data class MembershipPlan(
    val id: String,
    val title: String,
    val duration: String,
    val price: String,
    val originalPrice: String,
    val discountPercent: String,
    val isPopular: Boolean,
    val features: List<String>,
    val bgGradientColor: Long
)

data class UserPhoto(
    val id: String,
    val url: String,
    val isProfilePicture: Boolean,
    val status: String = "Approved" // "Approved", "In Review", "Under Verification"
)

data class VerificationStatus(
    val isFaceVerified: Boolean = false,
    val isGovtIdVerified: Boolean = false,
    val isPhoneVerified: Boolean = true,
    val isHoroscopeVerified: Boolean = true,
    val trustScore: Int = 75,
    val govtIdType: String = "Aadhaar Card",
    val govtIdNumber: String = "XXXX-XXXX-8921",
    val faceMatchAccuracy: Float = 0.0f,
    val verificationDate: String = "Pending"
)

data class ProfileCreationDraft(
    val profileFor: String = "Self",
    val name: String = "",
    val gender: String = "Male",
    val dob: String = "15/08/1997",
    val age: Int = 27,
    val height: String = "5 ft 9 in (175 cm)",
    val maritalStatus: String = "Never Married",
    val motherTongue: String = "Malayalam",
    val religion: String = "Hindu",
    val caste: String = "Nair",
    val gothram: String = "Kashyapa",
    val starNakshatra: String = "Rohini",
    val rasi: String = "Rishabham (Taurus)",
    val dosham: String = "No Dosham",
    val horoscopeMatchRequired: Boolean = true,
    val education: String = "B.Tech in Computer Science",
    val college: String = "NIT Calicut",
    val profession: String = "Senior Software Engineer",
    val company: String = "UST Global",
    val annualIncome: String = "₹ 18 - 22 Lakhs",
    val city: String = "Kochi",
    val district: String = "Ernakulam",
    val state: String = "Kerala",
    val nativePlace: String = "Aluva",
    val familyType: String = "Nuclear Family",
    val familyFather: String = "Retired Govt Official (KSEB)",
    val familyMother: String = "Homemaker",
    val familySiblings: String = "1 Younger Sister (Married)",
    val partnerAgeRange: String = "23 - 27 Yrs",
    val partnerHeightRange: String = "5 ft 2 in - 5 ft 7 in",
    val partnerEducation: String = "B.Tech / MCA / MBBS / Masters",
    val partnerLocation: String = "Kerala / Bangalore / Abroad",
    val partnerCaste: String = "Hindu - Nair (Open to other castes)",
    val bio: String = "Passionate tech professional rooted in traditional family values. Enjoys travel, photography, and classical music. Looking for a caring, progressive partner to build a wonderful journey together."
)

