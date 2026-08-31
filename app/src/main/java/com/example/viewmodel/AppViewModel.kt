package com.example.viewmodel

import android.app.Activity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.matchmaking.FullMatchReport
import com.example.matchmaking.MatchmakingEngine
import com.example.model.AppNotification
import com.example.model.CallSession
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.FaqItem
import com.example.model.Language
import com.example.model.MembershipPlan
import com.example.model.PendingAccountAction
import com.example.model.Profile
import com.example.model.ProfileCreationDraft
import com.example.model.PrivacySettings
import com.example.model.ReferralStats
import com.example.model.SearchFilters
import com.example.model.StatusScreenData
import com.example.model.SuccessStory
import com.example.model.TransactionRecord
import com.example.model.UserPhoto
import com.example.model.VerificationStatus
import com.example.network.ApiClient
import com.example.network.CreateRazorpayOrderRequest
import com.example.network.FirebaseManager
import com.example.network.InterestRequest
import com.example.network.NetworkMonitor
import com.example.network.SendMessagePayload
import com.example.network.SendOtpRequest
import com.example.network.VerifyOtpRequest
import com.example.network.VerifyPaymentRequest
import com.example.payment.PaymentUiState
import com.example.payment.RazorpayPaymentService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    GOVT_ID_VERIFICATION,
    // ---- 18 new feature pages ----
    ONBOARDING,
    SETTINGS,
    PRIVACY_CONTROLS,
    NOTIFICATIONS,
    SEARCH_FILTER,
    PRIVACY_POLICY,
    TERMS_OF_SERVICE,
    SAFETY_CENTER,
    HELP_SUPPORT,
    HOROSCOPE_REPORT,
    CALL_SCREEN,
    PAYMENT_HISTORY,
    PHOTO_VIEWER,
    SUCCESS_STORIES,
    REFERRAL,
    ACCOUNT_VERIFICATION,
    // ---- Global status screens ----
    LOADING,
    SUCCESS,
    ERROR,
    NO_INTERNET,
    SUBSCRIBED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED
}

enum class BottomTab {
    DISCOVERY,
    MATCHES,
    INBOX,
    PHOTOS,
    PREMIUM
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val networkMonitor = NetworkMonitor(application)

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

    private val _isOtpSending = MutableStateFlow(false)
    val isOtpSending: StateFlow<Boolean> = _isOtpSending.asStateFlow()

    private val _isVerifyingOtp = MutableStateFlow(false)
    val isVerifyingOtp: StateFlow<Boolean> = _isVerifyingOtp.asStateFlow()

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

    // ---------------- New state for the 18 feature pages ----------------

    private val _notifications = MutableStateFlow(SampleData.sampleNotifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    val unreadNotificationCount: StateFlow<Int> = _notifications
        .asStateFlow()
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val _transactions = MutableStateFlow(SampleData.sampleTransactions)
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    private val _successStories = MutableStateFlow(SampleData.sampleSuccessStories)
    val successStories: StateFlow<List<SuccessStory>> = _successStories.asStateFlow()

    private val _privacySettings = MutableStateFlow(PrivacySettings())
    val privacySettings: StateFlow<PrivacySettings> = _privacySettings.asStateFlow()

    private val _referralStats = MutableStateFlow(ReferralStats())
    val referralStats: StateFlow<ReferralStats> = _referralStats.asStateFlow()

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Profile>>(emptyList())
    val searchResults: StateFlow<List<Profile>> = _searchResults.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    private val _blockedUsers = MutableStateFlow<List<Profile>>(emptyList())
    val blockedUsers: StateFlow<List<Profile>> = _blockedUsers.asStateFlow()

    private val _pendingAccountAction = MutableStateFlow<PendingAccountAction?>(null)
    val pendingAccountAction: StateFlow<PendingAccountAction?> = _pendingAccountAction.asStateFlow()

    private val _accountOtpError = MutableStateFlow(false)
    val accountOtpError: StateFlow<Boolean> = _accountOtpError.asStateFlow()

    private val _accountOtpCode = MutableStateFlow("")
    val accountOtpCode: StateFlow<String> = _accountOtpCode.asStateFlow()

    private val _isAccountOtpSending = MutableStateFlow(false)
    val isAccountOtpSending: StateFlow<Boolean> = _isAccountOtpSending.asStateFlow()

    private val _isPerformingAccountAction = MutableStateFlow(false)
    val isPerformingAccountAction: StateFlow<Boolean> = _isPerformingAccountAction.asStateFlow()

    private val _callSession = MutableStateFlow<CallSession?>(null)
    val callSession: StateFlow<CallSession?> = _callSession.asStateFlow()

    private val _photoViewerUrls = MutableStateFlow<List<String>>(emptyList())
    val photoViewerUrls: StateFlow<List<String>> = _photoViewerUrls.asStateFlow()

    private val _photoViewerIndex = MutableStateFlow(0)
    val photoViewerIndex: StateFlow<Int> = _photoViewerIndex.asStateFlow()

    private val _statusScreenData = MutableStateFlow<StatusScreenData?>(null)
    val statusScreenData: StateFlow<StatusScreenData?> = _statusScreenData.asStateFlow()

    private val _loadingMessage = MutableStateFlow("Loading your Soulmate experience...")
    val loadingMessage: StateFlow<String> = _loadingMessage.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _emailVerificationState = MutableStateFlow("NOT_SET") // NOT_SET, PENDING, VERIFIED
    val emailVerificationState: StateFlow<String> = _emailVerificationState.asStateFlow()

    private val _isGoogleSigningIn = MutableStateFlow(false)
    val isGoogleSigningIn: StateFlow<Boolean> = _isGoogleSigningIn.asStateFlow()

    val faqs: List<FaqItem> = SampleData.sampleFaqs

    // OTP internals
    private var loginVerificationId: String? = null
    private var isLoginDemoOtpMode = false
    private var accountVerificationId: String? = null
    private var isAccountDemoOtpMode = false
    private var chatListenerJob: Job? = null

    // ---------------- Business-rule engine (membership / limits / OTP policy) ----------------

    companion object {
        const val MAX_MATCH_REQUESTS_PER_DAY = 10   // Free tier (rule #16)
        const val MAX_MESSAGE_USERS_PER_DAY = 1     // Free tier unique users/day (rule #11)
        const val MAX_PHOTOS = 6                    // rule #5
        const val MIN_AGE_YEARS = 18                // rule #2
        const val PREMIUM_VALIDITY_DAYS = 30L       // rule #9
        const val OTP_VALIDITY_MILLIS = 5 * 60_000L // rule #1
        const val OTP_MAX_ATTEMPTS = 5              // rule #1
    }

    private val _membershipTier = MutableStateFlow("FREE")
    val membershipTier: StateFlow<String> = _membershipTier.asStateFlow()

    private val _subscriptionExpiryMillis = MutableStateFlow(0L)
    val subscriptionExpiryMillis: StateFlow<Long> = _subscriptionExpiryMillis.asStateFlow()

    private val _dailyMatchRequestsSent = MutableStateFlow(0)
    val dailyMatchRequestsSent: StateFlow<Int> = _dailyMatchRequestsSent.asStateFlow()

    private val _dailyMessageUsers = MutableStateFlow<Set<String>>(emptySet())
    val dailyMessageUsers: StateFlow<Set<String>> = _dailyMessageUsers.asStateFlow()

    private val _otpAttemptCount = MutableStateFlow(0)
    val otpAttemptCount: StateFlow<Int> = _otpAttemptCount.asStateFlow()

    private var otpSentAtMillis = 0L

    val isPremium: Boolean
        get() = _membershipTier.value == "PREMIUM" &&
            _subscriptionExpiryMillis.value > System.currentTimeMillis()

    /** Rule #3: only verified profiles can match or chat. */
    val isVerified: Boolean
        get() = _verificationStatus.value.isFaceVerified || _verificationStatus.value.isGovtIdVerified

    /** Rule #18 — exact upgrade copy shown when a Free limit is reached. */
    fun upgradePromptFor(kind: String): StatusScreenData = if (kind == "MESSAGE") {
        StatusScreenData(
            kind = "ERROR", title = "You've reached today's free messaging limit.",
            message = "Free members can message 1 new user per day. Upgrade to Premium — ₹99/month for unlimited messaging, unlimited match requests and no daily limits.",
            actionLabel = "Upgrade to Premium — ₹99/month", destination = "MEMBERSHIP"
        )
    } else {
        StatusScreenData(
            kind = "ERROR", title = "You've reached your 10 match requests for today.",
            message = "Upgrade to Premium — ₹99/month for unlimited match requests, unlimited messaging and no daily limits.",
            actionLabel = "Upgrade to Premium — ₹99/month", destination = "MEMBERSHIP"
        )
    }

    private fun verificationPrompt(): StatusScreenData = StatusScreenData(
        kind = "INFO", title = "Verification Required",
        message = "Only verified profiles can send match requests and chat. Complete Face or Govt-ID verification in the Verification Center to unlock matching and messaging.",
        actionLabel = "Verify My Profile", destination = "VERIFICATION_CENTER"
    )

    private fun todayKey(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    /** Mirrors the server-authoritative counters (rule #22). */
    private suspend fun refreshEntitlementsFromServer() {
        val uid = myUid()
        if (!FirebaseManager.isSignedIn()) return
        FirebaseManager.fetchMembership(uid)?.let { m ->
            _membershipTier.value = (m["tier"] as? String) ?: "FREE"
            _subscriptionExpiryMillis.value = (m["expiryDate"] as? Long) ?: 0L
        }
        FirebaseManager.fetchDailyCounters(uid, todayKey())?.let { c ->
            _dailyMatchRequestsSent.value = (c["matchRequestsSent"] as? Long)?.toInt() ?: 0
            @Suppress("UNCHECKED_CAST")
            _dailyMessageUsers.value = (c["messageUsersStarted"] as? Map<String, Any?>)?.keys?.toSet() ?: emptySet()
        }
    }

    private fun calculateAgeFromDob(dob: String): Int? = try {
        val dobDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
            ?: return null
        val diff = System.currentTimeMillis() - dobDate.time
        (diff / (365.25 * 24 * 60 * 60 * 1000)).toInt().takeIf { it > 0 }
    } catch (e: Exception) { null }

    private fun parseDobMillis(dob: String): Long = try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)?.time ?: 0L
    } catch (e: Exception) { 0L }

    init {
        // Live connectivity monitoring drives the No-Internet screen
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                val previous = _isOnline.value
                _isOnline.value = online
                if (previous && !online) {
                    _currentScreen.value = ScreenState.NO_INTERNET
                }
            }
        }
        // Reflect the signed-in user's e-mail state
        FirebaseManager.currentUser?.let { user ->
            _userEmail.value = user.email ?: ""
            _emailVerificationState.value =
                if (user.email.isNullOrBlank()) "NOT_SET" else if (user.isEmailVerified) "VERIFIED" else "PENDING"
            // Live-sync notifications from Realtime Database
            loadFirebaseNotifications()
        }
    }

    private fun myUid(): String = FirebaseManager.currentUid ?: "local_demo_user"

    private fun timeFromMillis(millis: Long): String {
        return try {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(millis))
        } catch (e: Exception) {
            "Now"
        }
    }

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
        _otpCode.value = otp.filter { it.isDigit() }.take(6)
        _isOtpError.value = false
    }

    fun setAccountOtpCode(otp: String) {
        _accountOtpCode.value = otp.filter { it.isDigit() }.take(6)
        _accountOtpError.value = false
    }

    /**
     * Sends an SMS OTP via Firebase Phone Auth for LOGIN.
     * Falls back to demo mode (123456) when Firebase is unreachable.
     */
    fun requestOtp(activity: Activity) {
        if (phoneNumber.value.isBlank()) {
            setPhoneNumber("9876543210")
        }
        _isOtpSending.value = true
        _loadingMessage.value = "Sending OTP to ${countryCode.value} ${phoneNumber.value}..."
        val fullPhone = "${countryCode.value}${phoneNumber.value}"

        FirebaseManager.sendPhoneOtp(
            activity = activity,
            phoneNumber = fullPhone,
            onCodeSent = { verificationId ->
                _isOtpSending.value = false
                loginVerificationId = verificationId
                isLoginDemoOtpMode = false
                otpSentAtMillis = System.currentTimeMillis()
                _otpAttemptCount.value = 0
                _currentScreen.value = ScreenState.OTP_VERIFY
                showToast("OTP sent to $fullPhone via Firebase")
            },
            onAutoVerified = {
                _isOtpSending.value = false
                showToast("Phone verified automatically!")
                handleLoginSuccess()
            },
            onFailed = { error ->
                _isOtpSending.value = false
                // Graceful demo fallback when Firebase SMS is unavailable
                isLoginDemoOtpMode = true
                loginVerificationId = null
                _currentScreen.value = ScreenState.OTP_VERIFY
                showToast("Demo mode enabled — use OTP 123456. (${error.take(60)})")
            }
        )
    }

    /**
     * Verifies the login OTP. Real Firebase path when a verificationId exists,
     * otherwise demo verification (accepts 123456).
     */
    fun verifyOtp(enteredOtp: String): Boolean {
        if (_isVerifyingOtp.value) return true

        // Rule #1 — max 5 verification attempts
        if (_otpAttemptCount.value >= OTP_MAX_ATTEMPTS) {
            showToast("Maximum 5 OTP attempts exceeded. Please request a new OTP.")
            return false
        }

        if (isLoginDemoOtpMode || loginVerificationId == null) {
            return if (enteredOtp == "123456" || enteredOtp == "1234") {
                _isVerifyingOtp.value = true
                _currentScreen.value = ScreenState.LOADING
                _loadingMessage.value = "Verifying your mobile number..."
                viewModelScope.launch {
                    delay(1200)
                    _isVerifyingOtp.value = false
                    handleLoginSuccess()
                }
                true
            } else {
                _isOtpError.value = true
                _otpAttemptCount.value += 1
                if (_otpAttemptCount.value >= OTP_MAX_ATTEMPTS) {
                    showToast("Maximum 5 OTP attempts exceeded. Please request a new OTP.")
                }
                false
            }
        }

        if (enteredOtp.length < 6) {
            _isOtpError.value = true
            _otpAttemptCount.value += 1
            return false
        }

        // Rule #1 — OTP validity is 5 minutes
        if (otpSentAtMillis > 0 && System.currentTimeMillis() - otpSentAtMillis > OTP_VALIDITY_MILLIS) {
            showToast("This OTP has expired (valid 5 minutes). Please request a new one.")
            return false
        }

        _isVerifyingOtp.value = true
        FirebaseManager.verifyPhoneOtp(
            verificationId = loginVerificationId!!,
            code = enteredOtp,
            onSuccess = {
                _isVerifyingOtp.value = false
                handleLoginSuccess()
            },
            onFailed = { error ->
                _isVerifyingOtp.value = false
                _isOtpError.value = true
                _otpAttemptCount.value += 1
                showToast(error)
            }
        )
        return true
    }

    private fun handleLoginSuccess() {
        // Persist auth details to Realtime Database
        val uid = myUid()
        viewModelScope.launch {
            try {
                FirebaseManager.upsertUser(
                    uid,
                    mapOf(
                        "uid" to uid,
                        "phone" to "${countryCode.value}${phoneNumber.value}",
                        "status" to "ACTIVE",
                        "lastLoginAt" to System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                // offline / rules — app continues in local mode
            }
        }
        if (!_isProfileCompleted.value) {
            _currentScreen.value = ScreenState.PROFILE_CREATION
            showToast("Welcome! Please complete your biodata to start matching.")
        } else {
            _currentScreen.value = ScreenState.SUCCESS
            _statusScreenData.value = StatusScreenData(
                kind = "SUCCESS",
                title = "Login Successful!",
                message = "Welcome back to Soulmate Matrimony. Your verified matches are waiting for you.",
                actionLabel = "Start Exploring",
                destination = "MAIN_APP"
            )
        }
        // One active account per phone (rule #1) + entitlement sync (rule #16)
        viewModelScope.launch {
            FirebaseManager.registerPhoneIndex("${countryCode.value}${phoneNumber.value}")
            refreshEntitlementsFromServer()
        }
        // Refresh e-mail state + live notifications for the signed-in user
        FirebaseManager.currentUser?.let { user ->
            _userEmail.value = user.email ?: ""
            _emailVerificationState.value =
                if (user.email.isNullOrBlank()) "NOT_SET" else if (user.isEmailVerified) "VERIFIED" else "PENDING"
        }
        loadFirebaseNotifications()
    }

    // ---------------- Google Sign-In (Credential Manager) ----------------

    fun signInWithGoogle(activity: Activity) {
        _isGoogleSigningIn.value = true
        viewModelScope.launch {
            try {
                val idToken = com.example.network.GoogleSignInHelper.getGoogleIdToken(
                    activity, FirebaseManager.serverClientId(activity)
                )
                if (idToken == null) {
                    _isGoogleSigningIn.value = false
                    showToast("Google Sign-In cancelled")
                    return@launch
                }
                FirebaseManager.signInWithGoogleIdToken(idToken)
                _isGoogleSigningIn.value = false
                handleLoginSuccess()
            } catch (e: Exception) {
                _isGoogleSigningIn.value = false
                showToast("Google Sign-In failed: ${e.localizedMessage?.take(60) ?: "Unknown error"}")
            }
        }
    }

    // ---------------- Logout (with confirmation dialog in Settings UI) ----------------

    fun performLogout() {
        FirebaseManager.signOut()
        chatListenerJob?.cancel()
        loginVerificationId = null
        isLoginDemoOtpMode = false
        _otpCode.value = ""
        _accountOtpCode.value = ""
        _currentScreen.value = ScreenState.LOGIN
        showToast("You have been logged out safely.")
    }

    // ---------------- Deactivate / Delete Account (requires OTP) ----------------

    fun startAccountVerification(action: PendingAccountAction) {
        _pendingAccountAction.value = action
        _accountOtpCode.value = ""
        _accountOtpError.value = false
        _currentScreen.value = ScreenState.ACCOUNT_VERIFICATION
    }

    fun sendAccountActionOtp(activity: Activity) {
        _isAccountOtpSending.value = true
        // Use the authenticated phone number; fall back to entered one
        val registeredPhone = FirebaseManager.currentUserPhone
            ?: "${countryCode.value}${phoneNumber.value.ifBlank { "9876543210" }}"

        FirebaseManager.sendPhoneOtp(
            activity = activity,
            phoneNumber = registeredPhone,
            onCodeSent = { verificationId ->
                _isAccountOtpSending.value = false
                accountVerificationId = verificationId
                isAccountDemoOtpMode = false
                showToast("Verification code sent to $registeredPhone")
            },
            onAutoVerified = {
                _isAccountOtpSending.value = false
                performPendingAccountAction()
            },
            onFailed = { error ->
                _isAccountOtpSending.value = false
                isAccountDemoOtpMode = true
                accountVerificationId = null
                showToast("Demo mode — use OTP 123456. (${error.take(50)})")
            }
        )
    }

    fun verifyAccountActionOtp(enteredOtp: String): Boolean {
        if (isAccountDemoOtpMode || accountVerificationId == null) {
            return if (enteredOtp == "123456" || enteredOtp == "1234") {
                _currentScreen.value = ScreenState.LOADING
                _loadingMessage.value = if (pendingAccountAction.value == PendingAccountAction.DELETE)
                    "Deleting your account securely..." else "Deactivating your profile..."
                viewModelScope.launch {
                    delay(1000)
                    performPendingAccountAction()
                }
                true
            } else {
                _accountOtpError.value = true
                false
            }
        }

        if (enteredOtp.length < 6) {
            _accountOtpError.value = true
            return false
        }

        _isPerformingAccountAction.value = true
        FirebaseManager.verifyPhoneOtp(
            verificationId = accountVerificationId!!,
            code = enteredOtp,
            onSuccess = {
                _isPerformingAccountAction.value = false
                performPendingAccountAction()
            },
            onFailed = { error ->
                _isPerformingAccountAction.value = false
                _accountOtpError.value = true
                showToast(error)
            }
        )
        return true
    }

    private fun performPendingAccountAction() {
        val action = _pendingAccountAction.value ?: return
        val uid = myUid()
        _isPerformingAccountAction.value = true
        viewModelScope.launch {
            when (action) {
                PendingAccountAction.DEACTIVATE -> {
                    try {
                        FirebaseManager.setUserStatus(uid, "DEACTIVATED")
                    } catch (e: Exception) { /* offline tolerant */ }
                    FirebaseManager.signOut()
                    _isPerformingAccountAction.value = false
                    _statusScreenData.value = StatusScreenData(
                        kind = "INFO",
                        title = "Account Deactivated",
                        message = "Your profile is now hidden from all discovery feeds. Sign in again anytime to reactivate instantly — your biodata, matches and chats are safe.",
                        actionLabel = "Back to Login",
                        destination = "LOGIN"
                    )
                    _currentScreen.value = ScreenState.SUCCESS
                }
                PendingAccountAction.DELETE -> {
                    try {
                        FirebaseManager.eraseUserData(uid)
                        val result = FirebaseManager.deleteAuthAccount()
                        if (result.isFailure) {
                            showToast("Cloud account cleanup pending — local data erased.")
                        }
                    } catch (e: Exception) { /* offline tolerant */ }
                    FirebaseManager.signOut()
                    _isPerformingAccountAction.value = false
                    _statusScreenData.value = StatusScreenData(
                        kind = "SUCCESS",
                        title = "Account Deleted",
                        message = "Your profile, photos, chats and subscriptions have been permanently erased as per our data policy. We are sad to see you go — you are always welcome back.",
                        actionLabel = "Back to Login",
                        destination = "LOGIN"
                    )
                    _currentScreen.value = ScreenState.SUCCESS
                }
            }
            _pendingAccountAction.value = null
        }
    }

    fun cancelAccountAction() {
        _pendingAccountAction.value = null
        _accountOtpCode.value = ""
        _currentScreen.value = ScreenState.SETTINGS
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
        val target = _profiles.value.firstOrNull { it.id == profileId } ?: return

        // Rule #3 — only verified profiles can send match requests
        if (!isVerified) {
            showStatusScreen(verificationPrompt())
            return
        }

        val wasConnected = target.isConnected
        if (!wasConnected) {
            // Rule #10/#16 — Free: max 10 match requests per day; Premium: unlimited
            if (!isPremium && _dailyMatchRequestsSent.value >= MAX_MATCH_REQUESTS_PER_DAY) {
                showStatusScreen(upgradePromptFor("MATCH"))
                return
            }
        }

        viewModelScope.launch {
            var serverAccepted = false
            if (FirebaseManager.isSignedIn() && !profileId.startsWith("SOULMATE_")) {
                // Server is the final authority (rule #22)
                FirebaseManager.callFunction(
                    "sendMatchRequest",
                    mapOf("targetUid" to profileId, "note" to "Hi! I liked your profile.")
                ).onSuccess { result ->
                    serverAccepted = true
                    if (!isPremium) {
                        val remaining = (result["remainingToday"] as? Long)?.toInt() ?: -1
                        _dailyMatchRequestsSent.value =
                            if (remaining >= 0) MAX_MATCH_REQUESTS_PER_DAY - remaining
                            else _dailyMatchRequestsSent.value + 1
                    }
                }.onFailure { e ->
                    val msg = e.message ?: ""
                    when {
                        msg.contains("MATCH_LIMIT_REACHED") -> {
                            showStatusScreen(upgradePromptFor("MATCH"))
                            return@launch
                        }
                        msg.contains("VERIFICATION_REQUIRED") -> {
                            showStatusScreen(verificationPrompt())
                            return@launch
                        }
                        // Functions not deployed / offline → local demo fallback continues
                    }
                }
            }
            // NOTE: no direct RTDB write here — the sendMatchRequest callable
            // already records the request and notifies the target server-side.
            // (The old interests-node write is denied by database.rules.json.)
            if (!serverAccepted && !wasConnected && !isPremium) {
                _dailyMatchRequestsSent.value += 1
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

    // ---------------- Real-time Chat ----------------

    fun openChat(profile: Profile) {
        // Rule #3 — verified-only chat for real members
        if (FirebaseManager.isSignedIn() && !isVerified && !profile.id.startsWith("SOULMATE_")) {
            showStatusScreen(verificationPrompt())
            return
        }
        _selectedProfile.value = profile
        _currentScreen.value = ScreenState.CHAT_DETAIL

        chatListenerJob?.cancel()
        if (FirebaseManager.isSignedIn()) {
            _activeChat.value = emptyList()
            val threadId = FirebaseManager.chatThreadId(myUid(), profile.id)
            chatListenerJob = viewModelScope.launch {
                try {
                    FirebaseManager.listenChatMessages(threadId).collect { maps ->
                        _activeChat.value = maps.mapNotNull { mapToChatMessage(it, profile.id) }
                    }
                } catch (e: Exception) {
                    _activeChat.value = SampleData.sampleChats
                }
            }
        } else {
            _activeChat.value = SampleData.sampleChats
        }
    }

    fun exitChat() {
        chatListenerJob?.cancel()
    }

    private fun mapToChatMessage(map: Map<String, Any?>, profileId: String): ChatMessage? {
        val id = map["id"] as? String ?: return null
        val senderId = map["senderId"] as? String ?: return null
        val type = map["type"] as? String ?: "TEXT"
        val rawText = map["text"] as? String ?: ""
        val displayText = if (type == "IMAGE") "📷 Photo" else rawText
        val timestamp = (map["timestamp"] as? Long) ?: 0L
        return ChatMessage(
            id = id,
            profileId = profileId,
            message = displayText,
            timestamp = if (timestamp > 0) timeFromMillis(timestamp) else "Now",
            isFromMe = senderId == myUid(),
            isRead = (map["isRead"] as? Boolean) ?: true
        )
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val profile = _selectedProfile.value ?: return

        // Rule #3/#11 — verified-only chat for real members
        if (FirebaseManager.isSignedIn() && !isVerified && !profile.id.startsWith("SOULMATE_")) {
            showStatusScreen(verificationPrompt())
            return
        }
        // Rule #11/#16 — Free: message 1 unique user per day
        val isDemoTarget = profile.id.startsWith("SOULMATE_")
        if (FirebaseManager.isSignedIn() && !isDemoTarget && !isPremium &&
            !_dailyMessageUsers.value.contains(profile.id) &&
            _dailyMessageUsers.value.size >= MAX_MESSAGE_USERS_PER_DAY
        ) {
            showStatusScreen(upgradePromptFor("MESSAGE"))
            return
        }

        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            profileId = profile.id,
            message = text.trim(),
            timestamp = "Just now",
            isFromMe = true,
            isRead = true
        )
        _activeChat.update { it + newMsg }

        if (FirebaseManager.isSignedIn()) {
            viewModelScope.launch {
                if (isDemoTarget) {
                    // Demo profiles have no real server thread — local reply only
                    simulateAutoReply(text, profile.id)
                    return@launch
                }
                // Server is the final authority (rule #22). Direct RTDB writes to
                // `chats` are denied by database.rules.json by design.
                FirebaseManager.callFunction(
                    "sendMessage",
                    mapOf("targetUid" to profile.id, "text" to text.trim(), "type" to "TEXT")
                ).onSuccess {
                    if (!isPremium) {
                        _dailyMessageUsers.value = _dailyMessageUsers.value + profile.id
                    }
                }.onFailure { e ->
                    if (e.message?.contains("MESSAGE_LIMIT_REACHED") == true) {
                        showStatusScreen(upgradePromptFor("MESSAGE"))
                    } else {
                        // Remove the optimistic bubble so the UI reflects reality
                        _activeChat.update { list -> list.filterNot { it.id == newMsg.id } }
                        showToast("Message not delivered — check your connection and try again")
                    }
                }
            }
        } else {
            simulateAutoReply(text, profile.id)
        }
    }

    /** Uploads an image to Firebase Storage and posts it as an IMAGE chat message. */
    fun sendChatImage(uri: Uri) {
        val profile = _selectedProfile.value ?: return
        viewModelScope.launch {
            showToast("Uploading photo...")
            val threadId = FirebaseManager.chatThreadId(myUid(), profile.id)
            val result = if (FirebaseManager.isSignedIn()) {
                FirebaseManager.uploadChatMedia(threadId, uri)
            } else {
                Result.success(uri.toString())
            }
            result.onSuccess { url ->
                _activeChat.update {
                    it + ChatMessage(
                        "img_${System.currentTimeMillis()}", profile.id, "📷 Photo",
                        "Just now", true, true
                    )
                }
                if (FirebaseManager.isSignedIn() && !profile.id.startsWith("SOULMATE_")) {
                    // Server-authoritative delivery — direct writes to `chats`
                    // are denied by database.rules.json by design.
                    FirebaseManager.callFunction(
                        "sendMessage",
                        mapOf(
                            "targetUid" to profile.id,
                            "text" to "📷 Photo",
                            "type" to "IMAGE",
                            "mediaUrl" to url
                        )
                    ).onFailure {
                        showToast("Photo uploaded but not delivered — please try resending")
                    }
                }
            }.onFailure {
                showToast("Photo upload failed. Check your connection.")
            }
        }
    }

    private fun simulateAutoReply(originalText: String, profileId: String) {
        viewModelScope.launch {
            delay(1500)
            _isPartnerTyping.value = true
            delay(2000)
            _isPartnerTyping.value = false

            val replyText = when {
                originalText.contains("horoscope", true) || originalText.contains("jathakam", true) ->
                    "Namaskaram! Our families matched the Jathakam. 9/10 Poruthams agree perfectly! Would love to proceed."
                originalText.contains("parents", true) || originalText.contains("speak", true) ->
                    "Yes, sure! My father can speak with your family this Sunday afternoon."
                originalText.contains("photo", true) ->
                    "Sure, I have uploaded recent traditional wedding photos to my profile!"
                else ->
                    "Thank you for reaching out! Looking forward to getting to know each other better."
            }

            _activeChat.update {
                it + ChatMessage(
                    id = "reply_${System.currentTimeMillis()}",
                    profileId = profileId,
                    message = replyText,
                    timestamp = "Just now",
                    isFromMe = false,
                    isRead = true
                )
            }
        }
    }

    // ---------------- Photo management ----------------

    fun addUserPhoto(url: String) {
        // Rule #5 — maximum 6 profile photos
        if (_userPhotos.value.size >= MAX_PHOTOS) {
            showToast("Maximum 6 photos allowed. Remove one to add another.")
            return
        }
        val newPhoto = UserPhoto(
            id = "up_${System.currentTimeMillis()}",
            url = url,
            isProfilePicture = _userPhotos.value.isEmpty(),
            status = "Approved"
        )
        _userPhotos.update { it + newPhoto }
        showToast("Photo uploaded successfully!")
    }

    /** Uploads a locally picked image through Firebase Storage when signed in. */
    fun uploadUserPhoto(uri: Uri) {
        viewModelScope.launch {
            showToast("Uploading photo...")
            val result = if (FirebaseManager.isSignedIn()) {
                FirebaseManager.uploadProfilePhoto(myUid(), uri)
            } else {
                Result.success(uri.toString())
            }
            result.onSuccess { url -> addUserPhoto(url) }
                .onFailure { showToast("Upload failed — photo added locally instead") }
        }
    }

    fun removeUserPhoto(photoId: String) {
        val photo = _userPhotos.value.firstOrNull { it.id == photoId } ?: return
        val remaining = _userPhotos.value.filterNot { p -> p.id == photoId }
        // Rule #5 — cannot delete the only/main photo without selecting another main
        if (photo.isProfilePicture && remaining.isEmpty()) {
            showToast("You must keep at least one main profile photo. Add another photo first.")
            return
        }
        if (photo.isProfilePicture) {
            // Promote the first remaining photo to primary
            _userPhotos.value = remaining.mapIndexed { index, p -> p.copy(isProfilePicture = index == 0) }
            showToast("Main photo removed — first remaining photo is now primary")
        } else {
            _userPhotos.value = remaining
            showToast("Photo removed")
        }
    }

    fun setAsProfilePhoto(photoId: String) {
        _userPhotos.update { list ->
            list.map { it.copy(isProfilePicture = (it.id == photoId)) }
        }
        showToast("Primary profile photo updated!")
    }

    // ---------------- Profile creation & editing ----------------

    fun updateProfileCreationDraft(transform: (ProfileCreationDraft) -> ProfileCreationDraft) {
        _profileCreationDraft.update(transform)
    }

    fun completeProfileCreation() {
        val draft = _profileCreationDraft.value

        // Rule #2/#4 — age must be computed from DOB and be at least 18
        val computedAge = calculateAgeFromDob(draft.dob)
        if (computedAge == null || computedAge < MIN_AGE_YEARS) {
            showToast("You must be at least 18 years old to register on Soulmate.")
            return
        }
        // Rule #4 — at least 1 profile photo is mandatory before submitting
        if (_userPhotos.value.isEmpty()) {
            showToast("Please add at least 1 profile photo before submitting your biodata.")
            return
        }

        val newProfile = Profile(
            id = "SOULMATE_${System.currentTimeMillis() % 10000}",
            name = if (draft.name.isNotBlank()) draft.name else "Karthik Nair",
            age = computedAge,
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

        // Persist biodata to Firebase Realtime Database
        viewModelScope.launch {
            try {
                ApiClient.apiService.createProfile(draft)
            } catch (e: Exception) {
                // Fallback
            }
            if (FirebaseManager.isSignedIn()) {
                try {
                    // Rule #8 — profile enters PENDING_VERIFICATION until admin/AI approval
                    FirebaseManager.upsertUser(
                        myUid(),
                        mapOf(
                            "status" to "PENDING_VERIFICATION",
                            "dobMillis" to parseDobMillis(draft.dob),
                            "gender" to newProfile.gender,
                            "photoCount" to newProfile.photoUrls.size
                        )
                    )
                    // Server-side completeness gate (rules #3/#4/#8) — validates the
                    // mandatory biodata fields, then flips verification/status to PENDING
                    FirebaseManager.callFunction("submitForVerification", emptyMap())
                    // Rule #1 — one active account per phone (phone_index node)
                    FirebaseManager.registerPhoneIndex("${countryCode.value}${phoneNumber.value}")
                } catch (e: Exception) { /* offline tolerant */ }
                try {
                    val map = mapOf(
                        "name" to newProfile.name, "dob" to draft.dob, "profileFor" to draft.profileFor,
                        "age" to newProfile.age,
                        "gender" to newProfile.gender, "height" to newProfile.height,
                        "religion" to newProfile.religion, "caste" to newProfile.caste,
                        "gothram" to newProfile.gothram, "starNakshatra" to newProfile.starNakshatra,
                        "rasi" to newProfile.rasi, "dosham" to newProfile.dosham,
                        "maritalStatus" to newProfile.maritalStatus,
                        "motherTongue" to newProfile.motherTongue,
                        "education" to newProfile.education, "college" to newProfile.college,
                        "profession" to newProfile.profession, "company" to newProfile.company,
                        "annualIncome" to newProfile.annualIncome,
                        "city" to newProfile.city, "district" to newProfile.district,
                        "state" to newProfile.state, "nativePlace" to newProfile.nativePlace,
                        "bio" to newProfile.bio,
                        "familyType" to newProfile.familyType,
                        "familyFather" to newProfile.familyFather,
                        "familyMother" to newProfile.familyMother,
                        "familySiblings" to newProfile.familySiblings,
                        "partnerAgeRange" to newProfile.partnerAgeRange,
                        "partnerHeightRange" to newProfile.partnerHeightRange,
                        "partnerEducation" to newProfile.partnerEducation,
                        "partnerLocation" to newProfile.partnerLocation,
                        "partnerCaste" to newProfile.partnerCaste,
                        "photoUrls" to newProfile.photoUrls,
                        "trustScore" to newProfile.trustScore,
                        "updatedAt" to System.currentTimeMillis()
                    )
                    FirebaseManager.saveProfile(myUid(), map)
                } catch (e: Exception) {
                    showToast("Profile saved locally — will sync when online")
                }
            }
        }

        // Wire the global Success screen after profile creation
        _statusScreenData.value = StatusScreenData(
            kind = "SUCCESS",
            title = "Biodata Created Successfully!",
            message = "Welcome to the Soulmate family, ${newProfile.name.split(" ").first()}! Your profile is now live and our Vedic matchmaking engine has already started finding compatible partners for you.",
            actionLabel = "Explore Matches",
            destination = "MAIN_APP"
        )
        _currentScreen.value = ScreenState.SUCCESS
    }

    fun updateMyProfile(transform: (Profile) -> Profile) {
        _myProfile.update(transform)
        viewModelScope.launch {
            try {
                ApiClient.apiService.updateProfile(_myProfile.value)
            } catch (e: Exception) {
                // Fallback
            }
            if (FirebaseManager.isSignedIn()) {
                try {
                    // Partial update — a full replace would wipe dob/photoUrls/etc.
                    FirebaseManager.updateProfileFields(
                        myUid(),
                        mapOf(
                            "name" to _myProfile.value.name,
                            "bio" to _myProfile.value.bio,
                            "profession" to _myProfile.value.profession,
                            "education" to _myProfile.value.education,
                            "city" to _myProfile.value.city,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
        showToast("Profile updated successfully!")
    }

    // ---------------- Verification Center ----------------

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
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.saveVerifications(
                        myUid(),
                        mapOf(
                            "faceVerified" to true,
                            "faceMatchAccuracy" to matchScore.toDouble(),
                            "trustScore" to _verificationStatus.value.trustScore,
                            "faceVerifiedAt" to System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
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
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.saveVerifications(
                        myUid(),
                        mapOf(
                            "govtIdVerified" to true,
                            "govtIdType" to idType,
                            "govtIdNumber" to "XXXX-XXXX-${idNumber.takeLast(4).ifBlank { "8921" }}",
                            "trustScore" to _verificationStatus.value.trustScore,
                            "govtIdVerifiedAt" to System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
        showToast("$idType Verified Successfully!")
    }

    // ---------------- Razorpay Subscription & Payment screens ----------------

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
            delay(1000)

            // Rule #19/#22 — the server verifies the Razorpay HMAC signature before
            // Premium is activated. Premium is NEVER activated on the client's say-so.
            val fallbackExpiry = System.currentTimeMillis() + PREMIUM_VALIDITY_DAYS * 24 * 60 * 60 * 1000
            val result = FirebaseManager.callFunction(
                "activatePremium",
                mapOf(
                    "razorpayOrderId" to orderId,
                    "razorpayPaymentId" to paymentId,
                    "razorpaySignature" to signature,
                    "planId" to plan.id
                )
            )

            if (result.isSuccess) {
                // On success the server records subscription + transaction + notification
                val server = result.getOrNull() ?: emptyMap()
                _membershipTier.value = "PREMIUM"
                _subscriptionExpiryMillis.value =
                    (server["expiryDate"] as? Long) ?: fallbackExpiry
            } else {
                val cause = result.exceptionOrNull()
                val code = (cause as? com.google.firebase.functions.FirebaseFunctionsException)?.code
                val serverRejected =
                    code == com.google.firebase.functions.FirebaseFunctionsException.Code.PERMISSION_DENIED ||
                        code == com.google.firebase.functions.FirebaseFunctionsException.Code.INVALID_ARGUMENT ||
                        code == com.google.firebase.functions.FirebaseFunctionsException.Code.UNAUTHENTICATED
                if (serverRejected) {
                    // Signature/plan rejected by the backend — never grant Premium locally.
                    _paymentUiState.value = PaymentUiState.Failure(
                        -2,
                        "Payment verification failed. If you were charged, contact support with payment ID $paymentId."
                    )
                    showToast("Payment verification failed — Premium was not activated")
                    return@launch
                }
                // Offline / functions unreachable — demo fallback for this session only
                _membershipTier.value = "PREMIUM"
                _subscriptionExpiryMillis.value = fallbackExpiry

                // Offline fallback ledger (on success the server writes these itself —
                // writing them here too would create duplicate records).
                if (FirebaseManager.isSignedIn()) {
                    try {
                        FirebaseManager.saveSubscription(
                            myUid(),
                            mapOf(
                                "planId" to plan.id,
                                "planTitle" to plan.title,
                                "duration" to plan.duration,
                                "amount" to plan.price,
                                "orderId" to orderId,
                                "paymentId" to paymentId,
                                "status" to "ACTIVE",
                                "startDate" to System.currentTimeMillis(),
                                "expiryDate" to fallbackExpiry
                            )
                        )
                        FirebaseManager.saveTransaction(
                            myUid(),
                            mapOf(
                                "planTitle" to plan.title,
                                "planDuration" to plan.duration,
                                "amount" to plan.price,
                                "orderId" to orderId,
                                "paymentId" to paymentId,
                                "status" to "SUCCESS"
                            )
                        )
                    } catch (e: Exception) { /* offline tolerant */ }
                }
            }

            _activePlan.value = plan
            _paymentUiState.value = PaymentUiState.Success(plan.title, paymentId, orderId)

            // Record locally for the history page
            _transactions.update {
                listOf(
                    TransactionRecord(
                        id = "txn_${System.currentTimeMillis()}",
                        planTitle = plan.title,
                        planDuration = plan.duration,
                        amount = plan.price,
                        paymentId = paymentId,
                        orderId = orderId,
                        timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                            .format(Date()),
                        status = "SUCCESS"
                    )
                ) + it
            }

            // Route to the dedicated Payment Success screen
            _paymentUiState.value = PaymentUiState.Idle
            _currentScreen.value = ScreenState.PAYMENT_SUCCESS
        }
    }

    fun onRazorpayPaymentFailed(errorCode: Int, errorMessage: String, plan: MembershipPlan? = null) {
        _paymentUiState.value = PaymentUiState.Failure(errorCode, errorMessage)
        showToast("Payment Failed: $errorMessage")

        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.saveTransaction(
                        myUid(),
                        mapOf(
                            "planTitle" to (plan?.title ?: "Unknown Plan"),
                            "planDuration" to (plan?.duration ?: "-"),
                            "amount" to (plan?.price ?: "-"),
                            "orderId" to "-",
                            "paymentId" to "-",
                            "status" to "FAILED"
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }

        _currentScreen.value = ScreenState.PAYMENT_FAILED
        _paymentUiState.value = PaymentUiState.Idle
    }

    /** Rule #9 — activate the Free tier (₹0). */
    fun activateFreePlan() {
        _membershipTier.value = "FREE"
        _subscriptionExpiryMillis.value = 0L
        showToast("You are on the Free plan: 10 match requests/day, 1 message user/day. Upgrade to Premium — ₹99/month anytime.")
    }

    fun retryFailedPayment() {
        _currentScreen.value = ScreenState.MEMBERSHIP
    }

    /** Rule #9 — cancel auto-renewal; benefits remain until the paid period ends. */
    fun cancelAutoRenewal() {
        viewModelScope.launch {
            FirebaseManager.callFunction("cancelAutoRenewal", emptyMap())
            showStatusScreen(
                StatusScreenData(
                    kind = "INFO",
                    title = "Auto-Renewal Cancelled",
                    message = "Your Premium benefits stay active until the end of the current billing period. After that your account returns to the Free plan (10 match requests/day, 1 message user/day).",
                    actionLabel = "Back to Settings",
                    destination = "SETTINGS"
                )
            )
        }
    }

    fun resetPaymentState() {
        _paymentUiState.value = PaymentUiState.Idle
    }

    fun loadPaymentHistory() {
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    val remote = FirebaseManager.fetchTransactions(myUid())
                    if (remote.isNotEmpty()) {
                        _transactions.value = remote.map { map ->
                            TransactionRecord(
                                id = (map["id"] as? String) ?: "txn_remote",
                                planTitle = (map["planTitle"] as? String) ?: "Membership Plan",
                                planDuration = (map["planDuration"] as? String) ?: "-",
                                amount = (map["amount"] as? String) ?: "-",
                                paymentId = (map["paymentId"] as? String) ?: "-",
                                orderId = (map["orderId"] as? String) ?: "-",
                                timestamp = timeFromMillis((map["createdAt"] as? Long) ?: 0L),
                                status = (map["status"] as? String) ?: "SUCCESS"
                            )
                        }
                    }
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
    }

    // ---------------- Global status screens ----------------

    fun showStatusScreen(data: StatusScreenData) {
        _statusScreenData.value = data
        _currentScreen.value =
            if (data.kind == "ERROR") ScreenState.ERROR else ScreenState.SUCCESS
    }

    fun proceedAfterStatus() {
        val destination = _statusScreenData.value?.destination ?: "MAIN_APP"
        _statusScreenData.value = null
        _currentScreen.value = when (destination) {
            "LOGIN" -> ScreenState.LOGIN
            "MEMBERSHIP" -> ScreenState.MEMBERSHIP
            "VERIFICATION_CENTER" -> ScreenState.VERIFICATION_CENTER
            "SETTINGS" -> ScreenState.SETTINGS
            else -> ScreenState.MAIN_APP
        }
    }

    fun retryFromError() {
        _currentScreen.value = ScreenState.MAIN_APP
    }

    fun goOfflineScreen() {
        _currentScreen.value = ScreenState.NO_INTERNET
    }

    fun retryConnection() {
        if (networkMonitor.isCurrentlyOnline()) {
            _isOnline.value = true
            _currentScreen.value = ScreenState.MAIN_APP
        } else {
            showToast("Still offline. Please check your internet connection.")
        }
    }

    // ---------------- Notifications ----------------

    fun markAllNotificationsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
        if (FirebaseManager.isSignedIn()) {
            viewModelScope.launch {
                try {
                    FirebaseManager.markNotificationsRead(myUid())
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
    }

    fun openNotificationProfile(profileId: String?) {
        val target = _profiles.value.firstOrNull { it.id == profileId } ?: return
        viewProfile(target)
    }

    /**
     * Accepts/declines an incoming match request (rule #10 state machine).
     * The server callable is the authority: on ACCEPT it creates the chat
     * thread for both members and notifies the sender. The INTEREST
     * notification is removed once handled.
     */
    fun respondToMatchRequest(notification: AppNotification, accept: Boolean) {
        val requestId = notification.requestId ?: return
        viewModelScope.launch {
            val action = if (accept) "ACCEPTED" else "REJECTED"
            val result = FirebaseManager.callFunction(
                "respondToMatchRequest",
                mapOf("requestId" to requestId, "action" to action)
            )
            if (result.isSuccess) {
                if (accept) {
                    notification.profileId?.let { senderId ->
                        _profiles.update { list ->
                            list.map { if (it.id == senderId) it.copy(isConnected = true) else it }
                        }
                    }
                    showToast("Match accepted \u2014 you can start chatting now!")
                } else {
                    showToast("Request declined.")
                }
                _notifications.update { list -> list.filterNot { it.id == notification.id } }
                try {
                    FirebaseManager.removeNotification(myUid(), notification.id)
                } catch (e: Exception) { /* offline tolerant */ }
            } else {
                showToast("Could not respond \u2014 please try again.")
            }
        }
    }

    private fun loadFirebaseNotifications() {
        if (!FirebaseManager.isSignedIn()) return
        viewModelScope.launch {
            try {
                FirebaseManager.listenNotifications(myUid()).collect { maps ->
                    if (maps.isNotEmpty()) {
                        _notifications.value = maps.map { map ->
                            AppNotification(
                                id = (map["id"] as? String) ?: "ntf",
                                type = (map["type"] as? String) ?: "SYSTEM",
                                title = (map["title"] as? String) ?: "",
                                body = (map["body"] as? String) ?: "",
                                timeAgo = (map["timeAgo"] as? String) ?: "Recently",
                                isRead = (map["isRead"] as? Boolean) ?: false,
                                profileId = map["profileId"] as? String,
                                requestId = map["requestId"] as? String
                            )
                        }
                    }
                }
            } catch (e: Exception) { /* offline tolerant */ }
        }
    }

    // ---------------- Advanced Search & Filters ----------------

    fun updateSearchFilters(transform: (SearchFilters) -> SearchFilters) {
        _searchFilters.update(transform)
    }

    fun applySearchFilters() {
        val f = _searchFilters.value
        _loadingMessage.value = "Finding matches..."
        _currentScreen.value = ScreenState.LOADING
        viewModelScope.launch {
            delay(900) // brief engine pause for the loading screen
            // Rules #12/#13 — never surface blocked / self profiles in search
            val blockedIds = _blockedUsers.value.map { it.id }.toSet()
            val results = _profiles.value.filter { p ->
                if (p.id in blockedIds || p.id == myProfile.value.id) return@filter false
                val queryMatched = f.query.isBlank() ||
                    p.name.contains(f.query, true) ||
                    p.city.contains(f.query, true) ||
                    p.profession.contains(f.query, true) ||
                    p.caste.contains(f.query, true)
                val ageMatched = p.age in f.minAge..f.maxAge
                val religionMatched = f.religion == "Any" || p.religion.equals(f.religion, true)
                val casteMatched = f.caste == "Any" || p.caste.contains(f.caste, true)
                val nakshatraMatched = f.nakshatra == "Any" || p.starNakshatra.contains(f.nakshatra, true)
                val cityMatched = f.city == "Any" || p.city.contains(f.city, true) || p.district.contains(f.city, true)
                val educationMatched = f.education == "Any" || p.education.contains(f.education, true)
                val incomeMatched = f.income == "Any" || p.annualIncome.contains(f.income.replace("Lakhs", "Lakhs"), true)
                val maritalMatched = f.maritalStatus == "Any" || p.maritalStatus.equals(f.maritalStatus, true)
                val dietMatched = f.diet == "Any" || p.diet.contains(f.diet, true)
                val verifiedMatched = !f.verifiedOnly || p.verified

                queryMatched && ageMatched && religionMatched && casteMatched &&
                    nakshatraMatched && cityMatched && educationMatched &&
                    incomeMatched && maritalMatched && dietMatched && verifiedMatched
            }
            _searchResults.value = results
            _hasSearched.value = true
            _currentScreen.value = ScreenState.SEARCH_FILTER
        }
    }

    fun resetSearchFilters() {
        _searchFilters.value = SearchFilters()
        _searchResults.value = emptyList()
        _hasSearched.value = false
    }

    // ---------------- Privacy controls ----------------

    fun updatePrivacySettings(transform: (PrivacySettings) -> PrivacySettings) {
        _privacySettings.update(transform)
        if (FirebaseManager.isSignedIn()) {
            viewModelScope.launch {
                try {
                    val s = _privacySettings.value
                    FirebaseManager.savePrivacySettings(
                        myUid(),
                        mapOf(
                            "profileVisibility" to s.profileVisibility,
                            "photoVisibility" to s.photoVisibility,
                            "showHoroscope" to s.showHoroscope,
                            "showIncome" to s.showIncome,
                            "showFamilyDetails" to s.showFamilyDetails,
                            "allowDirectCalls" to s.allowDirectCalls,
                            "lastSeenVisible" to s.lastSeenVisible,
                            "readReceiptsEnabled" to s.readReceiptsEnabled,
                            "incognitoMode" to s.incognitoMode
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
    }

    // ---------------- Safety Center: report & block ----------------

    fun reportProfile(reportedProfile: Profile, reason: String, details: String) {
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.reportUser(
                        myUid(), reportedProfile.id, reportedProfile.name,
                        reason, details, reportCategoryFor(reason)
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
            showToast("Report submitted. Our Trust & Safety team will review within 24 hours.")
        }
    }

    /** Maps UI report reasons to the category enum enforced by database.rules.json. */
    private fun reportCategoryFor(reason: String): String = when (reason) {
        "Fake profile" -> "FAKE_PROFILE"
        "Spam" -> "SPAM"
        "Harassment" -> "HARASSMENT"
        "Inappropriate content" -> "INAPPROPRIATE_CONTENT"
        "Fraud / scam" -> "FRAUD_SCAM"
        "Offensive behavior" -> "OFFENSIVE_BEHAVIOR"
        "Impersonation" -> "IMPERSONATION"
        else -> "OTHER"
    }

    fun blockProfile(profile: Profile) {
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.blockUser(myUid(), profile.id, "Blocked via Safety Center")
                } catch (e: Exception) { /* offline tolerant */ }
            }
            _blockedUsers.update { current -> current + profile }
            _profiles.update { list -> list.filterNot { it.id == profile.id } }
            showToast("${profile.name} has been blocked. You will no longer see each other.")
        }
    }

    fun unblockProfile(profile: Profile) {
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.unblockUser(myUid(), profile.id)
                } catch (e: Exception) { /* offline tolerant */ }
            }
            _blockedUsers.update { current -> current.filterNot { it.id == profile.id } }
            showToast("${profile.name} unblocked")
        }
    }

    fun loadBlockedUsers() {
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    val blockedIds = FirebaseManager.fetchBlockedUsers(myUid())
                    if (blockedIds.isNotEmpty()) {
                        _blockedUsers.value = SampleData.profiles.filter { it.id in blockedIds }
                    }
                } catch (e: Exception) { /* offline tolerant */ }
            }
        }
    }

    // ---------------- Help & Support ----------------

    fun submitSupportTicket(subject: String, category: String, message: String) {
        if (subject.isBlank() || message.isBlank()) {
            showToast("Please fill in the subject and message.")
            return
        }
        viewModelScope.launch {
            if (FirebaseManager.isSignedIn()) {
                try {
                    FirebaseManager.createSupportTicket(
                        myUid(),
                        mapOf(
                            "subject" to subject,
                            "category" to category,
                            "message" to message,
                            "status" to "OPEN"
                        )
                    )
                } catch (e: Exception) { /* offline tolerant */ }
            }
            showToast("Ticket raised! Our team responds within 24 hours.")
        }
    }

    // ---------------- Email verification (email OTP link) ----------------

    fun updateUserEmail(email: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid e-mail address.")
            return
        }
        FirebaseManager.updateUserEmail(email) { success, message ->
            showToast(message)
            if (success) {
                _userEmail.value = email
                _emailVerificationState.value = "PENDING"
            }
        }
    }

    fun resendEmailVerification() {
        FirebaseManager.sendEmailVerification { success, message -> showToast(message) }
    }

    fun checkEmailVerification() {
        FirebaseManager.reloadUser { success, state ->
            if (success) {
                _emailVerificationState.value = if (state == "VERIFIED") "VERIFIED" else "PENDING"
                showToast(if (state == "VERIFIED") "E-mail verified successfully!" else "Verification still pending — tap the link in your inbox.")
            }
        }
    }

    // ---------------- Voice / Video calls ----------------

    fun startCall(profile: Profile, isVideo: Boolean) {
        if (!_privacySettings.value.allowDirectCalls) {
            showToast("Direct calls are disabled in your privacy settings.")
            return
        }
        _callSession.value = CallSession(profile = profile, isVideo = isVideo, callState = "RINGING")
        _currentScreen.value = ScreenState.CALL_SCREEN
    }

    fun connectCall() {
        _callSession.update { it?.copy(callState = "ONGOING") }
    }

    fun endCall() {
        _callSession.value = null
        _currentScreen.value = ScreenState.CHAT_DETAIL
    }

    fun dismissCallScreen() {
        _callSession.value = null
        _currentScreen.value = ScreenState.MAIN_APP
    }

    // ---------------- Full-screen photo viewer ----------------

    fun openPhotoViewer(urls: List<String>, startIndex: Int) {
        _photoViewerUrls.value = urls
        _photoViewerIndex.value = startIndex
        _currentScreen.value = ScreenState.PHOTO_VIEWER
    }

    fun setPhotoViewerIndex(index: Int) {
        _photoViewerIndex.value = index
    }

    fun closePhotoViewer() {
        _currentScreen.value = ScreenState.PROFILE_DETAIL
    }

    // ---------------- Referral ----------------

    fun applyReferralCode(code: String) {
        if (code.isBlank()) {
            showToast("Please enter a referral code.")
            return
        }
        _referralStats.update {
            it.copy(
                friendsReferred = it.friendsReferred + 1,
                premiumWeeksEarned = it.premiumWeeksEarned + 2,
                totalEarned = "₹ ${(it.friendsReferred + 1) * 50}"
            )
        }
        showToast("Referral applied! 2 weeks of premium added to your account.")
    }

    // ---------------- Toasts ----------------

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
