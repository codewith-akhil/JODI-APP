package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DeepBurgundy
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGold
import com.example.ui.theme.LightGreen
import com.example.ui.theme.LightRose
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.RoseSoft
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmBackground
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val verification by viewModel.verificationStatus.collectAsState()
    val myProfile by viewModel.myProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Trust & Verification Center",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "Build authentic trust with potential matches",
                            fontSize = 11.sp,
                            color = LightGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) },
                        modifier = Modifier.testTag("verification_back_button")
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
        containerColor = WarmBackground,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Trust Meter Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DeepBurgundy
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(PureWhite.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { verification.trustScore / 100f },
                                modifier = Modifier.size(90.dp),
                                color = GoldAccent,
                                trackColor = PureWhite.copy(alpha = 0.2f),
                                strokeWidth = 8.dp
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${verification.trustScore}%",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PureWhite
                                )
                                Text(
                                    text = "TRUST",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (verification.trustScore >= 95) "100% Verified Soulmate Elite" else "Silver Verified Member",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (verification.trustScore >= 95)
                                "Your profile is fully verified with Aadhaar and Biometric Face Match. You appear at the top of discovery feeds."
                            else
                                "Complete Face and Govt ID verification to unlock the Royal Gold Trust Badge and 3x more interests.",
                            fontSize = 12.sp,
                            color = PureWhite.copy(alpha = 0.85f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Verification Badges Grid / List
            item {
                Text(
                    text = "Verification Checkpoints",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // 1. Face Verification
            item {
                VerificationTierCard(
                    icon = Icons.Default.Face,
                    title = "AI Face Biometric Verification",
                    subtitle = if (verification.isFaceVerified)
                        "Verified with 99.4% selfie biometric match ✅"
                    else
                        "Take a 5-second selfie to verify identity & prevent fake profiles.",
                    isVerified = verification.isFaceVerified,
                    actionText = if (verification.isFaceVerified) "Re-Scan Face" else "Scan Face 🤳",
                    onAction = { viewModel.navigateTo(ScreenState.FACE_VERIFICATION) },
                    testTag = "goto_face_verification"
                )
            }

            // 2. Govt ID Verification
            item {
                VerificationTierCard(
                    icon = Icons.Default.Fingerprint,
                    title = "Government ID / Aadhaar via DigiLocker",
                    subtitle = if (verification.isGovtIdVerified)
                        "Government ID verified securely (${verification.govtIdType}) ✅"
                    else
                        "Verify via Aadhaar, Passport, or Driving License for the Green Trust Seal.",
                    isVerified = verification.isGovtIdVerified,
                    actionText = if (verification.isGovtIdVerified) "View Document" else "Verify ID 🪪",
                    onAction = { viewModel.navigateTo(ScreenState.GOVT_ID_VERIFICATION) },
                    testTag = "goto_govt_id_verification"
                )
            }

            // 3. Mobile Number Verification
            item {
                VerificationTierCard(
                    icon = Icons.Default.PhoneAndroid,
                    title = "Mobile & WhatsApp Number",
                    subtitle = "Verified via secure SMS OTP (+91 98453 21989) ✅",
                    isVerified = true,
                    actionText = "Verified",
                    onAction = { viewModel.showToast("Mobile number is already verified!") },
                    testTag = "verified_phone_tag"
                )
            }

            // 4. Education & Profession Verification
            item {
                VerificationTierCard(
                    icon = Icons.Default.School,
                    title = "Education & Degree Credential",
                    subtitle = "B.Tech in Computer Science from NIT Calicut (Self-Certified)",
                    isVerified = true,
                    actionText = "Verified",
                    onAction = { viewModel.showToast("Degree certified in profile biodata.") },
                    testTag = "verified_edu_tag"
                )
            }

            // Why Trust Matters Benefits Card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = DeepBurgundy,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Benefits of Verified Status",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBurgundy
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        TrustBenefitItem(
                            title = "300% Higher Response Rate",
                            description = "Verified profiles receive immediate responses from parents and candidates."
                        )
                        TrustBenefitItem(
                            title = "Top Ranking in Match Feeds",
                            description = "Your card is prioritized on the Discovery screen with the Blue Trust Shield."
                        )
                        TrustBenefitItem(
                            title = "Zero Spam & 100% Privacy",
                            description = "Only verified members can view full biodata and contact numbers."
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerificationTierCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isVerified: Boolean,
    actionText: String,
    onAction: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, if (isVerified) LightGreen else BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isVerified) LightGreen else LightRose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isVerified) SuccessGreen else DeepBurgundy,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBurgundy
                    )
                    if (isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onAction,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVerified) LightGreen else DeepBurgundy,
                    contentColor = if (isVerified) SuccessGreen else PureWhite
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag(testTag)
            ) {
                Text(
                    text = actionText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TrustBenefitItem(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = GoldAccent,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 14.sp
            )
        }
    }
}
