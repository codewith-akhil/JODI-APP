package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmBackground
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GovtIdVerificationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val verification by viewModel.verificationStatus.collectAsState()

    var selectedIdType by remember { mutableStateOf("Aadhaar Card (DigiLocker)") }
    var idNumber by remember { mutableStateOf("9845 3219 8921") }
    var isFrontUploaded by remember { mutableStateOf(true) }
    var isBackUploaded by remember { mutableStateOf(true) }
    var isVerifying by remember { mutableStateOf(false) }
    var isVerifiedSuccess by remember { mutableStateOf(verification.isGovtIdVerified) }

    LaunchedEffect(isVerifying) {
        if (isVerifying) {
            delay(2000)
            isVerifying = false
            isVerifiedSuccess = true
            viewModel.performGovtIdVerification(selectedIdType.split(" ")[0], idNumber)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Government ID Verification",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "100% Genuine & Safe Matchmaking",
                            fontSize = 11.sp,
                            color = LightGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) },
                        modifier = Modifier.testTag("govt_id_back_button")
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
            // Header Security Shield
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isVerifiedSuccess) LightGreen else LightGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isVerifiedSuccess) Icons.Default.VerifiedUser else Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (isVerifiedSuccess) SuccessGreen else DeepBurgundy,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (isVerifiedSuccess) "ID Verified Successfully! ✅" else "Why Verify Government ID?",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isVerifiedSuccess) SuccessGreen else DeepBurgundy
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isVerifiedSuccess)
                                    "Your profile has the official Verified Green Seal & top search ranking."
                                else
                                    "Verified profiles receive 300% more matrimonial interest and instant family trust.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Select ID Type
            item {
                Text(
                    text = "Select Document Type",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
                Spacer(modifier = Modifier.height(8.dp))
                val idOptions = listOf(
                    "Aadhaar Card (DigiLocker)",
                    "Passport",
                    "Driving License",
                    "Voter ID Card"
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    idOptions.forEach { opt ->
                        val isSelected = selectedIdType == opt
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) DeepBurgundy else PureWhite,
                            border = BorderStroke(1.dp, if (isSelected) DeepBurgundy else BorderLight),
                            modifier = Modifier.clickable { selectedIdType = opt }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = opt,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PureWhite else TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // ID Number Input
            item {
                Text(
                    text = "Document Identification Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = idNumber,
                    onValueChange = { idNumber = it },
                    placeholder = { Text("e.g. 12-digit Aadhaar / Passport number", fontSize = 13.sp, color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("govt_id_number_input"),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Bold),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite,
                        focusedBorderColor = DeepBurgundy,
                        unfocusedBorderColor = BorderLight,
                        cursorColor = DeepBurgundy
                    )
                )
            }

            // Document Upload Cards (Front & Back)
            item {
                Text(
                    text = "Upload Document Photos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Front
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clickable { isFrontUploaded = !isFrontUploaded },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isFrontUploaded) LightGreen else PureWhite),
                        border = BorderStroke(1.5.dp, if (isFrontUploaded) SuccessGreen else BorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isFrontUploaded) Icons.Default.CloudDone else Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = if (isFrontUploaded) SuccessGreen else DeepBurgundy,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isFrontUploaded) "Front Side Ready ✅" else "Front Side Photo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFrontUploaded) SuccessGreen else TextPrimary
                            )
                            Text(
                                text = if (isFrontUploaded) "Tap to change" else "Upload JPG/PNG",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Back
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clickable { isBackUploaded = !isBackUploaded },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isBackUploaded) LightGreen else PureWhite),
                        border = BorderStroke(1.5.dp, if (isBackUploaded) SuccessGreen else BorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isBackUploaded) Icons.Default.CloudDone else Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = if (isBackUploaded) SuccessGreen else DeepBurgundy,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isBackUploaded) "Back Side Ready ✅" else "Back Side Photo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isBackUploaded) SuccessGreen else TextPrimary
                            )
                            Text(
                                text = if (isBackUploaded) "Tap to change" else "Upload JPG/PNG",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Verification Action Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isVerifying = true
                    },
                    enabled = !isVerifying,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVerifiedSuccess) SuccessGreen else DeepBurgundy,
                        contentColor = PureWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_govt_id_button")
                ) {
                    if (isVerifying) {
                        CircularProgressIndicator(
                            color = PureWhite,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Verifying with Government Database...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = if (isVerifiedSuccess) Icons.Default.Verified else Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isVerifiedSuccess) "Verified & Active! 🛡️" else "Verify ID Instantly via DigiLocker",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Privacy Assurance Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightRose),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DeepBurgundy,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Your document number and images are stored encrypted and are never visible to other members. Only the Green Verified Badge is displayed.",
                            fontSize = 11.sp,
                            color = DeepBurgundy,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
