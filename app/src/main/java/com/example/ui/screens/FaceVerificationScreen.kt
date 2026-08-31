package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DeepBurgundy
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGold
import com.example.ui.theme.LightGreen
import com.example.ui.theme.LightRose
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmBackground
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceVerificationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val myProfile by viewModel.myProfile.collectAsState()
    val verification by viewModel.verificationStatus.collectAsState()

    var isScanning by remember { mutableStateOf(false) }
    var currentGestureStep by remember { mutableIntStateOf(1) } // 1: Center Face, 2: Blink, 3: Smile, 4: Verified
    var scanProgress by remember { mutableFloatStateOf(0f) }
    var isVerificationComplete by remember { mutableStateOf(false) }

    // Scanner animation loop
    val infiniteTransition = rememberInfiniteTransition(label = "RadarScanner")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserAnimation"
    )

    // Automated scanning progression simulation
    LaunchedEffect(isScanning) {
        if (isScanning) {
            currentGestureStep = 1
            scanProgress = 0.2f
            delay(1200)
            currentGestureStep = 2
            scanProgress = 0.55f
            delay(1200)
            currentGestureStep = 3
            scanProgress = 0.88f
            delay(1200)
            scanProgress = 1.0f
            currentGestureStep = 4
            isVerificationComplete = true
            isScanning = false
            viewModel.performFaceVerification(matchScore = 99.4f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Face Verification",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "Biometric liveness & photo match",
                            fontSize = 11.sp,
                            color = LightGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) },
                        modifier = Modifier.testTag("face_verify_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PureWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBurgundy)
            )
        },
        containerColor = Color(0xFF0F0B13),
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Instructions / Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isVerificationComplete) "Face Match Verified 100%! 🎉" else "Selfie Liveness Verification",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVerificationComplete) SuccessGreen else PureWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isVerificationComplete)
                        "Your biometric face matches your uploaded profile photo with 99.4% accuracy."
                    else
                        "Position your face inside the frame. Follow the on-screen gestures to earn the Blue Trust Shield.",
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }

            // Camera Viewfinder & Face Oval Frame
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1626)),
                contentAlignment = Alignment.Center
            ) {
                // Background simulated camera feed
                AsyncImage(
                    model = myProfile.photoUrls.firstOrNull() ?: "",
                    contentDescription = "User Face Camera Stream",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Oval Scanner Overlay
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val ovalWidth = size.width * 0.78f
                    val ovalHeight = size.height * 0.90f
                    val left = (size.width - ovalWidth) / 2f
                    val top = (size.height - ovalHeight) / 2f

                    // Target Oval Guide
                    drawOval(
                        color = when {
                            isVerificationComplete -> Color(0xFF4CAF50)
                            isScanning -> Color(0xFFFFD700)
                            else -> Color.White.copy(alpha = 0.7f)
                        },
                        topLeft = Offset(left, top),
                        size = Size(ovalWidth, ovalHeight),
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )

                    // Scanning Laser Line
                    if (isScanning) {
                        val laserY = top + (ovalHeight * laserPosition)
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, Color(0xFFFFD700), Color(0xFF4CAF50), Color.Transparent)
                            ),
                            start = Offset(left, laserY),
                            end = Offset(left + ovalWidth, laserY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Center Status Badge or Checkmark
                if (isVerificationComplete) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified",
                            tint = PureWhite,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }
            }

            // Gesture & Progress Indicator Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1626)),
                border = BorderStroke(1.dp, if (isVerificationComplete) SuccessGreen else Color(0xFF3B2E4A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isVerificationComplete) Icons.Default.Verified else Icons.Default.Face,
                                contentDescription = null,
                                tint = if (isVerificationComplete) SuccessGreen else GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    isVerificationComplete -> "Biometric Match: 99.4%"
                                    isScanning -> "Step $currentGestureStep of 3: AI Analyzing..."
                                    else -> "Ready for Scan"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }

                        if (isScanning) {
                            Text(
                                text = "${(scanProgress * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { scanProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isVerificationComplete) SuccessGreen else GoldAccent,
                        trackColor = Color(0xFF3B2E4A),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3 Gesture Checklist items
                    GestureStepRow(
                        stepNumber = 1,
                        text = "Center face in oval frame",
                        isPassed = currentGestureStep > 1 || isVerificationComplete
                    )
                    GestureStepRow(
                        stepNumber = 2,
                        text = "Blink your eyes twice",
                        isPassed = currentGestureStep > 2 || isVerificationComplete
                    )
                    GestureStepRow(
                        stepNumber = 3,
                        text = "Smile gently at camera",
                        isPassed = currentGestureStep > 3 || isVerificationComplete
                    )
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!isVerificationComplete) {
                    Button(
                        onClick = { isScanning = true },
                        enabled = !isScanning,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepBurgundy,
                            contentColor = PureWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_face_scan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isScanning) "Scanning Biometrics..." else "Start Face Verification 🤳",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen,
                            contentColor = PureWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("done_face_scan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Claim Verified Trust Badge 🛡️",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "256-bit Encrypted. Biometrics are never shared publicly.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun GestureStepRow(
    stepNumber: Int,
    text: String,
    isPassed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (isPassed) SuccessGreen else Color(0xFF3B2E4A)),
            contentAlignment = Alignment.Center
        ) {
            if (isPassed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = PureWhite,
                    modifier = Modifier.size(12.dp)
                )
            } else {
                Text(
                    text = "$stepNumber",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isPassed) PureWhite else Color.LightGray,
            fontWeight = if (isPassed) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
