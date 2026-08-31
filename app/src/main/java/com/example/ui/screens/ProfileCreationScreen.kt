package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DeepBurgundy
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGold
import com.example.ui.theme.LightGreen
import com.example.ui.theme.LightRose
import com.example.ui.theme.PureWhite
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
fun ProfileCreationScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.profileCreationDraft.collectAsState()
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 5

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Create Matrimony Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "Step $currentStep of $totalSteps: ${getStepTitle(currentStep)}",
                            fontSize = 12.sp,
                            color = LightGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentStep > 1) {
                                currentStep -= 1
                            } else {
                                viewModel.navigateTo(ScreenState.MAIN_APP)
                            }
                        },
                        modifier = Modifier.testTag("profile_creation_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PureWhite
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Skip",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PureWhite,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                viewModel.navigateTo(ScreenState.MAIN_APP)
                            }
                            .testTag("profile_creation_skip_button")
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBurgundy
                )
            )
        },
        bottomBar = {
            Surface(
                color = PureWhite,
                shadowElevation = 10.dp,
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep -= 1 },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, DeepBurgundy),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepBurgundy),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("profile_prev_step_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Previous", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Button(
                        onClick = {
                            if (currentStep < totalSteps) {
                                currentStep += 1
                            } else {
                                viewModel.completeProfileCreation()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepBurgundy,
                            contentColor = PureWhite
                        ),
                        modifier = Modifier
                            .weight(if (currentStep > 1) 1.5f else 1f)
                            .height(50.dp)
                            .testTag("profile_next_step_button")
                    ) {
                        Text(
                            text = if (currentStep == totalSteps) "Complete & Save 🎉" else "Save & Continue",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (currentStep == totalSteps) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
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
            // Progress Bar
            LinearProgressIndicator(
                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = GoldAccent,
                trackColor = BorderLight,
            )

            // Step Indicator Header
            StepProgressHeader(currentStep = currentStep, totalSteps = totalSteps)

            // Animated Step Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ProfileCreationStepTransition",
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { step ->
                when (step) {
                    1 -> Step1BasicDetails(viewModel = viewModel)
                    2 -> Step2ReligionHoroscope(viewModel = viewModel)
                    3 -> Step3EducationCareer(viewModel = viewModel)
                    4 -> Step4LocationFamily(viewModel = viewModel)
                    5 -> Step5PartnerPreferences(viewModel = viewModel)
                }
            }
        }
    }
}

private fun getStepTitle(step: Int): String {
    return when (step) {
        1 -> "Basic Details"
        2 -> "Religion & Horoscope"
        3 -> "Education & Career"
        4 -> "Location & Family"
        5 -> "Partner Preferences"
        else -> ""
    }
}

@Composable
private fun StepProgressHeader(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val steps = listOf(
            "Basic",
            "Horoscope",
            "Career",
            "Family",
            "Partner"
        )
        steps.forEachIndexed { index, title ->
            val stepNumber = index + 1
            val isCompleted = stepNumber < currentStep
            val isCurrent = stepNumber == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> SuccessGreen
                                isCurrent -> DeepBurgundy
                                else -> BorderLight
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "$stepNumber",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) PureWhite else TextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) DeepBurgundy else TextSecondary
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 1: BASIC DETAILS
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step1BasicDetails(viewModel: AppViewModel) {
    val draft by viewModel.profileCreationDraft.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StepHeaderCard(
                icon = Icons.Default.Person,
                title = "Let's start with your basic profile",
                subtitle = "This helps us introduce you to compatible matches and family members."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Profile Created For
        item {
            SectionTitle(title = "This profile is for")
            val options = listOf("Self", "Son", "Daughter", "Brother", "Sister", "Friend / Relative")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    ChoiceChip(
                        text = option,
                        isSelected = draft.profileFor == option,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(profileFor = option) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Full Name
        item {
            CustomInputField(
                label = "Full Name",
                placeholder = "e.g. Karthik Nair",
                value = draft.name,
                onValueChange = { name -> viewModel.updateProfileCreationDraft { it.copy(name = name) } },
                testTag = "draft_name_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Gender
        item {
            SectionTitle(title = "Gender")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Male", "Female").forEach { g ->
                    ChoiceChip(
                        text = g,
                        isSelected = draft.gender == g,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(gender = g) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Date of Birth & Age
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomInputField(
                        label = "Date of Birth",
                        placeholder = "DD/MM/YYYY",
                        value = draft.dob,
                        onValueChange = { dob -> viewModel.updateProfileCreationDraft { it.copy(dob = dob) } },
                        testTag = "draft_dob_input"
                    )
                }
                Column(modifier = Modifier.weight(0.6f)) {
                    CustomInputField(
                        label = "Age (Yrs)",
                        placeholder = "27",
                        value = "${draft.age}",
                        onValueChange = { ageStr ->
                            ageStr.toIntOrNull()?.let { age ->
                                viewModel.updateProfileCreationDraft { it.copy(age = age) }
                            }
                        },
                        testTag = "draft_age_input"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Height
        item {
            SectionTitle(title = "Height")
            val heights = listOf("5 ft 2 in (157 cm)", "5 ft 4 in (163 cm)", "5 ft 7 in (170 cm)", "5 ft 9 in (175 cm)", "5 ft 11 in (180 cm)", "6 ft 0 in (183 cm)")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                heights.forEach { h ->
                    ChoiceChip(
                        text = h,
                        isSelected = draft.height == h,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(height = h) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Marital Status
        item {
            SectionTitle(title = "Marital Status")
            val statuses = listOf("Never Married", "Divorced", "Awaiting Divorce", "Widowed")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statuses.forEach { st ->
                    ChoiceChip(
                        text = st,
                        isSelected = draft.maritalStatus == st,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(maritalStatus = st) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Mother Tongue
        item {
            SectionTitle(title = "Mother Tongue")
            val tongues = listOf("Malayalam", "Tamil", "Hindi", "Kannada", "Telugu", "Marathi", "Bengali")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tongues.forEach { t ->
                    ChoiceChip(
                        text = t,
                        isSelected = draft.motherTongue == t,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(motherTongue = t) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// STEP 2: RELIGION & HOROSCOPE
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step2ReligionHoroscope(viewModel: AppViewModel) {
    val draft by viewModel.profileCreationDraft.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StepHeaderCard(
                icon = Icons.Default.MenuBook,
                title = "Religious & Horoscope Details",
                subtitle = "Kerala astrological porutham matching is supported for 10-point compatibility."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Religion
        item {
            SectionTitle(title = "Religion")
            val religions = listOf("Hindu", "Christian", "Muslim", "Sikh", "Jain", "Inter-Religion")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                religions.forEach { r ->
                    ChoiceChip(
                        text = r,
                        isSelected = draft.religion == r,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(religion = r) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Caste / Community
        item {
            CustomInputField(
                label = "Caste / Sub-Caste",
                placeholder = "e.g. Nair, Ezhava, Brahmin, RC Catholic, Sunni",
                value = draft.caste,
                onValueChange = { c -> viewModel.updateProfileCreationDraft { it.copy(caste = c) } },
                testTag = "draft_caste_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Gothram
        item {
            CustomInputField(
                label = "Gothram (Optional)",
                placeholder = "e.g. Kashyapa, Bharadwaja, Vishwamitra",
                value = draft.gothram,
                onValueChange = { g -> viewModel.updateProfileCreationDraft { it.copy(gothram = g) } },
                testTag = "draft_gothram_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Star / Nakshatram
        item {
            SectionTitle(title = "Star / Nakshatram")
            val stars = listOf(
                "Ashwini (Aswathy)", "Bharani", "Krittika (Karthika)", "Rohini",
                "Mrigashirsha (Makayiram)", "Ardra (Thiruvathira)", "Punarvasu (Punartham)",
                "Pushya (Pooyam)", "Ashlesha (Ayilyam)", "Magha (Makam)", "Uttara (Uthram)",
                "Hasta (Atham)", "Chitra (Chithira)", "Swati (Chothi)", "Vishakha (Visakham)",
                "Anuradha (Anizham)", "Jyeshtha (Thrikketta)", "Mula (Moolam)", "Purva Ashadha (Pooradam)",
                "Uttara Ashadha (Uthradam)", "Shravana (Thiruvonam)", "Dhanishta (Avittam)",
                "Shatabhisha (Chathayam)", "Purva Bhadrapada (Poororattathi)", "Uttara Bhadrapada (Uthrattathi)", "Revati"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stars.take(12).forEach { s ->
                    ChoiceChip(
                        text = s,
                        isSelected = draft.starNakshatra.contains(s.split(" ")[0]),
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(starNakshatra = s) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Raasi / Moon Sign
        item {
            SectionTitle(title = "Raasi / Moon Sign")
            val rasis = listOf(
                "Mesham (Aries)", "Rishabham (Taurus)", "Mithunam (Gemini)",
                "Karkidakam (Cancer)", "Chingam (Leo)", "Kanni (Virgo)",
                "Thulam (Libra)", "Vrischikam (Scorpio)", "Dhanu (Sagittarius)",
                "Makaram (Capricorn)", "Kumbham (Aquarius)", "Meenam (Pisces)"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rasis.forEach { r ->
                    ChoiceChip(
                        text = r,
                        isSelected = draft.rasi == r,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(rasi = r) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Dosham
        item {
            SectionTitle(title = "Dosham / Chevva Dosham")
            val doshams = listOf("No Dosham", "Chevva Dosham (Kuja)", "Sarpa / Rahu Dosham", "Don't Know")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                doshams.forEach { d ->
                    ChoiceChip(
                        text = d,
                        isSelected = draft.dosham == d,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(dosham = d) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// STEP 3: EDUCATION & CAREER
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step3EducationCareer(viewModel: AppViewModel) {
    val draft by viewModel.profileCreationDraft.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StepHeaderCard(
                icon = Icons.Default.School,
                title = "Education & Career Details",
                subtitle = "Profiles with verified degrees and professions receive 4x more attention."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Highest Education
        item {
            SectionTitle(title = "Highest Education Qualification")
            val educations = listOf(
                "B.Tech / B.E.", "M.Tech / M.E.", "MBBS / MD / MS", "MBA / PGDM",
                "MCA / M.Sc IT", "Chartered Accountant (CA)", "B.Sc / B.Com",
                "M.Sc / M.Com / M.A.", "Ph.D / Doctorate", "Diploma"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                educations.forEach { ed ->
                    ChoiceChip(
                        text = ed,
                        isSelected = draft.education.contains(ed.split(" ")[0]),
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(education = ed) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // College / University
        item {
            CustomInputField(
                label = "College / University Name",
                placeholder = "e.g. NIT Calicut, CET Trivandrum, IIM Kozhikode",
                value = draft.college,
                onValueChange = { c -> viewModel.updateProfileCreationDraft { it.copy(college = c) } },
                testTag = "draft_college_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Profession
        item {
            CustomInputField(
                label = "Profession / Job Role",
                placeholder = "e.g. Senior Software Engineer, Doctor, Bank Manager",
                value = draft.profession,
                onValueChange = { p -> viewModel.updateProfileCreationDraft { it.copy(profession = p) } },
                testTag = "draft_profession_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Company
        item {
            CustomInputField(
                label = "Company / Employer",
                placeholder = "e.g. UST Global, Infosys, Govt of Kerala, Apollo Hospitals",
                value = draft.company,
                onValueChange = { comp -> viewModel.updateProfileCreationDraft { it.copy(company = comp) } },
                testTag = "draft_company_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Annual Income
        item {
            SectionTitle(title = "Annual Income")
            val incomes = listOf(
                "₹ 3 - 5 Lakhs", "₹ 6 - 10 Lakhs", "₹ 10 - 15 Lakhs",
                "₹ 15 - 25 Lakhs", "₹ 25 - 40 Lakhs", "₹ 40 Lakhs +", "Prefer not to say"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                incomes.forEach { inc ->
                    ChoiceChip(
                        text = inc,
                        isSelected = draft.annualIncome == inc,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(annualIncome = inc) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// STEP 4: LOCATION & FAMILY
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step4LocationFamily(viewModel: AppViewModel) {
    val draft by viewModel.profileCreationDraft.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StepHeaderCard(
                icon = Icons.Default.Home,
                title = "Location & Family Background",
                subtitle = "Indian matrimony values family harmony. Share your family roots."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Living City & District
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomInputField(
                        label = "Living City",
                        placeholder = "e.g. Kochi, Bangalore",
                        value = draft.city,
                        onValueChange = { c -> viewModel.updateProfileCreationDraft { it.copy(city = c) } },
                        testTag = "draft_city_input"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    CustomInputField(
                        label = "Native District",
                        placeholder = "e.g. Ernakulam, Thrissur",
                        value = draft.district,
                        onValueChange = { d -> viewModel.updateProfileCreationDraft { it.copy(district = d) } },
                        testTag = "draft_district_input"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Family Type
        item {
            SectionTitle(title = "Family Type")
            val types = listOf("Nuclear Family", "Joint Family")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                types.forEach { ft ->
                    ChoiceChip(
                        text = ft,
                        isSelected = draft.familyType == ft,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(familyType = ft) } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Father's Details
        item {
            CustomInputField(
                label = "Father's Profession / Details",
                placeholder = "e.g. Retired Govt Officer (KSEB), Business",
                value = draft.familyFather,
                onValueChange = { f -> viewModel.updateProfileCreationDraft { it.copy(familyFather = f) } },
                testTag = "draft_father_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Mother's Details
        item {
            CustomInputField(
                label = "Mother's Profession / Details",
                placeholder = "e.g. Homemaker, Teacher, Bank Officer",
                value = draft.familyMother,
                onValueChange = { m -> viewModel.updateProfileCreationDraft { it.copy(familyMother = m) } },
                testTag = "draft_mother_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Siblings
        item {
            CustomInputField(
                label = "Siblings Details",
                placeholder = "e.g. 1 Elder Sister (Married), 1 Younger Brother",
                value = draft.familySiblings,
                onValueChange = { s -> viewModel.updateProfileCreationDraft { it.copy(familySiblings = s) } },
                testTag = "draft_siblings_input"
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// STEP 5: PARTNER PREFERENCES & BIO
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step5PartnerPreferences(viewModel: AppViewModel) {
    val draft by viewModel.profileCreationDraft.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            StepHeaderCard(
                icon = Icons.Default.Favorite,
                title = "Partner Preferences & Bio",
                subtitle = "Tell us what you are looking for in your ideal soulmate."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Preferred Age Range
        item {
            SectionTitle(title = "Preferred Age Range")
            val ages = listOf("21 - 25 Yrs", "23 - 27 Yrs", "25 - 29 Yrs", "28 - 32 Yrs", "Open to All")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ages.forEach { a ->
                    ChoiceChip(
                        text = a,
                        isSelected = draft.partnerAgeRange == a,
                        onClick = { viewModel.updateProfileCreationDraft { it.copy(partnerAgeRange = a) } }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Preferred Education
        item {
            CustomInputField(
                label = "Preferred Education / Profession",
                placeholder = "e.g. B.Tech / MBBS / CA / Post Graduate",
                value = draft.partnerEducation,
                onValueChange = { pe -> viewModel.updateProfileCreationDraft { it.copy(partnerEducation = pe) } },
                testTag = "draft_partner_edu_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Preferred Location
        item {
            CustomInputField(
                label = "Preferred Location / Country",
                placeholder = "e.g. Kerala, Bangalore, UAE / GCC, Any",
                value = draft.partnerLocation,
                onValueChange = { pl -> viewModel.updateProfileCreationDraft { it.copy(partnerLocation = pl) } },
                testTag = "draft_partner_loc_input"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // About Myself / Bio
        item {
            SectionTitle(title = "About Myself (Bio)")
            OutlinedTextField(
                value = draft.bio,
                onValueChange = { b -> viewModel.updateProfileCreationDraft { it.copy(bio = b) } },
                placeholder = {
                    Text(
                        text = "Write a few words about your values, personality, passions, and the kind of partner you seek...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("draft_bio_input"),
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
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Quick Suggestion Chips for Bio
        item {
            Text(
                text = "✨ Quick Bio Suggestions:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )
            Spacer(modifier = Modifier.height(6.dp))
            val bioSuggestions = listOf(
                "Family-oriented IT professional looking for a caring, understanding life partner.",
                "Passionate about travel, culture and music. Seeking a progressive companion with good values.",
                "Calm and optimistic doctor looking for a like-minded soulmate to share life's milestones."
            )
            bioSuggestions.forEach { suggestion ->
                Surface(
                    color = LightRose,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, RoseSoft),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewModel.updateProfileCreationDraft { it.copy(bio = suggestion) }
                        }
                ) {
                    Text(
                        text = "“$suggestion”",
                        fontSize = 12.sp,
                        color = DeepBurgundy,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER UI COMPONENTS
// -------------------------------------------------------------
@Composable
private fun StepHeaderCard(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(LightRose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepBurgundy,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBurgundy
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = DeepBurgundy,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ChoiceChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) DeepBurgundy else PureWhite,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) DeepBurgundy else BorderLight
        ),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PureWhite else TextPrimary
            )
        }
    }
}

@Composable
private fun CustomInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    testTag: String
) {
    Column {
        SectionTitle(title = label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = TextMuted) },
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
