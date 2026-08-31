package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
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
fun LoginScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val countryCode by viewModel.countryCode.collectAsState()
    var countryDropdownExpanded by remember { mutableStateOf(false) }

    val countryCodes = listOf(
        "+91" to "🇮🇳 India (+91)",
        "+971" to "🇦🇪 UAE (+971)",
        "+966" to "🇸🇦 Saudi Arabia (+966)",
        "+965" to "🇰🇼 Kuwait (+965)",
        "+974" to "🇶🇦 Qatar (+974)",
        "+1" to "🇺🇸 USA (+1)",
        "+44" to "🇬🇧 UK (+44)",
        "+65" to "🇸🇬 Singapore (+65)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        // Back & Language Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(ScreenState.LANGUAGE_SELECT) },
                modifier = Modifier.testTag("login_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Surface(
                onClick = { viewModel.navigateTo(ScreenState.LANGUAGE_SELECT) },
                shape = RoundedCornerShape(20.dp),
                color = LightRose,
                modifier = Modifier.testTag("login_language_tag")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = selectedLanguage.flag, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedLanguage.nativeName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBurgundy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Headline
        Text(
            text = "Welcome to Soulmate",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = DeepBurgundy
        )
        Text(
            text = "Enter your mobile number to find your perfect partner",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mobile Number Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Mobile Number",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Country Code Picker
                    Box {
                        Surface(
                            onClick = { countryDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            color = WarmBackground,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("country_code_dropdown")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = countryCode,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = countryDropdownExpanded,
                            onDismissRequest = { countryDropdownExpanded = false }
                        ) {
                            countryCodes.forEach { (code, label) ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        viewModel.setCountryCode(code)
                                        countryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Phone Number Input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { if (it.length <= 10) viewModel.setPhoneNumber(it.filter { char -> char.isDigit() }) },
                        placeholder = { Text("98765 43210", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                            focusedBorderColor = DeepBurgundy,
                            unfocusedBorderColor = BorderLight,
                            cursorColor = DeepBurgundy,
                            focusedLeadingIconColor = DeepBurgundy,
                            unfocusedLeadingIconColor = TextSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_number_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                tint = DeepBurgundy
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "🔒 An OTP will be sent for verification. Your number is kept 100% confidential.",
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Get OTP Button — triggers real Firebase SMS OTP
        val activity = context as? Activity
        val isSendingOtp by viewModel.isOtpSending.collectAsState()
        Button(
            onClick = {
                if (phoneNumber.length < 5) {
                    viewModel.setPhoneNumber("9876543210") // auto-fill sample for convenience
                }
                if (activity != null) {
                    viewModel.requestOtp(activity)
                } else {
                    // Headless fallback (rare): go straight to OTP entry in demo mode
                    viewModel.navigateTo(ScreenState.OTP_VERIFY)
                }
            },
            enabled = !isSendingOtp,
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepBurgundy,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("get_otp_button")
        ) {
            if (isSendingOtp) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = PureWhite,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Sending OTP via Firebase...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Get OTP / ഒ.ടി.പി നേടുക",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Or Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(BorderLight)
            )
            Text(
                text = "OR",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(BorderLight)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In (Credential Manager + Firebase)
        val googleActivity = context as? Activity
        val isGoogleSigningIn by viewModel.isGoogleSigningIn.collectAsState()
        OutlinedButton(
            onClick = { googleActivity?.let { viewModel.signInWithGoogle(it) } },
            enabled = !isGoogleSigningIn,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderLight),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("google_signin_button")
        ) {
            if (isGoogleSigningIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = DeepBurgundy,
                    strokeWidth = 2.dp
                )
            } else {
                // Google "G" mark
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(PureWhite)
                        .border(1.dp, BorderLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4285F4)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Create Profile Direct Link
        TextButton(
            onClick = {
                viewModel.navigateTo(ScreenState.PROFILE_CREATION)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("new_profile_creation_link")
        ) {
            Text(
                text = "New User? Create Matrimony Profile Wizard ✨",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Trust Footer
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Safe",
                tint = SuccessGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Verified Matchmaking Platform by Soulmate",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun OtpVerificationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val countryCode by viewModel.countryCode.collectAsState()
    val otpCode by viewModel.otpCode.collectAsState()
    val isOtpError by viewModel.isOtpError.collectAsState()
    val isVerifying by viewModel.isVerifyingOtp.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity
    var secondsLeft by remember { mutableIntStateOf(30) }

    LaunchedEffect(key1 = secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    // Auto-submit when all 6 digits are entered
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6 && !isVerifying) {
            viewModel.verifyOtp(otpCode)
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
        // Top Back Button
        IconButton(
            onClick = { viewModel.navigateTo(ScreenState.LOGIN) },
            modifier = Modifier.testTag("otp_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Icon Header
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(LightRose, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "OTP Lock",
                tint = DeepBurgundy,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Verify Mobile Number",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Enter the 6-digit code sent to ",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = "$countryCode $phoneNumber",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // OTP Input Boxes (6 Digits — Firebase Phone Auth standard)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0 until 6) {
                val digit = if (i < otpCode.length) otpCode[i].toString() else ""
                val isFocused = i == otpCode.length

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PureWhite)
                        .border(
                            width = if (isFocused) 2.dp else 1.dp,
                            color = when {
                                isOtpError -> CrimsonRed
                                isFocused -> RosePrimary
                                digit.isNotEmpty() -> DeepBurgundy
                                else -> BorderLight
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .testTag("otp_box_$i"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = digit,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        if (isOtpError) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Invalid code — please enter the 6-digit OTP",
                color = CrimsonRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Demo Helper Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Demo Tip",
                    tint = SuccessGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Demo fallback: use OTP 123456 if Firebase SMS is unavailable",
                    fontSize = 11.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { viewModel.setOtpCode("123456") },
                    modifier = Modifier.testTag("quick_fill_otp")
                ) {
                    Text("Auto Fill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resend Timer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (secondsLeft > 0) {
                Text(
                    text = "Resend OTP in 00:${if (secondsLeft < 10) "0$secondsLeft" else secondsLeft}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            } else {
                TextButton(
                    onClick = {
                        secondsLeft = 30
                        activity?.let { viewModel.requestOtp(it) }
                    },
                    modifier = Modifier.testTag("resend_otp_button")
                ) {
                    Text(
                        text = "Resend OTP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Verify Button
        Button(
            onClick = { viewModel.verifyOtp(otpCode) },
            enabled = otpCode.length == 6 && !isVerifying,
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepBurgundy,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("verify_otp_button")
        ) {
            if (isVerifying) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = PureWhite,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Verifying...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Verify & Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Numeric Keypad for direct testing
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val keyRows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "⌫")
            )

            for (row in keyRows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (key in row) {
                        Surface(
                            onClick = {
                                when (key) {
                                    "C" -> viewModel.setOtpCode("")
                                    "⌫" -> {
                                        if (otpCode.isNotEmpty()) {
                                            viewModel.setOtpCode(otpCode.dropLast(1))
                                        }
                                    }
                                    else -> {
                                        if (otpCode.length < 6) {
                                            viewModel.setOtpCode(otpCode + key)
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = PureWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("keypad_$key")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = key,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
