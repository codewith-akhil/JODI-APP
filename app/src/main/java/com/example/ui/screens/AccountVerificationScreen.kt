package com.example.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PendingAccountAction
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import kotlinx.coroutines.delay

/**
 * Mobile OTP verification gate for destructive account actions
 * (Deactivate profile / Delete account), backed by Firebase Phone Auth.
 */
@Composable
fun AccountVerificationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val action by viewModel.pendingAccountAction.collectAsState()
    val otpCode by viewModel.accountOtpCode.collectAsState()
    val isOtpError by viewModel.accountOtpError.collectAsState()
    val isSending by viewModel.isAccountOtpSending.collectAsState()
    val isPerforming by viewModel.isPerformingAccountAction.collectAsState()

    val context = LocalContext.current
    var secondsLeft by remember { mutableIntStateOf(45) }
    var otpSent by remember { mutableStateOf(false) }

    val isDelete = action == PendingAccountAction.DELETE
    val accent = if (isDelete) PrimaryBlue else DarkGold
    val accentContainer = if (isDelete) LightBlue else LightGold

    LaunchedEffect(key1 = secondsLeft) {
        if (otpSent && secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.cancelAccountAction() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Security Verification",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action icon header
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(accentContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDelete) Icons.Default.Delete else Icons.Default.PersonOff,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = if (isDelete) "Delete Account Permanently"
            else "Deactivate My Profile",
            fontSize = 23.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isDelete)
                "This will permanently erase your biodata, photos, chats, matches and subscriptions from our servers. This action cannot be undone."
            else
                "Your profile will be hidden from all discovery feeds immediately. You can reactivate anytime by logging back in — nothing is deleted.",
            fontSize = 13.sp,
            color = TextSecondary,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // OTP requirement explanation
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Protected by OTP Verification",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "We'll send a 6-digit code to your registered mobile number to confirm it's really you.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!otpSent) {
            // Send OTP stage
            Button(
                onClick = {
                    otpSent = true
                    (context as? Activity)?.let { viewModel.sendAccountActionOtp(it) }
                },
                enabled = !isSending,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = PureWhite,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Sending OTP...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isDelete) "Send OTP to Delete Account" else "Send OTP to Deactivate",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // OTP entry stage
            Text(
                text = "Enter the 6-digit code sent to your mobile",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6 OTP boxes
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 0 until 6) {
                    val digit = if (i < otpCode.length) otpCode[i].toString() else ""
                    val isFocused = i == otpCode.length
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PureWhite)
                            .border(
                                width = if (isFocused) 2.dp else 1.dp,
                                color = when {
                                    isOtpError -> CrimsonRed
                                    isFocused -> accent
                                    digit.isNotEmpty() -> accent
                                    else -> BorderLight
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            if (isOtpError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Invalid code. Please check the SMS and try again.",
                    color = CrimsonRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (secondsLeft > 0) {
                    Text(
                        text = "Resend OTP in 00:${if (secondsLeft < 10) "0$secondsLeft" else secondsLeft}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                } else {
                    TextButton(onClick = {
                        secondsLeft = 45
                        (context as? Activity)?.let { viewModel.sendAccountActionOtp(it) }
                    }) {
                        Text("Resend OTP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.verifyAccountActionOtp(otpCode) },
                enabled = otpCode.length == 6 && !isPerforming,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                if (isPerforming) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = PureWhite,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        if (isDelete) "Deleting Account..." else "Deactivating...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isDelete) "Verify & Delete Permanently" else "Verify & Deactivate",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { viewModel.cancelAccountAction() },
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                "Cancel — Keep My Account",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Demo tip: if Firebase SMS is unavailable, use OTP 123456.",
            fontSize = 10.sp,
            color = TextMuted,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
