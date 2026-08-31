package com.example.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.PendingAccountAction
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

/**
 * Settings hub — account management, privacy, legal, support and the
 * destructive account actions (deactivate / delete) plus Logout with a
 * confirmation dialog.
 */
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val myProfile by viewModel.myProfile.collectAsState()
    val verification by viewModel.verificationStatus.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val emailState by viewModel.emailVerificationState.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PureWhite)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Settings",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile summary card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PureWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(LightGreen)
                    ) {
                        AsyncImage(
                            model = myProfile.photoUrls.firstOrNull(),
                            contentDescription = myProfile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = myProfile.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "+91 ${if (viewModel.phoneNumber.value.isNotBlank()) viewModel.phoneNumber.value else "98765 43210"}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (verification.trustScore >= 90) LightGreen else LightGold
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = if (verification.trustScore >= 90) SuccessGreen else DarkGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${verification.trustScore}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (verification.trustScore >= 90) SuccessGreen else DarkGold
                            )
                        }
                    }
                }
            }

            // Email verification (email OTP link) card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "E-mail Verification",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        when (emailState) {
                            "VERIFIED" -> Surface(shape = RoundedCornerShape(8.dp), color = LightGreen) {
                                Text(
                                    "Verified", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            "PENDING" -> Surface(shape = RoundedCornerShape(8.dp), color = LightGold) {
                                Text(
                                    "Pending", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                    color = DarkGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            else -> Surface(shape = RoundedCornerShape(8.dp), color = SurfaceLight) {
                                Text(
                                    "Not Set", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                    color = TextMuted,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (userEmail.isBlank())
                            "Add an e-mail to receive OTP verification links, receipts and match alerts."
                        else userEmail,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.navigateTo(ScreenState.EDIT_PROFILE) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue, contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 14.dp, vertical = 8.dp
                            )
                        ) {
                            Text(if (userEmail.isBlank()) "Add E-mail" else "Change E-mail", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        if (emailState == "PENDING") {
                            OutlinedButton(
                                onClick = { viewModel.resendEmailVerification() },
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue)
                            ) {
                                Text("Resend Link", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }
                            TextButton(onClick = { viewModel.checkEmailVerification() }) {
                                Text("I Verified It", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                        }
                    }
                }
            }

            // ---- Preferences section ----
            SettingsSection(title = "Account & Preferences") {
                SettingsRow(
                    icon = Icons.Default.VerifiedUser,
                    title = "Edit My Biodata",
                    subtitle = "Update your profile, photos and horoscope details",
                    onClick = { viewModel.navigateTo(ScreenState.EDIT_PROFILE) }
                )
                SettingsRow(
                    icon = Icons.Default.Shield,
                    title = "Trust & Verification Center",
                    subtitle = "Face liveness, Govt ID and Trust Score ${verification.trustScore}%",
                    onClick = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) }
                )
                SettingsRow(
                    icon = Icons.Default.VerifiedUser,
                    title = "Privacy Controls",
                    subtitle = "Profile visibility, photo privacy and horoscope hiding",
                    onClick = { viewModel.navigateTo(ScreenState.PRIVACY_CONTROLS) }
                )
            }

            // ---- Activity section ----
            SettingsSection(title = "Activity") {
                SettingsRow(
                    icon = Icons.Default.Notifications,
                    title = "Notification Center",
                    subtitle = "Interests, messages, visitors and match alerts",
                    onClick = { viewModel.navigateTo(ScreenState.NOTIFICATIONS) }
                )
                SettingsRow(
                    icon = Icons.Default.History,
                    title = "Subscription & Payments",
                    subtitle = "Manage plan, view invoices and receipts",
                    onClick = { viewModel.navigateTo(ScreenState.PAYMENT_HISTORY) }
                )
                SettingsRow(
                    icon = Icons.Default.Share,
                    title = "Refer & Earn",
                    subtitle = "Invite friends, earn premium weeks",
                    onClick = { viewModel.navigateTo(ScreenState.REFERRAL) }
                )
                SettingsRow(
                    icon = Icons.Default.Favorite,
                    title = "Success Stories",
                    subtitle = "Couples who found each other on Soulmate",
                    onClick = { viewModel.navigateTo(ScreenState.SUCCESS_STORIES) }
                )
            }

            // ---- Support & safety ----
            SettingsSection(title = "Safety & Support") {
                SettingsRow(
                    icon = Icons.Default.GppGood,
                    title = "Safety Center",
                    subtitle = "Report profiles, blocked members and safety tips",
                    onClick = { viewModel.navigateTo(ScreenState.SAFETY_CENTER) }
                )
                SettingsRow(
                    icon = Icons.Default.HelpOutline,
                    title = "Help & Support",
                    subtitle = "FAQs and contact our care team",
                    onClick = { viewModel.navigateTo(ScreenState.HELP_SUPPORT) }
                )
            }

            // ---- Legal ----
            SettingsSection(title = "Legal") {
                SettingsRow(
                    icon = Icons.Default.Policy,
                    title = "Privacy Policy",
                    subtitle = "How we protect and process your data",
                    onClick = { viewModel.navigateTo(ScreenState.PRIVACY_POLICY) }
                )
                SettingsRow(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    subtitle = "Membership, refund and usage terms",
                    onClick = { viewModel.navigateTo(ScreenState.TERMS_OF_SERVICE) }
                )
            }

            // ---- Logout ----
            Button(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            // ---- Danger zone ----
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Danger Zone",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "These actions are protected by mobile OTP verification.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.startAccountVerification(PendingAccountAction.DEACTIVATE) },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOff,
                            contentDescription = null,
                            tint = DarkGold,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Deactivate My Profile",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.startAccountVerification(PendingAccountAction.DELETE) },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Delete Account Permanently",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // ---- Logout confirmation dialog ----
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(
                    text = "Log Out of Soulmate?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "You will stop receiving new interests and match alerts while logged out. Your profile, chats and verification progress will be safely saved.\n\nAre you sure you want to continue?",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.performLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Yes, Log Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                ) {
                    Text("Stay Logged In", color = TextSecondary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) { content() }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(LightGreenBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Open",
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
