package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.Profile
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.BottomTab
import com.example.viewmodel.ScreenState

@Composable
fun MainAppScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentBottomTab.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            SoulmateBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectBottomTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmBackground)
        ) {
            when (currentTab) {
                BottomTab.DISCOVERY -> DiscoveryFeedView(viewModel)
                BottomTab.MATCHES -> ShortlistedView(viewModel)
                BottomTab.INBOX -> InboxView(viewModel)
                BottomTab.PHOTOS -> PhotoManagerView(viewModel)
                BottomTab.PREMIUM -> MembershipScreen(viewModel)
            }
        }
    }
}

@Composable
fun SoulmateBottomNavigationBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = PureWhite,
        tonalElevation = 8.dp,
        modifier = modifier
            .navigationBarsPadding()
            .testTag("main_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentTab == BottomTab.DISCOVERY,
            onClick = { onTabSelected(BottomTab.DISCOVERY) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Discovery") },
            label = { Text("Daily Matches", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepBurgundy,
                selectedTextColor = DeepBurgundy,
                indicatorColor = LightRose
            ),
            modifier = Modifier.testTag("tab_discovery")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.MATCHES,
            onClick = { onTabSelected(BottomTab.MATCHES) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Shortlisted") },
            label = { Text("Shortlisted", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RosePrimary,
                selectedTextColor = RosePrimary,
                indicatorColor = LightRose
            ),
            modifier = Modifier.testTag("tab_shortlisted")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.INBOX,
            onClick = { onTabSelected(BottomTab.INBOX) },
            icon = {
                BadgedBox(
                    badge = { Badge { Text("3") } }
                ) {
                    Icon(Icons.Default.Mail, contentDescription = "Inbox")
                }
            },
            label = { Text("Inbox", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepBurgundy,
                selectedTextColor = DeepBurgundy,
                indicatorColor = LightRose
            ),
            modifier = Modifier.testTag("tab_inbox")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.PHOTOS,
            onClick = { onTabSelected(BottomTab.PHOTOS) },
            icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = "My Photos") },
            label = { Text("My Photos", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DeepBurgundy,
                selectedTextColor = DeepBurgundy,
                indicatorColor = LightRose
            ),
            modifier = Modifier.testTag("tab_photos")
        )
        NavigationBarItem(
            selected = currentTab == BottomTab.PREMIUM,
            onClick = { onTabSelected(BottomTab.PREMIUM) },
            icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = "Premium Subscription", tint = GoldAccent) },
            label = { Text("Membership", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                indicatorColor = LightGold
            ),
            modifier = Modifier.testTag("tab_premium")
        )
    }
}

@Composable
fun DiscoveryFeedView(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.profiles.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val myProfile by viewModel.myProfile.collectAsState()
    val verification by viewModel.verificationStatus.collectAsState()
    val mutualMatch by viewModel.mutualMatchProfile.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val filterList = listOf("All", "100% Verified", "Kochi & Ernakulam", "High Porutham (9/10)", "Engineers & Doctors", "Recent Profiles")

    val filteredProfiles = when (selectedFilter) {
        "100% Verified" -> profiles.filter { it.verified }
        "Kochi & Ernakulam" -> profiles.filter { it.city.contains("Ernakulam", true) || it.district.contains("Kochi", true) }
        "High Porutham (9/10)" -> profiles.filter { it.dosham.contains("No Dosham", true) }
        "Engineers & Doctors" -> profiles.filter { it.profession.contains("Engineer", true) || it.profession.contains("Physician", true) || it.profession.contains("Doctor", true) }
        "Recent Profiles" -> profiles.filter { it.joinedDaysAgo <= 2 }
        else -> profiles
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // App Top Bar
            item {
                SoulmateTopHeader(
                    selectedLanguage = selectedLanguage.nativeName,
                    onLanguageClick = { viewModel.navigateTo(ScreenState.LANGUAGE_SELECT) },
                    onUpgradeClick = { viewModel.selectBottomTab(BottomTab.PREMIUM) },
                    onEditProfileClick = { viewModel.navigateTo(ScreenState.EDIT_PROFILE) },
                    onVerificationClick = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) },
                    trustScore = verification.trustScore,
                    isVerified = verification.isFaceVerified || verification.isGovtIdVerified
                )
            }

            // Quick Profile & Trust Center Hub Card
            item {
                ProfileAndTrustQuickCard(
                    myProfile = myProfile,
                    verification = verification,
                    onEditProfile = { viewModel.navigateTo(ScreenState.EDIT_PROFILE) },
                    onGetVerified = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) },
                    onCreateProfile = { viewModel.navigateTo(ScreenState.PROFILE_CREATION) }
                )
            }

            // Daily Recommendations Banner
            item {
                DailyMatchBanner(onUpgradeClick = { viewModel.selectBottomTab(BottomTab.PREMIUM) })
            }

            // Filter Pills
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterList) { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepBurgundy,
                                selectedLabelColor = PureWhite,
                                containerColor = PureWhite,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) DeepBurgundy else BorderLight
                            ),
                            modifier = Modifier.testTag("filter_$filter")
                        )
                    }
                }
            }

            // Profile Cards
            items(filteredProfiles) { profile ->
                val compatibilityReport = remember(profile.id) {
                    viewModel.getCompatibilityReport(profile)
                }
                ProfileMatchCard(
                    profile = profile,
                    compatibility = compatibilityReport,
                    onViewProfile = { viewModel.viewProfile(profile) },
                    onShortlist = { viewModel.toggleShortlist(profile.id) },
                    onConnect = { viewModel.toggleConnect(profile.id) },
                    onChat = { viewModel.openChat(profile) }
                )
            }

            if (filteredProfiles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No matches found in this category",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Try switching to 'All' or clearing filters.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Mutual Match Celebration Modal
        if (mutualMatch != null) {
            val matchedProf = mutualMatch!!
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xB3000000))
                    .clickable { viewModel.dismissMatchDialog() }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    modifier = Modifier.fillMaxWidth().clickable(enabled = false) {}
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Match",
                            tint = RosePrimary,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "It's a Mutual Match! 💍",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepBurgundy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "You and ${matchedProf.name} have expressed mutual interest. Our Vedic Horoscope matches with 9/10 Poruthams!",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.dismissMatchDialog() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Keep Exploring", fontSize = 13.sp)
                            }
                            Button(
                                onClick = {
                                    viewModel.dismissMatchDialog()
                                    viewModel.openChat(matchedProf)
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Start Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoulmateTopHeader(
    selectedLanguage: String,
    onLanguageClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onVerificationClick: () -> Unit,
    trustScore: Int,
    isVerified: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onEditProfileClick)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PureWhite)
                    .border(1.5.dp, PrimaryEmerald, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_jodii_logo),
                    contentDescription = "Soulmate Matrimony Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Soulmate",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepBurgundy
                    )
                    if (isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = SuccessGreen,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Text(
                    text = "Where Hearts Meet",
                    fontSize = 10.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Trust Shield Badge
            Surface(
                onClick = onVerificationClick,
                shape = RoundedCornerShape(14.dp),
                color = if (isVerified) LightGreen else LightGold,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isVerified) SuccessGreen else GoldAccent),
                modifier = Modifier.testTag("home_trust_badge_pill")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = if (isVerified) Icons.Default.Verified else Icons.Default.Security,
                        contentDescription = "Trust Score",
                        tint = if (isVerified) SuccessGreen else DarkGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$trustScore%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isVerified) SuccessGreen else DarkGold
                    )
                }
            }

            // Edit Profile Pill
            Surface(
                onClick = onEditProfileClick,
                shape = RoundedCornerShape(14.dp),
                color = LightRose,
                modifier = Modifier.testTag("home_edit_profile_pill")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "My Profile",
                        tint = DeepBurgundy,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Biodata",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBurgundy
                    )
                }
            }

            // Premium Membership Pill
            Surface(
                onClick = onUpgradeClick,
                shape = RoundedCornerShape(14.dp),
                color = DeepBurgundy,
                modifier = Modifier.testTag("home_vip_pill")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Premium",
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Premium",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileAndTrustQuickCard(
    myProfile: Profile,
    verification: com.example.model.VerificationStatus,
    onEditProfile: () -> Unit,
    onGetVerified: () -> Unit,
    onCreateProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("profile_and_trust_quick_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onEditProfile)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, DeepBurgundy, CircleShape)
                    ) {
                        AsyncImage(
                            model = myProfile.photoUrls.firstOrNull() ?: "",
                            contentDescription = "My Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = myProfile.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBurgundy
                            )
                            if (verification.isFaceVerified || verification.isGovtIdVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = "${myProfile.age} yrs • ${myProfile.profession.split(" ")[0]} • ${myProfile.city}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Trust Score Pill
                Surface(
                    onClick = onGetVerified,
                    shape = RoundedCornerShape(12.dp),
                    color = if (verification.trustScore >= 95) LightGreen else LightGold,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (verification.trustScore >= 95) SuccessGreen else GoldAccent
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${verification.trustScore}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (verification.trustScore >= 95) SuccessGreen else DarkGold
                        )
                        Text(
                            text = "TRUST SCORE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (verification.trustScore >= 95) SuccessGreen else DarkGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit Biodata Button
                Surface(
                    onClick = onEditProfile,
                    shape = RoundedCornerShape(10.dp),
                    color = LightRose,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_edit_biodata_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = DeepBurgundy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit Biodata",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBurgundy
                        )
                    }
                }

                // Verification Badge / Face Scan Button
                Surface(
                    onClick = onGetVerified,
                    shape = RoundedCornerShape(10.dp),
                    color = if (verification.isFaceVerified && verification.isGovtIdVerified) LightGreen else DeepBurgundy,
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("quick_verify_badge_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (verification.isFaceVerified) Icons.Default.Verified else Icons.Default.Security,
                            contentDescription = null,
                            tint = if (verification.isFaceVerified && verification.isGovtIdVerified) SuccessGreen else GoldAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (verification.isFaceVerified && verification.isGovtIdVerified) "100% Verified 🛡️" else "Get Verified Badge",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (verification.isFaceVerified && verification.isGovtIdVerified) SuccessGreen else PureWhite
                        )
                    }
                }

                // Create Profile Button
                Surface(
                    onClick = onCreateProfile,
                    shape = RoundedCornerShape(10.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .weight(0.8f)
                        .testTag("quick_create_profile_button")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+ New",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyMatchBanner(onUpgradeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onUpgradeClick)
            .testTag("daily_match_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepBurgundy)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✨", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Today's Verified Matches",
                        color = GoldAccent,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Get direct access to verified phone numbers & horoscope porutham reports.",
                    color = PureWhite,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GoldAccent
            ) {
                Text(
                    text = "Upgrade",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkCardSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileMatchCard(
    profile: Profile,
    compatibility: com.example.matchmaking.FullMatchReport? = null,
    onViewProfile: () -> Unit,
    onShortlist: () -> Unit,
    onConnect: () -> Unit,
    onChat: () -> Unit
) {
    var currentPhotoIndex by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable(onClick = onViewProfile)
            .testTag("profile_card_${profile.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Photo Container with Indicators
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFFE0E0E0))
            ) {
                AsyncImage(
                    model = profile.photoUrls.getOrElse(currentPhotoIndex) { profile.photoUrls.first() },
                    contentDescription = profile.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient Overlay at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x99000000)),
                                startY = 400f
                            )
                        )
                )

                // Top Pills (Verified & Compatibility Score)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (profile.verified) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = VerifiedBlue
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = PureWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "100% ID Verified",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Porutham / Match Score Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xCC000000)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Match Score",
                                tint = GoldAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val scoreText = if (compatibility != null) "${compatibility.poruthamCount}/10 Porutham • ${compatibility.overallScore}%" else "${profile.trustScore}% Score"
                            Text(
                                text = scoreText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }
                }

                // Photo Indicators
                if (profile.photoUrls.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        profile.photoUrls.indices.forEach { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == currentPhotoIndex) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(if (index == currentPhotoIndex) GoldAccent else Color(0x88FFFFFF))
                                    .clickable { currentPhotoIndex = index }
                            )
                        }
                    }
                }

                // Name & Age on Photo
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${profile.name}, ${profile.age}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    Text(
                        text = "${profile.religion} • ${profile.caste} | ${profile.height}",
                        fontSize = 13.sp,
                        color = LightGold
                    )
                }
            }

            // Info Details
            Column(modifier = Modifier.padding(16.dp)) {
                // Profession & Education
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BusinessCenter,
                        contentDescription = "Profession",
                        tint = DeepBurgundy,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${profile.profession} at ${profile.company}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Education",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = profile.education,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${profile.city}, ${profile.district} (Native: ${profile.nativePlace})",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Highlight Tags
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HighlightTag(label = "Star: ${profile.starNakshatra}", bgColor = LightGold, textColor = DarkGold)
                    HighlightTag(label = "Income: ${profile.annualIncome}", bgColor = LightGreen, textColor = SuccessGreen)
                    HighlightTag(label = "Jathakam: ${profile.dosham}", bgColor = LightRose, textColor = DeepBurgundy)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shortlist Button
                    IconButton(
                        onClick = onShortlist,
                        modifier = Modifier
                            .size(46.dp)
                            .background(LightRose, CircleShape)
                            .testTag("shortlist_btn_${profile.id}")
                    ) {
                        Icon(
                            imageVector = if (profile.isShortlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Shortlist",
                            tint = RosePrimary
                        )
                    }

                    // Chat Button
                    IconButton(
                        onClick = onChat,
                        modifier = Modifier
                            .size(46.dp)
                            .background(LightBlue, CircleShape)
                            .testTag("chat_btn_${profile.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = VerifiedBlue
                        )
                    }

                    // Connect / Send Interest Button
                    Button(
                        onClick = onConnect,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (profile.isConnected) SuccessGreen else DeepBurgundy,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("connect_btn_${profile.id}")
                    ) {
                        Text(
                            text = if (profile.isConnected) "✓ Interest Sent" else "Send Interest 💍",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HighlightTag(
    label: String,
    bgColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ShortlistedView(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.profiles.collectAsState()
    val shortlistedList = profiles.filter { it.isShortlisted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PureWhite)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Shortlisted",
                tint = RosePrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Shortlisted Profiles (${shortlistedList.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Profiles you saved for review with family",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        if (shortlistedList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Empty",
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Shortlisted Profiles Yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the heart icon on any profile to save it here for family discussion.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.selectBottomTab(BottomTab.DISCOVERY) },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)
                    ) {
                        Text("Explore Matches")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(shortlistedList) { profile ->
                    ProfileMatchCard(
                        profile = profile,
                        onViewProfile = { viewModel.viewProfile(profile) },
                        onShortlist = { viewModel.toggleShortlist(profile.id) },
                        onConnect = { viewModel.toggleConnect(profile.id) },
                        onChat = { viewModel.openChat(profile) }
                    )
                }
            }
        }
    }
}
