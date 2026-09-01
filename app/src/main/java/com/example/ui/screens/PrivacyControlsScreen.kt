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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

/**
 * Privacy Controls — profile visibility, photo privacy, horoscope/income
 * hiding, call permissions, last-seen & read receipts, incognito mode.
 * Persisted to Firebase Realtime Database under privacy_settings/{uid}.
 */
@Composable
fun PrivacyControlsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.privacySettings.collectAsState()

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
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Privacy Controls",
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
            // Who can see my profile
            PrivacyOptionsCard(
                icon = Icons.Default.RemoveRedEye,
                title = "Profile Visibility",
                subtitle = "Choose who can discover your biodata in feeds and search",
                options = listOf("Everyone", "Verified Only", "Hidden"),
                selected = settings.profileVisibility,
                onSelect = { selected ->
                    viewModel.updatePrivacySettings { s ->
                        s.copy(profileVisibility = selected)
                    }
                },
                optionHelp = mapOf(
                    "Everyone" to "Visible to all members on Soulmate",
                    "Verified Only" to "Only Face / ID verified members can view you",
                    "Hidden" to "Paused — hidden from all feeds without deleting data"
                )
            )

            // Photo visibility
            PrivacyOptionsCard(
                icon = Icons.Default.Photo,
                title = "Photo Privacy",
                subtitle = "Control who can see your photo gallery",
                options = listOf("All Users", "Connected Only", "Private"),
                selected = settings.photoVisibility,
                onSelect = { selected ->
                    viewModel.updatePrivacySettings { s ->
                        s.copy(photoVisibility = selected)
                    }
                },
                optionHelp = mapOf(
                    "All Users" to "Any member browsing your profile",
                    "Connected Only" to "Only members you connected with",
                    "Private" to "Photos hidden behind a request-to-view flow"
                )
            )

            // Boolean toggles
            PrivacyToggleCard(
                icon = Icons.Default.Stars,
                title = "Show Horoscope Details",
                subtitle = "Display nakshatra, rasi and dosham on your biodata",
                checked = settings.showHoroscope,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(showHoroscope = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.Paid,
                title = "Show Annual Income",
                subtitle = "Hide your income bracket from other members",
                checked = settings.showIncome,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(showIncome = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.FamilyRestroom,
                title = "Show Family Details",
                subtitle = "Display family background and roots section",
                checked = settings.showFamilyDetails,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(showFamilyDetails = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.Videocam,
                title = "Allow Direct Voice / Video Calls",
                subtitle = "Let connected members call you from chat",
                checked = settings.allowDirectCalls,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(allowDirectCalls = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.Schedule,
                title = "Show Last Seen",
                subtitle = "Display when you were last active in chats",
                checked = settings.lastSeenVisible,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(lastSeenVisible = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.MarkChatRead,
                title = "Read Receipts",
                subtitle = "Show blue ticks when you read messages",
                checked = settings.readReceiptsEnabled,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(readReceiptsEnabled = checked) }
                }
            )

            PrivacyToggleCard(
                icon = Icons.Default.VisibilityOff,
                title = "Incognito Browsing",
                subtitle = "View profiles without appearing in their visitors list",
                checked = settings.incognitoMode,
                onChecked = { checked ->
                    viewModel.updatePrivacySettings { s -> s.copy(incognitoMode = checked) }
                }
            )

            // Watermark info card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "All your photos are automatically watermark-protected and phone numbers stay hidden until you unlock contact sharing.",
                        fontSize = 12.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PrivacyOptionsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    optionHelp: Map<String, String>
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(LightBlue, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            options.forEach { option ->
                val isSelected = option == selected
                Surface(
                    onClick = { onSelect(option) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) LightBlue else SurfaceLight,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, if (isSelected) PrimaryBlue else BorderLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) PrimaryBlue else TextPrimary
                            )
                            Text(
                                text = optionHelp[option] ?: "",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Selected",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PrivacyToggleCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(LightGreenBackground, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryEmerald,
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
            Switch(
                checked = checked,
                onCheckedChange = onChecked,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PureWhite,
                    checkedTrackColor = PrimaryEmerald,
                    uncheckedThumbColor = PureWhite,
                    uncheckedTrackColor = BorderLight
                )
            )
        }
    }
}
