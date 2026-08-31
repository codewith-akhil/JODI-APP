package com.example.network

import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.Profile
import com.example.model.ProfileCreationDraft
import com.example.model.VerificationStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// --- Auth DTOs ---
@JsonClass(generateAdapter = true)
data class SendOtpRequest(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "country_code") val countryCode: String = "+91"
)

@JsonClass(generateAdapter = true)
data class SendOtpResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "session_id") val sessionId: String? = null,
    @Json(name = "is_new_user") val isNewUser: Boolean = false
)

@JsonClass(generateAdapter = true)
data class VerifyOtpRequest(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "otp_code") val otpCode: String,
    @Json(name = "session_id") val sessionId: String? = null
)

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "user_id") val userId: String,
    @Json(name = "is_first_time_user") val isFirstTimeUser: Boolean,
    @Json(name = "is_profile_completed") val isProfileCompleted: Boolean
)

// --- Matchmaking & Recommendation DTOs ---
@JsonClass(generateAdapter = true)
data class CompatibilityResponse(
    @Json(name = "target_profile_id") val targetProfileId: String,
    @Json(name = "total_match_score") val totalMatchScore: Int,
    @Json(name = "guna_milan_score") val gunaMilanScore: Int, // Max 36
    @Json(name = "porutham_count") val poruthamCount: Int, // Max 10
    @Json(name = "nakshatra_compatibility") val nakshatraCompatibility: String,
    @Json(name = "rasi_compatibility") val rasiCompatibility: String,
    @Json(name = "dosham_status") val doshamStatus: String,
    @Json(name = "career_compatibility_score") val careerCompatibilityScore: Int,
    @Json(name = "lifestyle_compatibility_score") val lifestyleCompatibilityScore: Int,
    @Json(name = "summary") val summary: String
)

@JsonClass(generateAdapter = true)
data class InterestRequest(
    @Json(name = "target_profile_id") val targetProfileId: String,
    @Json(name = "personalized_note") val personalizedNote: String? = null
)

@JsonClass(generateAdapter = true)
data class InterestResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "status") val status: String, // "SENT", "ACCEPTED", "MUTUAL_MATCH"
    @Json(name = "is_mutual_match") val isMutualMatch: Boolean = false,
    @Json(name = "message") val message: String
)

// --- Chat DTOs ---
@JsonClass(generateAdapter = true)
data class SendMessagePayload(
    @Json(name = "recipient_id") val recipientId: String,
    @Json(name = "message") val message: String,
    @Json(name = "message_type") val messageType: String = "TEXT", // "TEXT", "IMAGE", "AUDIO"
    @Json(name = "media_url") val mediaUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class ChatMessageDto(
    @Json(name = "id") val id: String,
    @Json(name = "sender_id") val senderId: String,
    @Json(name = "recipient_id") val recipientId: String,
    @Json(name = "message") val message: String,
    @Json(name = "timestamp") val timestamp: String,
    @Json(name = "is_read") val isRead: Boolean,
    @Json(name = "status") val status: String // "SENT", "DELIVERED", "READ"
)

// --- Razorpay Payment DTOs ---
@JsonClass(generateAdapter = true)
data class CreateRazorpayOrderRequest(
    @Json(name = "plan_id") val planId: String,
    @Json(name = "amount_in_paise") val amountInPaise: Long,
    @Json(name = "currency") val currency: String = "INR",
    @Json(name = "user_id") val userId: String
)

@JsonClass(generateAdapter = true)
data class RazorpayOrderResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "order_id") val orderId: String,
    @Json(name = "amount") val amount: Long,
    @Json(name = "currency") val currency: String,
    @Json(name = "key_id") val keyId: String,
    @Json(name = "user_name") val userName: String,
    @Json(name = "user_email") val userEmail: String,
    @Json(name = "user_contact") val userContact: String
)

@JsonClass(generateAdapter = true)
data class VerifyPaymentRequest(
    @Json(name = "razorpay_order_id") val razorpayOrderId: String,
    @Json(name = "razorpay_payment_id") val razorpayPaymentId: String,
    @Json(name = "razorpay_signature") val razorpaySignature: String,
    @Json(name = "plan_id") val planId: String
)

@JsonClass(generateAdapter = true)
data class PaymentVerificationResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "subscription_id") val subscriptionId: String,
    @Json(name = "active_plan_title") val activePlanTitle: String,
    @Json(name = "expiry_date") val expiryDate: String
)

// --- Retrofit Service Interfaces ---
interface MatrimonyApiService {

    // Auth
    @POST("api/v1/auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>

    @POST("api/v1/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<AuthTokenResponse>

    // Profiles & Biodata
    @GET("api/v1/profiles/me")
    suspend fun getMyProfile(): Response<Profile>

    @POST("api/v1/profiles/create")
    suspend fun createProfile(@Body draft: ProfileCreationDraft): Response<Profile>

    @PUT("api/v1/profiles/update")
    suspend fun updateProfile(@Body profile: Profile): Response<Profile>

    @GET("api/v1/profiles/discovery")
    suspend fun getDiscoveryProfiles(
        @Query("filter") filter: String? = null,
        @Query("religion") religion: String? = null,
        @Query("caste") caste: String? = null,
        @Query("min_age") minAge: Int? = null,
        @Query("max_age") maxAge: Int? = null
    ): Response<List<Profile>>

    @GET("api/v1/profiles/{id}")
    suspend fun getProfileById(@Path("id") id: String): Response<Profile>

    // Matchmaking & Horoscope
    @GET("api/v1/matches/compatibility/{targetId}")
    suspend fun getCompatibilityReport(@Path("targetId") targetId: String): Response<CompatibilityResponse>

    @POST("api/v1/matches/interest/send")
    suspend fun sendInterest(@Body request: InterestRequest): Response<InterestResponse>

    @POST("api/v1/matches/shortlist/{id}")
    suspend fun toggleShortlist(@Path("id") profileId: String): Response<Map<String, Boolean>>

    // Real-Time Chat
    @GET("api/v1/chat/threads")
    suspend fun getChatThreads(): Response<List<ChatThread>>

    @GET("api/v1/chat/threads/{threadId}/messages")
    suspend fun getMessages(@Path("threadId") threadId: String): Response<List<ChatMessageDto>>

    @POST("api/v1/chat/messages/send")
    suspend fun sendMessage(@Body payload: SendMessagePayload): Response<ChatMessageDto>

    // Razorpay Subscription
    @POST("api/v1/payments/razorpay/create-order")
    suspend fun createRazorpayOrder(@Body request: CreateRazorpayOrderRequest): Response<RazorpayOrderResponse>

    @POST("api/v1/payments/razorpay/verify")
    suspend fun verifyPayment(@Body request: VerifyPaymentRequest): Response<PaymentVerificationResponse>

    // Verification
    @POST("api/v1/verification/govt-id")
    suspend fun submitGovtId(
        @Query("id_type") idType: String,
        @Query("id_number") idNumber: String
    ): Response<VerificationStatus>

    @POST("api/v1/verification/face-biometrics")
    suspend fun submitFaceBiometric(
        @Query("accuracy") accuracy: Float
    ): Response<VerificationStatus>
}
