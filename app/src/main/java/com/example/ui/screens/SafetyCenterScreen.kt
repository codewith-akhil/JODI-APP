package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

/**
 * Safety Center — safety guidelines, report-a-profile flow and
 * blocked-members management. Reports and blocks persist to Firebase.
 */
@Composable
fun SafetyCenterScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.profiles.collectAsState()
    val blockedUsers by viewModel.blockedUsers.collectAsState()

    var reportTarget by remember { mutableStateOf<Profile?>(null) }
    var blockTarget by remember { mutableStateOf<Profile?>(null) }

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
            IconButton(onClick = { viewModel.navigateTo(ScreenState.SETTINGS) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Icon(
                imageVector = Icons.Default.GppGood,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Safety Center",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Safety commitments
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PureWhite
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Our Safety Commitments",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        SafetyTip(
                            icon = Icons.Default.Lock,
                            text = "Phone numbers stay hidden until you explicitly unlock contact sharing."
                        )
                        SafetyTip(
                            icon = Icons.Default.TipsAndUpdates,
                            text = "Never share OTPs, bank details, or transfer money to a match — even for \"emergencies\"."
                        )
                        SafetyTip(
                            icon = Icons.Default.MoneyOff,
                            text = "Soulmate never asks for fees outside the app. Report anyone who does."
                        )
                        SafetyTip(
                            icon = Icons.Default.Person,
                            text = "Meet in public places and inform family when meeting a match in person for the first time."
                        )
                    }
                }
            }

            // Report a profile
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = DarkGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Report a Profile",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Flag suspicious, abusive or fake profiles. Our Trust & Safety team reviews every report within 24 hours.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        profiles.take(5).forEach { profile ->
                            MemberActionRow(
                                profile = profile,
                                actionLabel = "Report",
                                actionColor = DarkGold,
                                onAction = { reportTarget = profile }
                            )
                        }
                    }
                }
            }

            // Block a member
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Block a Member",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Blocked members can't view your profile, message you, or see you in discovery.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        profiles.take(5).forEach { profile ->
                            MemberActionRow(
                                profile = profile,
                                actionLabel = "Block",
                                actionColor = PrimaryBlue,
                                onAction = { blockTarget = profile }
                            )
                        }
                    }
                }
            }

            // Blocked list
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Blocked Members (${blockedUsers.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (blockedUsers.isEmpty()) {
                            Text(
                                text = "You haven't blocked anyone. A respectful community starts with clear boundaries.",
                                fontSize = 12.sp,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        } else {
                            blockedUsers.forEach { blocked ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(LightBlue)
                                    ) {
                                        AsyncImage(
                                            model = blocked.photoUrls.firstOrNull(),
                                            contentDescription = blocked.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = blocked.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    TextButton(onClick = { viewModel.unblockProfile(blocked) }) {
                                        Text("Unblock", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // ---- Report dialog ----
    reportTarget?.let { target ->
        ReportProfileDialog(
            profile = target,
            onDismiss = { reportTarget = null },
            onSubmit = { reason, details ->
                viewModel.reportProfile(target, reason, details)
                reportTarget = null
            }
        )
    }

    // ---- Block confirmation dialog ----
    blockTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { blockTarget = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text("Block ${target.name}?", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "You will no longer see each other in discovery, chat or interests. This can be reversed anytime from Safety Center.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.blockProfile(target)
                        blockTarget = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue, contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Yes, Block", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { blockTarget = null },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                ) {
                    Text("Cancel", color = TextSecondary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SafetyTip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(modifier = Modifier.padding(vertical = 5.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryEmerald,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MemberActionRow(
    profile: Profile,
    actionLabel: String,
    actionColor: androidx.compose.ui.graphics.Color,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(LightGreenBackground)
        ) {
            AsyncImage(
                model = profile.photoUrls.firstOrNull(),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${profile.name}, ${profile.age}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "${profile.city} • ${profile.profession}",
                fontSize = 10.sp,
                color = TextMuted
            )
        }
        Surface(
            onClick = onAction,
            shape = RoundedCornerShape(9.dp),
            color = SurfaceLight,
            border = androidx.compose.foundation.BorderStroke(1.dp, actionColor.copy(alpha = 0.5f))
        ) {
            Text(
                text = actionLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = actionColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ReportProfileDialog(
    profile: Profile,
    onDismiss: () -> Unit,
    onSubmit: (reason: String, details: String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Fake profile") }
    var details by remember { mutableStateOf("") }

    val reasons = listOf(
        "Fake profile",
        "Inappropriate photos",
        "Harassment / abuse",
        "Asking for money",
        "Spam or scam links",
        "Already married"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = DarkGold,
                modifier = Modifier.size(30.dp)
            )
        },
        title = {
            Text("Report ${profile.name}", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "Why are you reporting this profile?",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                reasons.forEach { reason ->
                    val isSelected = reason == selectedReason
                    Surface(
                        onClick = { selectedReason = reason },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) LightGold else SurfaceLight,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, if (isSelected) DarkGold else BorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = reason,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) DarkGold else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    placeholder = {
                        Text("Additional details (optional)", fontSize = 12.sp)
                    },
                    minLines = 2,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGold,
                        unfocusedBorderColor = BorderLight,
                        cursorColor = DarkGold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedReason, details) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGold, contentColor = PureWhite
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Submit Report", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = PureWhite,
        shape = RoundedCornerShape(20.dp)
    )
}
