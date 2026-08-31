package com.example.payment

import android.content.Context
import com.example.model.MembershipPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    data class InitiatingOrder(val plan: MembershipPlan) : PaymentUiState()
    data class ReadyForCheckout(
        val orderId: String,
        val amountInPaise: Long,
        val plan: MembershipPlan,
        val razorpayOptions: JSONObject
    ) : PaymentUiState()
    data class VerifyingPayment(val paymentId: String, val orderId: String) : PaymentUiState()
    data class Success(val planTitle: String, val paymentId: String, val orderId: String) : PaymentUiState()
    data class Failure(val errorCode: Int, val message: String) : PaymentUiState()
}

object RazorpayPaymentService {

    // Razorpay Key ID (Test key provided as standard default, override in .env or Secrets Panel)
    const val RAZORPAY_TEST_KEY_ID = "rzp_test_SoulmateMatrimony2026"

    /**
     * Builds Razorpay Checkout Options JSON for native checkout or web intent
     */
    fun createCheckoutPayload(
        orderId: String,
        plan: MembershipPlan,
        userPhone: String,
        userName: String,
        userEmail: String = "user@soulmatematrimony.com",
        keyId: String = RAZORPAY_TEST_KEY_ID
    ): JSONObject {
        // Price string parsing (e.g. "₹ 199" -> 19900 paise)
        val numericPrice = plan.price.replace("[^0-9]".toRegex(), "").toLongOrNull() ?: 199L
        val amountInPaise = numericPrice * 100

        return JSONObject().apply {
            put("name", "Soulmate Matrimony")
            put("description", "${plan.title} - ${plan.duration} Subscription")
            put("image", "https://api.soulmatematrimony.com/assets/logo_gold.png")
            put("currency", "INR")
            put("amount", amountInPaise)
            put("order_id", orderId)
            put("key", keyId)

            val prefill = JSONObject().apply {
                put("email", userEmail)
                put("contact", if (userPhone.isNotBlank()) userPhone else "+919876543210")
                put("name", userName)
            }
            put("prefill", prefill)

            val theme = JSONObject().apply {
                put("color", "#0284C7") // Soulmate Brand Primary Blue
                put("backdrop_color", "#1C1B1F")
            }
            put("theme", theme)

            val retry = JSONObject().apply {
                put("enabled", true)
                put("max_count", 3)
            }
            put("retry", retry)

            val notes = JSONObject().apply {
                put("plan_id", plan.id)
                put("plan_title", plan.title)
                put("platform", "Android Native")
            }
            put("notes", notes)
        }
    }
}
