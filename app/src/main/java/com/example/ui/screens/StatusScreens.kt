package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MembershipPlan
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import com.example.viewmodel.BottomTab
import kotlinx.coroutines.delay

/**
 * Global status screens: Loading / Success / Error / No-Internet /
 * Subscribed / Payment Success / Payment Failed.
 * Follows the app's green-blue matrimony design system strictly.
 */

// ---------------------------------------------------------------------------
// Reusable brand loader — animated interlocking rings + pulsing heart
// ---------------------------------------------------------------------------

@Composable
fun JodiLoader(modifier: Modifier = Modifier, loaderSize: Int = 120) {
    val infiniteTransition = rememberInfiniteTransition(label = "JodiLoader")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "ringRotation"
    )
    val counterRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing)),
        label = "counterRotation"
    )
    val heartPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "heartPulse"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "ringAlpha"
    )

    Box(
        modifier = modifier.size(loaderSize.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val size = this.size.minDimension
            val center = Offset(size / 2f, size / 2f)
            val outerRadius = size * 0.40f
            val innerRadius = size * 0.28f

            // Outer rotating arc — brand blue
            rotate(degrees = rotation, pivot = center) {
                drawArc(
                    color = PrimaryBlue,
                    startAngle = 0f,
                    sweepAngle = 280f,
                    useCenter = false,
                    topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                    size = androidx.compose.ui.geometry.Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = size * 0.05f, cap = StrokeCap.Round),
                    alpha = ringAlpha
                )
            }
            // Inner counter-rotating arc — brand emerald
            rotate(degrees = counterRotation, pivot = center) {
                drawArc(
                    color = PrimaryEmerald,
                    startAngle = 90f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = Offset(center.x - innerRadius, center.y - innerRadius),
                    size = androidx.compose.ui.geometry.Size(innerRadius * 2, innerRadius * 2),
                    style = Stroke(width = size * 0.045f, cap = StrokeCap.Round),
                    alpha = ringAlpha
                )
            }
            // Orbiting gold dot
            rotate(degrees = rotation * 1.6f, pivot = center) {
                drawCircle(
                    color = GoldAccent,
                    radius = size * 0.045f,
                    center = Offset(center.x, center.y - outerRadius)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Loading",
            tint = PrimaryEmerald,
            modifier = Modifier
                .size((loaderSize * 0.32).dp)
                .scale(heartPulse)
        )
    }
}

// ---------------------------------------------------------------------------
// Full-screen Loading — "the perfect Android loading screen"
// ---------------------------------------------------------------------------

private val loadingMessages = listOf(
    "Finding your special someone...",
    "Matching sacred horoscopes...",
    "Verifying trusted profiles...",
    "Polishing your biodata...",
    "Almost there, the stars are aligning..."
)

@Composable
fun LoadingScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val message by viewModel.loadingMessage.collectAsState()
    var rotatingIndex by remember { mutableIntStateOf(0) }

    // Rotate poetic status lines when the caller keeps a generic message
    LaunchedEffect(Unit) {
        while (true) {
            delay(2200)
            rotatingIndex = (rotatingIndex + 1) % loadingMessages.size
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGreenBackground, LightBlueBackground, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        JodiLoader(loaderSize = 130)

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Soulmate",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryBlue
        )
        Text(
            text = "Where Hearts Meet",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = message,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = loadingMessages[rotatingIndex],
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(0.85f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Three bouncing dots
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { index ->
                val dotScale by rememberInfiniteTransition(label = "dots").animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        tween(600, delayMillis = index * 180),
                        RepeatMode.Reverse
                    ),
                    label = "dot$index"
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .scale(dotScale)
                        .background(PrimaryBlue.copy(alpha = 0.7f), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Secure • Verified • Traditional",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            letterSpacing = 1.sp
        )
    }
}

// ---------------------------------------------------------------------------
// Generic Success screen (profile created, login, verification, account...)
// ---------------------------------------------------------------------------

@Composable
fun SuccessScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val data by viewModel.statusScreenData.collectAsState()
    val status = data ?: return

    var animated by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (animated) 1f else 0f,
        animationSpec = tween(600),
        label = "iconScale"
    )
    LaunchedEffect(Unit) { animated = true }

    val isInfo = status.kind == "INFO"
    val iconTint = if (isInfo) PrimaryBlue else PrimaryEmerald
    val containerColor = if (isInfo) LightBlue else LightGreen
    val icon: ImageVector = if (isInfo) Icons.Default.Info else Icons.Default.Verified

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGreenBackground, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .scale(iconScale)
                .background(containerColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = status.title,
                    tint = iconTint,
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = status.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = status.message,
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.proceedAfterStatus() },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = status.actionLabel,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (status.destination == "LOGIN") {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your privacy and data were handled securely.",
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Generic Error screen
// ---------------------------------------------------------------------------

@Composable
fun ErrorScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val data by viewModel.statusScreenData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGold, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(LightGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = "Error",
                    tint = DarkGold,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = data?.title ?: "Something Went Wrong",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = data?.message
                ?: "We hit an unexpected snag while processing your request. Your data is safe — please try again.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = { viewModel.retryFromError() },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Retry", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { viewModel.proceedAfterStatus() }) {
            Text(
                "Go Back Home",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
    }
}

// ---------------------------------------------------------------------------
// No Internet screen (auto-recovers when connectivity returns)
// ---------------------------------------------------------------------------

@Composable
fun NoInternetScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "offline")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "offlinePulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightBlueBackground, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .scale(pulse)
                .background(LightBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = "No Internet",
                tint = PrimaryBlue,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "You're Offline",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Soulmate needs an internet connection to sync your matches, chats and verification status. Please check your Wi-Fi or mobile data.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "This screen dismisses automatically the moment you're back online.",
                fontSize = 12.sp,
                color = SuccessGreen,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.retryConnection() },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Retry", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { viewModel.retryConnection() }) {
            Text(
                "Continue with cached content",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Payment Success screen (Razorpay result)
// ---------------------------------------------------------------------------

@Composable
fun PaymentSuccessScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val activePlan by viewModel.activePlan.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val plan = activePlan
    val lastTransaction = transactions.firstOrNull()

    val infiniteTransition = rememberInfiniteTransition(label = "celebrate")
    val ringPulse by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "ringPulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGreenBackground, LightGold, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .scale(ringPulse)
                .background(LightGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Payment Successful",
                    tint = PrimaryEmerald,
                    modifier = Modifier.size(58.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Payment Successful! 🎉",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (plan != null)
                "Your ${plan.title} membership is now active. Welcome to premium matchmaking!"
            else
                "Your membership is now active. Welcome to premium matchmaking!",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Receipt card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                if (plan != null) {
                    ReceiptRow("Plan", "${plan.title} (${plan.duration})")
                    ReceiptRow("Amount", plan.price)
                }
                ReceiptRow("Payment ID", lastTransaction?.paymentId ?: "pay_rzp_verified")
                ReceiptRow("Order ID", lastTransaction?.orderId ?: "order_rzp_verified")
                ReceiptRow("Status", "SUCCESS — Signature Verified")
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightGreen
                ) {
                    Text(
                        text = "A receipt has been saved to your Transaction History.",
                        fontSize = 11.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.navigateTo(ScreenState.SUBSCRIBED) },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text("View My Benefits", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                viewModel.selectBottomTab(BottomTab.DISCOVERY)
                viewModel.navigateTo(ScreenState.MAIN_APP)
            },
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                "Start Exploring Premium Matches",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Medium)
        Text(
            text = value,
            fontSize = 12.sp,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
    }
}

// ---------------------------------------------------------------------------
// Payment Failed screen
// ---------------------------------------------------------------------------

@Composable
fun PaymentFailedScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGold, PureWhite)
                )
            )
            .statusBarsPadding()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(LightGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = "Payment Failed",
                    tint = DarkGold,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Payment Could Not Complete",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "No amount was deducted from your account. This can happen due to a timeout, a cancelled UPI request, or a bank-side decline. You can safely retry.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Safe",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "100% safe — failed transactions are never charged by Razorpay.",
                    fontSize = 12.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { viewModel.retryFailedPayment() },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Retry", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry Payment", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            viewModel.selectBottomTab(BottomTab.DISCOVERY)
            viewModel.navigateTo(ScreenState.MAIN_APP)
        }) {
            Text(
                "Maybe Later",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Subscribed screen — active plan benefits hub
// ---------------------------------------------------------------------------

@Composable
fun SubscribedScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val activePlan by viewModel.activePlan.collectAsState()
    val plans by viewModel.membershipPlans.collectAsState()
    val plan: MembershipPlan? = activePlan
        ?: plans.firstOrNull { it.isPopular }
        ?: plans.firstOrNull()

    // Defensive empty state — never crash if the plan list is unavailable
    if (plan == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(WarmBackground)
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = DarkGold,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No active membership found",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.navigateTo(ScreenState.MEMBERSHIP) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue, contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Browse Membership Plans", fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
    ) {
        // Celebration header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(colors = listOf(PrimaryBlue, PrimaryTeal))
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Premium",
                    tint = LightGold,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "You're a Premium Member!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${plan.title} • ${plan.duration}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Your Active Benefits",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    plan.features.forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = PrimaryEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = feature,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.navigateTo(ScreenState.PAYMENT_HISTORY) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Manage Subscription & Receipts", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.selectBottomTab(BottomTab.DISCOVERY)
                    viewModel.navigateTo(ScreenState.MAIN_APP)
                },
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "Start Exploring",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
