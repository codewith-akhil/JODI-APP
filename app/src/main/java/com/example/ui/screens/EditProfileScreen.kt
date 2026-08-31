package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
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
fun EditProfileScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val myProfile by viewModel.myProfile.collectAsState()
    val verification by viewModel.verificationStatus.collectAsState()
    val userPhotos by viewModel.userPhotos.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Basic & Bio",
        "Horoscope",
        "Career & Edu",
        "Family Roots",
        "Partner Wishes"
    )

    // Local mutable state for editing
    var name by remember(myProfile) { mutableStateOf(myProfile.name) }
    var age by remember(myProfile) { mutableIntStateOf(myProfile.age) }
    var height by remember(myProfile) { mutableStateOf(myProfile.height) }
    var maritalStatus by remember(myProfile) { mutableStateOf(myProfile.maritalStatus) }
    var motherTongue by remember(myProfile) { mutableStateOf(myProfile.motherTongue) }
    var diet by remember(myProfile) { mutableStateOf(myProfile.diet) }
    var drinking by remember(myProfile) { mutableStateOf(myProfile.drinking) }
    var smoking by remember(myProfile) { mutableStateOf(myProfile.smoking) }
    var bio by remember(myProfile) { mutableStateOf(myProfile.bio) }

    var religion by remember(myProfile) { mutableStateOf(myProfile.religion) }
    var caste by remember(myProfile) { mutableStateOf(myProfile.caste) }
    var gothram by remember(myProfile) { mutableStateOf(myProfile.gothram) }
    var starNakshatra by remember(myProfile) { mutableStateOf(myProfile.starNakshatra) }
    var rasi by remember(myProfile) { mutableStateOf(myProfile.rasi) }
    var dosham by remember(myProfile) { mutableStateOf(myProfile.dosham) }

    var education by remember(myProfile) { mutableStateOf(myProfile.education) }
    var college by remember(myProfile) { mutableStateOf(myProfile.college) }
    var profession by remember(myProfile) { mutableStateOf(myProfile.profession) }
    var company by remember(myProfile) { mutableStateOf(myProfile.company) }
    var annualIncome by remember(myProfile) { mutableStateOf(myProfile.annualIncome) }
    var city by remember(myProfile) { mutableStateOf(myProfile.city) }
    var district by remember(myProfile) { mutableStateOf(myProfile.district) }

    var familyFather by remember(myProfile) { mutableStateOf(myProfile.familyFather) }
    var familyMother by remember(myProfile) { mutableStateOf(myProfile.familyMother) }
    var familySiblings by remember(myProfile) { mutableStateOf(myProfile.familySiblings) }
    var familyType by remember(myProfile) { mutableStateOf(myProfile.familyType) }
    var nativePlace by remember(myProfile) { mutableStateOf(myProfile.nativePlace) }

    var partnerAgeRange by remember(myProfile) { mutableStateOf(myProfile.partnerAgeRange) }
    var partnerHeightRange by remember(myProfile) { mutableStateOf(myProfile.partnerHeightRange) }
    var partnerEducation by remember(myProfile) { mutableStateOf(myProfile.partnerEducation) }
    var partnerLocation by remember(myProfile) { mutableStateOf(myProfile.partnerLocation) }
    var partnerCaste by remember(myProfile) { mutableStateOf(myProfile.partnerCaste) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Edit My Biodata",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "Keep your details updated for better matchmaking",
                            fontSize = 11.sp,
                            color = LightGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) },
                        modifier = Modifier.testTag("edit_profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PureWhite
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.viewProfile(myProfile)
                        },
                        modifier = Modifier.testTag("preview_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Preview",
                            tint = PureWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBurgundy)
            )
        },
        bottomBar = {
            Surface(
                color = PureWhite,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(text = "Cancel", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            viewModel.updateMyProfile { current ->
                                current.copy(
                                    name = name,
                                    age = age,
                                    height = height,
                                    maritalStatus = maritalStatus,
                                    motherTongue = motherTongue,
                                    diet = diet,
                                    drinking = drinking,
                                    smoking = smoking,
                                    bio = bio,
                                    religion = religion,
                                    caste = caste,
                                    gothram = gothram,
                                    starNakshatra = starNakshatra,
                                    rasi = rasi,
                                    dosham = dosham,
                                    education = education,
                                    college = college,
                                    profession = profession,
                                    company = company,
                                    annualIncome = annualIncome,
                                    city = city,
                                    district = district,
                                    familyFather = familyFather,
                                    familyMother = familyMother,
                                    familySiblings = familySiblings,
                                    familyType = familyType,
                                    nativePlace = nativePlace,
                                    partnerAgeRange = partnerAgeRange,
                                    partnerHeightRange = partnerHeightRange,
                                    partnerEducation = partnerEducation,
                                    partnerLocation = partnerLocation,
                                    partnerCaste = partnerCaste
                                )
                            }
                            viewModel.navigateTo(ScreenState.MAIN_APP)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                            .testTag("save_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Save Changes", fontWeight = FontWeight.Bold, color = PureWhite)
                    }
                }
            }
        },
        containerColor = WarmBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Profile Card Preview Header
            ProfilePhotoMiniHeader(
                profile = myProfile,
                verification = verification,
                onManagePhotos = { viewModel.navigateTo(ScreenState.PHOTO_MANAGER) },
                onGetVerified = { viewModel.navigateTo(ScreenState.VERIFICATION_CENTER) }
            )

            // Scrollable Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = PureWhite,
                contentColor = DeepBurgundy,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = DeepBurgundy,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) DeepBurgundy else TextSecondary
                            )
                        }
                    )
                }
            }

            // Tab Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Basic & Bio
                        item {
                            EditField(label = "Full Name", value = name, onValueChange = { name = it }, testTag = "edit_name")
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    EditField(
                                        label = "Age (Years)",
                                        value = "$age",
                                        onValueChange = { it.toIntOrNull()?.let { a -> age = a } },
                                        testTag = "edit_age"
                                    )
                                }
                                Column(modifier = Modifier.weight(1.5f)) {
                                    EditField(label = "Height", value = height, onValueChange = { height = it }, testTag = "edit_height")
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Marital Status", value = maritalStatus, onValueChange = { maritalStatus = it }, testTag = "edit_marital")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Mother Tongue", value = motherTongue, onValueChange = { motherTongue = it }, testTag = "edit_tongue")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Diet Preference", value = diet, onValueChange = { diet = it }, testTag = "edit_diet")
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    EditField(label = "Drinking", value = drinking, onValueChange = { drinking = it }, testTag = "edit_drinking")
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    EditField(label = "Smoking", value = smoking, onValueChange = { smoking = it }, testTag = "edit_smoking")
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "About Myself (Bio)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBurgundy,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .testTag("edit_bio"),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary),
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
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    1 -> {
                        // Horoscope & Religion
                        item {
                            EditField(label = "Religion", value = religion, onValueChange = { religion = it }, testTag = "edit_religion")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Caste / Sub-Caste", value = caste, onValueChange = { caste = it }, testTag = "edit_caste")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Gothram", value = gothram, onValueChange = { gothram = it }, testTag = "edit_gothram")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Nakshatram (Star)", value = starNakshatra, onValueChange = { starNakshatra = it }, testTag = "edit_star")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Raasi (Moon Sign)", value = rasi, onValueChange = { rasi = it }, testTag = "edit_rasi")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Dosham / Chevva Dosham", value = dosham, onValueChange = { dosham = it }, testTag = "edit_dosham")
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    2 -> {
                        // Career & Education
                        item {
                            EditField(label = "Highest Education", value = education, onValueChange = { education = it }, testTag = "edit_edu")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "College / University", value = college, onValueChange = { college = it }, testTag = "edit_college")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Profession / Role", value = profession, onValueChange = { profession = it }, testTag = "edit_profession")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Company / Organization", value = company, onValueChange = { company = it }, testTag = "edit_company")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Annual Income", value = annualIncome, onValueChange = { annualIncome = it }, testTag = "edit_income")
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    EditField(label = "Work City", value = city, onValueChange = { city = it }, testTag = "edit_city")
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    EditField(label = "District", value = district, onValueChange = { district = it }, testTag = "edit_district")
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    3 -> {
                        // Family Roots
                        item {
                            EditField(label = "Family Type", value = familyType, onValueChange = { familyType = it }, testTag = "edit_family_type")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Native Place / Ancestral Home", value = nativePlace, onValueChange = { nativePlace = it }, testTag = "edit_native")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Father's Details", value = familyFather, onValueChange = { familyFather = it }, testTag = "edit_father")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Mother's Details", value = familyMother, onValueChange = { familyMother = it }, testTag = "edit_mother")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Siblings Details", value = familySiblings, onValueChange = { familySiblings = it }, testTag = "edit_siblings")
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    4 -> {
                        // Partner Preferences
                        item {
                            EditField(label = "Preferred Age Range", value = partnerAgeRange, onValueChange = { partnerAgeRange = it }, testTag = "edit_p_age")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Preferred Height Range", value = partnerHeightRange, onValueChange = { partnerHeightRange = it }, testTag = "edit_p_height")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Preferred Education", value = partnerEducation, onValueChange = { partnerEducation = it }, testTag = "edit_p_edu")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Preferred Location", value = partnerLocation, onValueChange = { partnerLocation = it }, testTag = "edit_p_loc")
                            Spacer(modifier = Modifier.height(14.dp))
                            EditField(label = "Preferred Caste / Community", value = partnerCaste, onValueChange = { partnerCaste = it }, testTag = "edit_p_caste")
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfilePhotoMiniHeader(
    profile: Profile,
    verification: com.example.model.VerificationStatus,
    onManagePhotos: () -> Unit,
    onGetVerified: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onManagePhotos)
            ) {
                AsyncImage(
                    model = profile.photoUrls.firstOrNull() ?: "",
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Edit photo",
                        tint = PureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.name,
                        fontSize = 16.sp,
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
                    text = "${profile.age} yrs • ${profile.city}, ${profile.district}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = LightRose,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable(onClick = onManagePhotos)
                    ) {
                        Text(
                            text = "📷 Photos (${profile.photoUrls.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBurgundy,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = if (verification.trustScore >= 90) LightGreen else LightGold,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable(onClick = onGetVerified)
                    ) {
                        Text(
                            text = "🛡️ Trust: ${verification.trustScore}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (verification.trustScore >= 90) SuccessGreen else DarkCardSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBurgundy,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold),
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
}
