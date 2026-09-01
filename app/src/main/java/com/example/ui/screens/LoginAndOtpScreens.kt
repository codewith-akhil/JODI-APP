package com.example.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.composed
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val countryCode by viewModel.countryCode.collectAsState()
    val context = LocalContext.current
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
                color = LightTeal,
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
                        color = PrimaryEmerald
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Real JODI SOULMATE emblem
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(PureWhite)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.ic_jodii_logo),
                contentDescription = "JODI Soulmate",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Headline
        Text(
            text = "Welcome to Soulmate",
            fontSize = 27.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryEmerald
        )
        Text(
            text = "Enter your mobile number to find your perfect partner",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

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
                            focusedBorderColor = PrimaryEmerald,
                            unfocusedBorderColor = BorderLight,
                            cursorColor = PrimaryEmerald,
                            focusedLeadingIconColor = PrimaryEmerald,
                            unfocusedLeadingIconColor = TextSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_number_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                tint = PrimaryEmerald
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
                if (phoneNumber.length < 10) {
                    viewModel.showToast("Please enter a valid 10-digit mobile number.")
                    return@Button
                }
                activity?.let { viewModel.requestOtp(it) }
            },
            enabled = !isSendingOtp,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryEmerald,
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
                    text = "Sending OTP...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Get OTP",
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
                    color = PrimaryEmerald,
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
                text = "Verified Matchmaking Platform by JODI Soulmate",
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
    var secondsLeft by androidx.compose.runtime.mutableIntStateOf(60)
    val focusRequester = remember { FocusRequester() }

    // Open the keyboard automatically when the screen appears
    LaunchedEffect(Unit) {
        delay(250) // let the transition settle so focus is not stolen
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        secondsLeft = 60
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    // Auto-submit when all 6 digits are entered
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6 && !isVerifying) {
            delay(150)
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
            .noRippleClickable { focusRequester.requestFocus() }
    ) {
        // Top Back Button
        IconButton(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier.testTag("otp_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Icon Header
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(LightGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "OTP Lock",
                tint = PrimaryEmerald,
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
                color = PrimaryEmerald
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // OTP Input — a real (visually hidden) text field drives the 6 boxes,
        // so the system keyboard opens and SMS autofill works natively.
        Box {
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
                                    isFocused -> PrimaryEmerald
                                    digit.isNotEmpty() -> PrimaryEmerald
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

            BasicTextField(
                value = otpCode,
                onValueChange = { viewModel.setOtpCode(it) },
                textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
                cursorBrush = SolidColor(Color.Transparent),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { viewModel.verifyOtp(otpCode) }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .focusRequester(focusRequester)
                    .testTag("otp_hidden_field")
            )
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

        // Resend Timer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (secondsLeft > 0) {
                Text(
                    text = "Resend OTP in ${secondsLeft}s",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            } else {
                TextButton(
                    onClick = {
                        secondsLeft = 60
                        activity?.let { viewModel.requestOtp(it) }
                    },
                    modifier = Modifier.testTag("resend_otp_button")
                ) {
                    Text(
                        text = "Resend OTP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryEmerald
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
                containerColor = PrimaryEmerald,
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Didn't receive the code? Check your SMS inbox or use the automatic detection when it arrives.",
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
