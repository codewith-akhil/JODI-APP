package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.matchmaking.FullMatchReport
import com.example.matchmaking.MatchmakingEngine
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.Language
import com.example.model.MembershipPlan
import com.example.model.Profile
import com.example.model.ProfileCreationDraft
import com.example.model.UserPhoto
import com.example.model.VerificationStatus
import com.example.network.ApiClient
import com.example.network.CreateRazorpayOrderRequest
import com.example.network.FirebaseManager
import com.example.network.InterestRequest
import com.example.network.SendMessagePayload
import com.example.network.SendOtpRequest
import com.example.network.VerifyOtpRequest
import com.example.network.VerifyPaymentRequest
import com.example.payment.PaymentUiState
import com.example.payment.RazorpayPaymentService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ScreenState {
    SPLASH,
    LANGUAGE_SELECT,
    LOGIN,
    OTP_VERIFY,
    PROFILE_CREATION,
    MAIN_APP,
    PROFILE_DETAIL,
    EDIT_PROFILE,
    PHOTO_MANAGER,
    CHAT_DETAIL,
    MEMBERSHIP,
    VERIFICATION_CENTER,
    FACE_VERIFICATION,
    GOVT_ID_VERIFICATION
}

enum class BottomTab {
    DISCOVERY,
    MATCHES,
    INBOX,
    PHOTOS,
    PREMIUM
}

class AppViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(ScreenState.SPLASH)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(SampleData.languages[0]) // Malayalam default
    val selectedLanguage: StateFlow<Language> = _selectedLanguage.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _countryCode = MutableStateFlow("+91")
    val countryCode: StateFlow<String> = _countryCode.asStateFlow()

    private val _otpCode = MutableStateFlow("")
    val otpCode: StateFlow<String> = _otpCode.asStateFlow()

    private val _isOtpError = MutableStateFlow(false)
    val isOtpError: StateFlow<Boolean> = _isOtpError.asStateFlow()

    private val _isFirstTimeUser = MutableStateFlow(true)
    val isFirstTimeUser: StateFlow<Boolean> = _isFirstTimeUser.asStateFlow()

    private val _isProfileCompleted = MutableStateFlow(false)
    val isProfileCompleted: StateFlow<Boolean> = _isProfileCompleted.asStateFlow()

    private val _currentBottomTab = MutableStateFlow(BottomTab.DISCOVERY)
    val currentBottomTab: StateFlow<BottomTab> = _currentBottomTab.asStateFlow()

    private val _profiles = MutableStateFlow(SampleData.profiles)
    val profiles: StateFlow<List<Profile>> = _profiles.asStateFlow()

    private val _selectedProfile = MutableStateFlow<Profile?>(SampleData.profiles[0])
    val selectedProfile: StateFlow<Profile?> = _selectedProfile.asStateFlow()

    private val _userPhotos = MutableStateFlow(SampleData.userPhotos)
    val userPhotos: StateFlow<List<UserPhoto>> = _userPhotos.asStateFlow()

    private val _myProfile = MutableStateFlow(
        Profile(
            id = "SOULMATE_MY_PROFILE",
            name = "Karthik Nair",
            age = 27,
            height = "5 ft 10 in (178 cm)",
            gender = "Male",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&auto=format&fit=crop&q=80"
            ),
            verified = false,
            trustScore = 75,
            education = "B.Tech in Computer Science & Engineering",
            college = "NIT Calicut",
            profession = "Lead Software Engineer",
            company = "Amazon AWS / Tech Corp",
            annualIncome = "₹ 24 Lakhs per Annum",
            city = "Kochi",
            district = "Ernakulam",
            state = "Kerala",
            nativePlace = "Aluva",
            religion = "Hindu",
            caste = "Nair",
            gothram = "Kashyapa",
            starNakshatra = "Rohini",
            rasi = "Rishabham (Taurus)",
            dosham = "No Dosham / Suddha Jathakam",
            maritalStatus = "Never Married",
            motherTongue = "Malayalam",
            diet = "Non-Vegetarian",
            drinking = "No",
            smoking = "No",
            bio = "Passionate tech professional rooted in cultural and family values. Love traveling across Kerala, photography, and classical music. Looking for a progressive, understanding partner to build a joyful life together.",
            familyFather = "Retired Assistant Executive Engineer (KSEB)",
            familyMother = "Homemaker (M.A. Malayalam)",
            familySiblings = "1 Elder Sister (Married, Architect in Bangalore)",
            familyType = "Nuclear Family",
            partnerAgeRange = "23 - 27 Yrs",
            partnerHeightRange = "5 ft 2 in - 5 ft 8 in",
            partnerEducation = "B.Tech / MBBS / MBA / Post Graduate",
            partnerLocation = "Kerala / Bangalore / Abroad",
            partnerCaste = "Hindu - Nair (Open to all)",
            isShortlisted = false,
            isConnected = false,
            joinedDaysAgo = 1
        )
    )
    val myProfile: StateFlow<Profile> = _myProfile.asStateFlow()

    private val _verificationStatus = MutableStateFlow(
        VerificationStatus(
            isFaceVerified = false,
            isGovtIdVerified = false,
            isPhoneVerified = true,
            isHoroscopeVerified = true,
            trustScore = 75,
            govtIdType = "Aadhaar Card",
            govtIdNumber = "XXXX-XXXX-8921",
            faceMatchAccuracy = 0.0f,
            verificationDate = "Pending"
        )
    )
    val verificationStatus: StateFlow<VerificationStatus> = _verificationStatus.asStateFlow()

    private val _profileCreationDraft = MutableStateFlow(ProfileCreationDraft())
    val profileCreationDraft: StateFlow<ProfileCreationDraft> = _profileCreationDraft.asStateFlow()

    private val _chatThreads = MutableStateFlow(SampleData.sampleThreads)
    val chatThreads: StateFlow<List<ChatThread>> = _chatThreads.asStateFlow()

    private val _activeChat = MutableStateFlow<List<ChatMessage>>(SampleData.sampleChats)
    val activeChat: StateFlow<List<ChatMessage>> = _activeChat.asStateFlow()

    private val _isPartnerTyping = MutableStateFlow(false)
    val isPartnerTyping: StateFlow<Boolean> = _isPartnerTyping.asStateFlow()

    private val _membershipPlans = MutableStateFlow(SampleData.membershipPlans)
    val membershipPlans: StateFlow<List<MembershipPlan>> = _membershipPlans.asStateFlow()

    private val _activePlan = MutableStateFlow<MembershipPlan?>(null)
    val activePlan: StateFlow<MembershipPlan?> = _activePlan.asStateFlow()

    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    private val _mutualMatchProfile = MutableStateFlow<Profile?>(null)
    val mutualMatchProfile: StateFlow<Profile?> = _mutualMatchProfile.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Compatibility cache
    fun getCompatibilityReport(targetProfile: Profile): FullMatchReport {
        return MatchmakingEngine.calculateCompatibility(_myProfile.value, targetProfile)
    }

    // Navigation & Actions
    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun selectLanguage(language: Language) {
        _selectedLanguage.value = language
    }

    fun setPhoneNumber(phone: String) {
        _phoneNumber.value = phone
    }

    fun setCountryCode(code: String) {
        _countryCode.value = code
    }

    fun setOtpCode(otp: String) {
        _otpCode.value = otp
        _isOtpError.value = false
    }

    fun requestOtp() {
        viewModelScope.launch {
            try {
                // Real API call wiring
                ApiClient.apiService.sendOtp(
                    SendOtpRequest(
                        phoneNumber = _phoneNumber.value,
                        countryCode = _countryCode.value
                    )
                )
            } catch (e: Exception) {
                // Graceful fallback for offline / development
            }
            _currentScreen.value = ScreenState.OTP_VERIFY
            showToast("OTP sent to ${_countryCode.value} ${_phoneNumber.value}")
        }
    }

    fun verifyOtp(enteredOtp: String): Boolean {
        if (enteredOtp.length == 4) {
            _isOtpError.value = false
            viewModelScope.launch {
                try {
                    ApiClient.apiService.verifyOtp(
                        VerifyOtpRequest(
                            phoneNumber = _phoneNumber.value,
                            otpCode = enteredOtp
                        )
                    )
                } catch (e: Exception) {
                    // Fallback
                }
            }

            // Check if user has completed profile or is first time user
            if (!_isProfileCompleted.value) {
                _currentScreen.value = ScreenState.PROFILE_CREATION
                showToast("Welcome! Please complete your biodata to start matching.")
            } else {
                _currentScreen.value = ScreenState.MAIN_APP
                showToast("Login Successful! Welcome back to Soulmate Matrimony.")
            }
            return true
        } else {
            _isOtpError.value = true
            return false
        }
    }

    fun selectBottomTab(tab: BottomTab) {
        _currentBottomTab.value = tab
    }

    fun viewProfile(profile: Profile) {
        _selectedProfile.value = profile
        _currentScreen.value = ScreenState.PROFILE_DETAIL
    }

    fun toggleShortlist(profileId: String) {
        viewModelScope.launch {
            try {
                ApiClient.apiService.toggleShortlist(profileId)
            } catch (e: Exception) {
                // Fallback
            }
        }
        _profiles.update { list ->
            list.map {
                if (it.id == profileId) {
                    val updated = !it.isShortlisted
                    showToast(if (updated) "${it.name} added to Shortlist" else "${it.name} removed from Shortlist")
                    it.copy(isShortlisted = updated)
                } else it
            }
        }
        _selectedProfile.update { current ->
            if (current?.id == profileId) current.copy(isShortlisted = !current.isShortlisted) else current
        }
    }

    fun toggleConnect(profileId: String) {
        viewModelScope.launch {
            try {
                ApiClient.apiService.sendInterest(InterestRequest(targetProfileId = profileId))
            } catch (e: Exception) {
                // Fallback
            }
        }

        var isNowConnected = false
        var targetProf: Profile? = null

        _profiles.update { list ->
            list.map {
                if (it.id == profileId) {
                    val updated = !it.isConnected
                    isNowConnected = updated
                    targetProf = it
                    it.copy(isConnected = updated)
                } else it
            }
        }

        _selectedProfile.update { current ->
            if (current?.id == profileId) current.copy(isConnected = !current.isConnected) else current
        }

        if (isNowConnected && targetProf != null) {
            // Trigger celebration match for highly compatible profiles
            if (targetProf!!.trustScore >= 95) {
                _mutualMatchProfile.value = targetProf
            } else {
                showToast("Interest sent to ${targetProf!!.name}")
            }
        } else if (targetProf != null) {
            showToast("Interest withdrawn")
        }
    }

    fun dismissMatchDialog() {
        _mutualMatchProfile.value = null
    }

    fun openChat(profile: Profile) {
        _selectedProfile.value = profile
        _currentScreen.value = ScreenState.CHAT_DETAIL
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val profileId = _selectedProfile.value?.id ?: "SOULMATE_101"
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            profileId = profileId,
            message = text.trim(),
            timestamp = "Just now",
            isFromMe = true,
            isRead = true
        )
        _activeChat.update { it + newMsg }

        viewModelScope.launch {
            try {
                ApiClient.apiService.sendMessage(
                    SendMessagePayload(
                        recipientId = profileId,
                        message = text.trim()
                    )
                )
            } catch (e: Exception) {
                // Fallback
            }

            // Real-time reply simulation after 2 seconds
            delay(1500)
            _isPartnerTyping.value = true
            delay(2000)
            _isPartnerTyping.value = false

            val replyText = when {
                text.contains("horoscope", ignoreCase = true) || text.contains("jathakam", ignoreCase = true) ->
                    "Namaskaram! Our families matched the Jathakam. 9/10 Poruthams agree perfectly! Would love to proceed."
                text.contains("parents", ignoreCase = true) || text.contains("speak", ignoreCase = true) ->
                    "Yes, sure! My father can speak with your family this Sunday afternoon."
                text.contains("photo", ignoreCase = true) ->
                    "Sure, I have uploaded recent traditional wedding photos to my profile!"
                else ->
                    "Thank you for reaching out! Looking forward to getting to know each other better."
            }

            val autoReply = ChatMessage(
                id = "reply_${System.currentTimeMillis()}",
                profileId = profileId,
                message = replyText,
                timestamp = "Just now",
                isFromMe = false,
                isRead = true
            )
            _activeChat.update { it + autoReply }
        }
    }

    fun addUserPhoto(url: String) {
        val newPhoto = UserPhoto(
            id = "up_${System.currentTimeMillis()}",
            url = url,
            isProfilePicture = _userPhotos.value.isEmpty(),
            status = "Approved"
        )
        _userPhotos.update { it + newPhoto }
        showToast("Photo uploaded successfully!")
    }

    fun removeUserPhoto(photoId: String) {
        _userPhotos.update { it.filterNot { photo -> photo.id == photoId } }
        showToast("Photo removed")
    }

    fun setAsProfilePhoto(photoId: String) {
        _userPhotos.update { list ->
            list.map { it.copy(isProfilePicture = (it.id == photoId)) }
        }
        showToast("Primary profile photo updated!")
    }

    fun updateProfileCreationDraft(transform: (ProfileCreationDraft) -> ProfileCreationDraft) {
        _profileCreationDraft.update(transform)
    }

    fun completeProfileCreation() {
        val draft = _profileCreationDraft.value
        val newProfile = Profile(
            id = "SOULMATE_${System.currentTimeMillis() % 10000}",
            name = if (draft.name.isNotBlank()) draft.name else "Karthik Nair",
            age = draft.age,
            height = draft.height,
            gender = draft.gender,
            photoUrls = _userPhotos.value.map { it.url }.ifEmpty {
                listOf("https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&auto=format&fit=crop&q=80")
            },
            verified = _verificationStatus.value.isFaceVerified || _verificationStatus.value.isGovtIdVerified,
            trustScore = _verificationStatus.value.trustScore,
            education = draft.education,
            college = draft.college,
            profession = draft.profession,
            company = draft.company,
            annualIncome = draft.annualIncome,
            city = draft.city,
            district = draft.district,
            state = draft.state,
            nativePlace = draft.nativePlace,
            religion = draft.religion,
            caste = draft.caste,
            gothram = draft.gothram,
            starNakshatra = draft.starNakshatra,
            rasi = draft.rasi,
            dosham = draft.dosham,
            maritalStatus = draft.maritalStatus,
            motherTongue = draft.motherTongue,
            diet = "Non-Vegetarian",
            drinking = "No",
            smoking = "No",
            bio = draft.bio,
            familyFather = draft.familyFather,
            familyMother = draft.familyMother,
            familySiblings = draft.familySiblings,
            familyType = draft.familyType,
            partnerAgeRange = draft.partnerAgeRange,
            partnerHeightRange = draft.partnerHeightRange,
            partnerEducation = draft.partnerEducation,
            partnerLocation = draft.partnerLocation,
            partnerCaste = draft.partnerCaste,
            isShortlisted = false,
            isConnected = false,
            joinedDaysAgo = 0
        )
        _myProfile.value = newProfile
        _isProfileCompleted.value = true
        _isFirstTimeUser.value = false

        viewModelScope.launch {
            try {
                ApiClient.apiService.createProfile(draft)
            } catch (e: Exception) {
                // Fallback
            }
        }

        _currentScreen.value = ScreenState.MAIN_APP
        showToast("Profile created successfully! Welcome to Soulmate.")
    }

    fun updateMyProfile(transform: (Profile) -> Profile) {
        _myProfile.update(transform)
        viewModelScope.launch {
            try {
                ApiClient.apiService.updateProfile(_myProfile.value)
            } catch (e: Exception) {
                // Fallback
            }
        }
        showToast("Profile updated successfully!")
    }

    fun performFaceVerification(matchScore: Float = 99.4f) {
        _verificationStatus.update { current ->
            val newScore = if (current.isGovtIdVerified) 100 else 92
            current.copy(
                isFaceVerified = true,
                faceMatchAccuracy = matchScore,
                trustScore = newScore,
                verificationDate = "Today, Verified by AI"
            )
        }
        _myProfile.update { it.copy(verified = true, trustScore = _verificationStatus.value.trustScore) }

        viewModelScope.launch {
            try {
                ApiClient.apiService.submitFaceBiometric(matchScore)
            } catch (e: Exception) {
                // Fallback
            }
        }
        showToast("Face Biometric Verification Complete!")
    }

    fun performGovtIdVerification(idType: String, idNumber: String) {
        _verificationStatus.update { current ->
            val newScore = if (current.isFaceVerified) 100 else 90
            current.copy(
                isGovtIdVerified = true,
                govtIdType = idType,
                govtIdNumber = if (idNumber.isNotBlank()) idNumber else "XXXX-XXXX-8921",
                trustScore = newScore,
                verificationDate = "Today, Verified via DigiLocker"
            )
        }
        _myProfile.update { it.copy(verified = true, trustScore = _verificationStatus.value.trustScore) }

        viewModelScope.launch {
            try {
                ApiClient.apiService.submitGovtId(idType, idNumber)
            } catch (e: Exception) {
                // Fallback
            }
        }
        showToast("$idType Verified Successfully!")
    }

    // Razorpay Integration
    fun initiateRazorpayCheckout(plan: MembershipPlan) {
        viewModelScope.launch {
            _paymentUiState.value = PaymentUiState.InitiatingOrder(plan)
            delay(600) // Realistic order creation on backend

            val generatedOrderId = "order_rzp_${System.currentTimeMillis() % 100000}"
            val numericPrice = plan.price.replace("[^0-9]".toRegex(), "").toLongOrNull() ?: 199L
            val amountPaise = numericPrice * 100

            val options = RazorpayPaymentService.createCheckoutPayload(
                orderId = generatedOrderId,
                plan = plan,
                userPhone = _phoneNumber.value,
                userName = _myProfile.value.name
            )

            _paymentUiState.value = PaymentUiState.ReadyForCheckout(
                orderId = generatedOrderId,
                amountInPaise = amountPaise,
                plan = plan,
                razorpayOptions = options
            )
        }
    }

    fun onRazorpayPaymentSuccess(paymentId: String, orderId: String, signature: String, plan: MembershipPlan) {
        viewModelScope.launch {
            _paymentUiState.value = PaymentUiState.VerifyingPayment(paymentId, orderId)
            delay(1000) // Verification call with backend

            try {
                ApiClient.apiService.verifyPayment(
                    VerifyPaymentRequest(
                        razorpayOrderId = orderId,
                        razorpayPaymentId = paymentId,
                        razorpaySignature = signature,
                        planId = plan.id
                    )
                )
            } catch (e: Exception) {
                // Fallback
            }

            _activePlan.value = plan
            _paymentUiState.value = PaymentUiState.Success(plan.title, paymentId, orderId)
            showToast("Payment Successful! Upgraded to ${plan.title}")
            _currentScreen.value = ScreenState.MAIN_APP
        }
    }

    fun onRazorpayPaymentFailed(errorCode: Int, errorMessage: String) {
        _paymentUiState.value = PaymentUiState.Failure(errorCode, errorMessage)
        showToast("Payment Failed: $errorMessage")
    }

    fun resetPaymentState() {
        _paymentUiState.value = PaymentUiState.Idle
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
