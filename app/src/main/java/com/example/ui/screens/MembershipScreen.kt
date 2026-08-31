package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MembershipPlan
import com.example.payment.PaymentUiState
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val plans by viewModel.membershipPlans.collectAsState()
    val activePlan by viewModel.activePlan.collectAsState()
    val paymentUiState by viewModel.paymentUiState.collectAsState()
    var selectedPlanForPayment by remember { mutableStateOf<MembershipPlan?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Google Pay UPI") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
    ) {
        // Executive Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepBurgundy, CrimsonRed)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Premium",
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SOULMATE MEMBERSHIP",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldAccent,
                                letterSpacing = 1.5.sp
                            )
                        }
                        Text(
                            text = "Find Your Life Partner 3x Faster",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x33FFFFFF)
                    ) {
                        Text(
                            text = "Special Offer",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightGold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Unlock verified phone numbers, direct WhatsApp connectivity, Vedic horoscope porutham reports and priority profile placement.",
                    fontSize = 12.sp,
                    color = PureWhite.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )
            }
        }

        // Plans List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (activePlan != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = LightGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = SuccessGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Active Subscription: ${activePlan!!.title}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = "Valid for ${activePlan!!.duration}. Enjoy unlimited contacts and verified matchmaking.",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            items(plans) { plan ->
                PlanCard(
                    plan = plan,
                    isCurrentActive = activePlan?.id == plan.id,
                    onSelect = {
                        selectedPlanForPayment = plan
                        viewModel.initiateRazorpayCheckout(plan)
                    }
                )
            }

            // Trust & Razorpay Guarantee
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Safe",
                            tint = SuccessGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "100% Safe & Secure via Razorpay",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Instant plan activation with UPI, Debit/Credit cards and NetBanking with 256-bit bank-grade encryption.",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Account & history links
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(ScreenState.PAYMENT_HISTORY) },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Transactions",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                    if (activePlan != null) {
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(ScreenState.SUBSCRIBED) },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = DarkGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "My Benefits",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGold
                            )
                        }
                    }
                }
            }
        }
    }

    // Razorpay Checkout Bottom Sheet
    if (selectedPlanForPayment != null) {
        val plan = selectedPlanForPayment!!
        ModalBottomSheet(
            onDismissRequest = {
                selectedPlanForPayment = null
                viewModel.resetPaymentState()
            },
            sheetState = rememberModalBottomSheetState(),
            containerColor = PureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                // Razorpay Brand Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(PrimaryBlue, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("R", color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Razorpay Trusted Gateway", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("256-Bit SSL Secured", fontSize = 10.sp, color = SuccessGreen)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightGold
                    ) {
                        Text(
                            text = plan.price,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepBurgundy,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = DividerColor)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Order Summary: ${plan.title} (${plan.duration})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Options
                val paymentMethods = listOf(
                    Triple("Google Pay UPI", "Instant One-Tap UPI Checkout", Icons.Default.QrCode),
                    Triple("PhonePe / Paytm UPI", "UPI App, ID & QR Code", Icons.Default.QrCode),
                    Triple("Credit / Debit Card", "Visa, MasterCard, RuPay", Icons.Default.CreditCard),
                    Triple("NetBanking", "SBI, HDFC, Federal Bank, ICICI", Icons.Default.AccountBalance)
                )

                paymentMethods.forEach { (name, sub, icon) ->
                    val isSelected = selectedPaymentMethod == name
                    Surface(
                        onClick = { selectedPaymentMethod = name },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) LightRose else WarmBackground,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DeepBurgundy else BorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) DeepBurgundy else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) DeepBurgundy else TextPrimary
                                    )
                                    Text(
                                        text = sub,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = DeepBurgundy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                when (paymentUiState) {
                    is PaymentUiState.InitiatingOrder, is PaymentUiState.VerifyingPayment -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = DeepBurgundy, strokeWidth = 2.5.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (paymentUiState is PaymentUiState.InitiatingOrder) "Connecting to Razorpay..." else "Verifying Payment Signature...",
                                    fontSize = 13.sp,
                                    color = DeepBurgundy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    else -> {
                        // Use the orderId created in initiateRazorpayCheckout (PaymentUiState.ReadyForCheckout)
                        // so the receipt matches the order that was actually initiated.
                        val checkout = paymentUiState as? PaymentUiState.ReadyForCheckout
                        Button(
                            onClick = {
                                if (plan.id == "plan_free") {
                                    // Rule #9 — the Free plan requires no payment
                                    selectedPlanForPayment = null
                                    viewModel.activateFreePlan()
                                    return@Button
                                }
                                val generatedPaymentId = "pay_rzp_${System.currentTimeMillis() % 100000}"
                                val signature = "sig_${System.currentTimeMillis()}"
                                viewModel.onRazorpayPaymentSuccess(
                                    paymentId = generatedPaymentId,
                                    orderId = checkout?.orderId
                                        ?: "order_rzp_${System.currentTimeMillis() % 100000}",
                                    signature = signature,
                                    plan = plan
                                )
                                selectedPlanForPayment = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepBurgundy,
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("razorpay_pay_now_button")
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (plan.id == "plan_free") "Continue with Free Plan — ₹0/month" else "Pay ${plan.price} via Razorpay",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = {
                                // User-cancelled checkout is NOT a payment failure:
                                // no FAILED receipt is recorded, we simply close the sheet.
                                selectedPlanForPayment = null
                                viewModel.resetPaymentState()
                                viewModel.showToast("Payment cancelled — no amount was charged.")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Cancel Payment",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    plan: MembershipPlan,
    isCurrentActive: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("plan_card_${plan.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (plan.isPopular) 4.dp else 1.dp),
        border = if (plan.isPopular) androidx.compose.foundation.BorderStroke(2.dp, GoldAccent) else androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Column {
            // Ribbon Banner for Popular
            if (plan.isPopular) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GoldAccent)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RECOMMENDED SUBSCRIPTION PLAN (67% OFF)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkCardSurface,
                        letterSpacing = 1.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = plan.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Duration: ${plan.duration}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = plan.price,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepBurgundy
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = plan.originalPrice,
                                fontSize = 12.sp,
                                color = TextMuted,
                                textDecoration = TextDecoration.LineThrough
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LightGreen
                            ) {
                                Text(
                                    text = plan.discountPercent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DividerColor)
                Spacer(modifier = Modifier.height(14.dp))

                // Feature Checklist
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Feature",
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feature,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (plan.isPopular) PrimaryEmerald else PrimaryBlue,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(
                        text = if (isCurrentActive) "Active Plan" else "Select & Upgrade",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
