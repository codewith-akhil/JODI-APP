package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matchmaking.MatchmakingEngine
import com.example.model.Profile
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DeepBurgundy
import com.example.ui.theme.DividerColor
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightBlue
import com.example.ui.theme.LightGold
import com.example.ui.theme.LightGreen
import com.example.ui.theme.LightRose
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.WarmBackground
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.BottomTab
import com.example.viewmodel.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.selectedProfile.collectAsState()
    val myProfile by viewModel.myProfile.collectAsState()
    var showContactSheet by remember { mutableStateOf(false) }
    var currentPhotoIndex by remember { mutableIntStateOf(0) }

    if (profile == null) return
    val p = profile!!

    val matchReport = remember(p, myProfile) {
        MatchmakingEngine.calculateCompatibility(myProfile, p)
    }

    Scaffold(
        bottomBar = {
            ProfileBottomActionBar(
                profile = p,
                onConnect = { viewModel.toggleConnect(p.id) },
                onChat = { viewModel.openChat(p) },
                onViewContact = { showContactSheet = true }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(WarmBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Photo Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = p.photoUrls.getOrElse(currentPhotoIndex) { p.photoUrls.first() },
                        contentDescription = p.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x66000000), Color.Transparent, Color(0xCC000000)),
                                    startY = 0f,
                                    endY = 1000f
                                )
                            )
                    )

                    // Navigation Bar Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0x66000000), CircleShape)
                                .testTag("detail_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PureWhite
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { viewModel.toggleShortlist(p.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0x66000000), CircleShape)
                                    .testTag("detail_shortlist_button")
                            ) {
                                Icon(
                                    imageVector = if (p.isShortlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Shortlist",
                                    tint = if (p.isShortlisted) RosePrimary else PureWhite
                                )
                            }
                            IconButton(
                                onClick = { viewModel.showToast("Profile link copied to clipboard") },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0x66000000), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = PureWhite
                                )
                            }
                        }
                    }

                    // Photo Carousel Dots
                    if (p.photoUrls.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 90.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            p.photoUrls.indices.forEach { index ->
                                Box(
                                    modifier = Modifier
                                        .size(if (index == currentPhotoIndex) 10.dp else 8.dp)
                                        .clip(CircleShape)
                                        .background(if (index == currentPhotoIndex) GoldAccent else PureWhite.copy(alpha = 0.5f))
                                        .clickable { currentPhotoIndex = index }
                                )
                            }
                        }
                    }

                    // Bottom info on photo
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${p.name}, ${p.age}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            if (p.verified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = VerifiedBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Text(
                            text = "Matrimony ID: ${p.id} • ${p.motherTongue}",
                            fontSize = 13.sp,
                            color = LightGold
                        )
                    }
                }
            }

            // Trust Score Banner
            item {
                TrustScoreBanner(trustScore = p.trustScore)
            }

            // Astrological Matchmaking & 10 Poruthams Report
            item {
                DetailCard(title = "Astrological Compatibility & 10 Poruthams", icon = Icons.Default.AutoAwesome) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightGold,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${matchReport.poruthamCount} / 10 Poruthams Matching",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGold
                                    )
                                    Text(
                                        text = "Astro Match: ${matchReport.overallScore}% • Guna: ${matchReport.gunaScore}/36",
                                        fontSize = 12.sp,
                                        color = DeepBurgundy,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (matchReport.overallScore >= 85) SuccessGreen else DeepBurgundy
                                ) {
                                    Text(
                                        text = if (matchReport.overallScore >= 85) "HIGH MATCH" else "GOOD MATCH",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PureWhite,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = matchReport.astrologicalSummary,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 10 Poruthams Breakdown Table
                    matchReport.poruthams.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = item.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (item.isMatched) LightGreen else LightRose
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.isMatched) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (item.isMatched) SuccessGreen else CrimsonRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = if (item.isMatched) "Match (${item.score}/${item.maxScore})" else "No (0/${item.maxScore})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isMatched) SuccessGreen else CrimsonRed
                                    )
                                }
                            }
                        }
                        Divider(color = DividerColor.copy(alpha = 0.5f), thickness = 0.5.dp)
                    }
                }
            }

            // About Bio Section
            item {
                DetailCard(title = "About ${p.name}", icon = Icons.Default.AccountCircle) {
                    Text(
                        text = p.bio,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }
            }

            // Basic Details
            item {
                DetailCard(title = "Basic & Lifestyle Details", icon = Icons.Default.AccountCircle) {
                    DetailRow(label = "Age / Height", value = "${p.age} Yrs, ${p.height}")
                    DetailRow(label = "Marital Status", value = p.maritalStatus)
                    DetailRow(label = "Mother Tongue", value = p.motherTongue)
                    DetailRow(label = "Eating Habits", value = p.diet)
                    DetailRow(label = "Drinking / Smoking", value = "${p.drinking} / ${p.smoking}")
                    DetailRow(label = "Native Location", value = "${p.city}, ${p.district}, ${p.state}")
                }
            }

            // Religious & Horoscope (Jathakam)
            item {
                DetailCard(title = "Religious & Horoscope (Jathakam)", icon = Icons.Default.AutoAwesome) {
                    DetailRow(label = "Religion & Caste", value = "${p.religion} - ${p.caste}")
                    DetailRow(label = "Gothram", value = p.gothram)
                    DetailRow(label = "Star (Nakshatra)", value = p.starNakshatra)
                    DetailRow(label = "Rasi (Moon Sign)", value = p.rasi)
                    DetailRow(label = "Dosham Status", value = p.dosham)
                }
            }

            // Education & Profession
            item {
                DetailCard(title = "Education & Career", icon = Icons.Default.BusinessCenter) {
                    DetailRow(label = "Education", value = p.education)
                    DetailRow(label = "College / University", value = p.college)
                    DetailRow(label = "Profession / Role", value = p.profession)
                    DetailRow(label = "Company / Employer", value = p.company)
                    DetailRow(label = "Annual Income", value = p.annualIncome)
                    DetailRow(label = "Work Location", value = "${p.city}, ${p.state}")
                }
            }

            // Family Background
            item {
                DetailCard(title = "Family Background", icon = Icons.Default.FamilyRestroom) {
                    DetailRow(label = "Father's Details", value = p.familyFather)
                    DetailRow(label = "Mother's Details", value = p.familyMother)
                    DetailRow(label = "Siblings", value = p.familySiblings)
                    DetailRow(label = "Family Type", value = p.familyType)
                    DetailRow(label = "Ancestral Native Place", value = p.nativePlace)
                }
            }

            // Partner Preferences
            item {
                DetailCard(title = "Partner Preferences", icon = Icons.Default.Psychology) {
                    DetailRow(label = "Expected Age", value = p.partnerAgeRange)
                    DetailRow(label = "Expected Height", value = p.partnerHeightRange)
                    DetailRow(label = "Preferred Education", value = p.partnerEducation)
                    DetailRow(label = "Preferred Location", value = p.partnerLocation)
                    DetailRow(label = "Preferred Caste", value = p.partnerCaste)
                }
            }
        }
    }

    // Modal Sheet for Verified Contact info
    if (showContactSheet) {
        ModalBottomSheet(
            onDismissRequest = { showContactSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = PureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(LightGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Premium",
                        tint = DarkGold,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Unlock Contact Number of ${p.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Upgrade to Soulmate Gold or Platinum to view verified family phone numbers and WhatsApp directly.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Blurred Number Preview
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Parent's Phone Number", fontSize = 11.sp, color = TextMuted)
                            Text(text = "+91 9847X XXXXX", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", tint = RosePrimary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        showContactSheet = false
                        viewModel.selectBottomTab(BottomTab.PREMIUM)
                        viewModel.navigateTo(ScreenState.MAIN_APP)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = DarkCardSurface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("View Subscription Plans & Unlock", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileBottomActionBar(
    profile: Profile,
    onConnect: () -> Unit,
    onChat: () -> Unit,
    onViewContact: () -> Unit
) {
    Surface(
        color = PureWhite,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chat
            IconButton(
                onClick = onChat,
                modifier = Modifier
                    .size(48.dp)
                    .background(LightBlue, CircleShape)
                    .testTag("detail_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = VerifiedBlue
                )
            }

            // View Contact
            OutlinedButton(
                onClick = onViewContact,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Contact",
                    tint = DarkGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Phone No",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGold
                )
            }

            // Send Interest
            Button(
                onClick = onConnect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (profile.isConnected) SuccessGreen else DeepBurgundy,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("detail_connect_button")
            ) {
                Text(
                    text = if (profile.isConnected) "✓ Interest Sent" else "Send Interest 💍",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TrustScoreBanner(trustScore: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Trust",
                tint = SuccessGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "$trustScore% Verified Trust Badge",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Text(
                    text = "Government ID, Degree Certificate & Mobile number verified by Soulmate trust team.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = DeepBurgundy,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = DividerColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.weight(0.55f)
        )
    }
}
