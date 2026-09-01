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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

/**
 * Advanced Search & Filter page — the deep criteria search promised by the
 * product documentation: Age, Religion, Caste, Nakshatra, Location,
 * Education, Income, Marital status, Diet & Verified-only.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchFilterScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val filters by viewModel.searchFilters.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()

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
            IconButton(onClick = { viewModel.navigateTo(ScreenState.MAIN_APP) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Advanced Search",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { viewModel.resetSearchFilters() }) {
                Text("Reset", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Keyword search
            OutlinedTextField(
                value = filters.query,
                onValueChange = { value ->
                    viewModel.updateSearchFilters { it.copy(query = value) }
                },
                placeholder = {
                    Text("Name, city, profession or caste...", color = TextMuted, fontSize = 13.sp)
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderLight,
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    cursorColor = PrimaryBlue
                ),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Age range
            FilterSectionCard(title = "Age Range") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SmallNumberField(
                        value = filters.minAge,
                        label = "Min",
                        onValue = { v ->
                            viewModel.updateSearchFilters { f ->
                                f.copy(minAge = v.coerceIn(18, 70))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Text("to", fontSize = 13.sp, color = TextMuted)
                    SmallNumberField(
                        value = filters.maxAge,
                        label = "Max",
                        onValue = { v ->
                            viewModel.updateSearchFilters { f ->
                                f.copy(maxAge = v.coerceIn(18, 70))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            FilterSectionCard(title = "Religion") {
                ChipRow(
                    options = listOf("Any", "Hindu", "Christian", "Muslim"),
                    selected = filters.religion,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(religion = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Caste / Community") {
                ChipRow(
                    options = listOf(
                        "Any", "Nair", "Brahmin (Iyer)", "Syrian Catholic (RC)", "Ezhava", "Sunni"
                    ),
                    selected = filters.caste,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(caste = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Star / Nakshatra") {
                ChipRow(
                    options = listOf("Any", "Rohini", "Ashwathi", "Makayiram"),
                    selected = filters.nakshatra,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(nakshatra = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Location") {
                ChipRow(
                    options = listOf(
                        "Any", "Kochi", "Ernakulam", "Kozhikode", "Trivandrum", "Malappuram"
                    ),
                    selected = filters.city,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(city = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Education") {
                ChipRow(
                    options = listOf("Any", "B.Tech", "MBBS", "MBA", "CA", "M.Arch"),
                    selected = filters.education,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(education = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Annual Income") {
                ChipRow(
                    options = listOf(
                        "Any", "₹ 10 - 14", "₹ 14 - 18", "₹ 16 - 20", "₹ 24 - 30", "₹ 35 - 45"
                    ),
                    selected = filters.income,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(income = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Marital Status") {
                ChipRow(
                    options = listOf("Any", "Never Married", "Divorced", "Widowed"),
                    selected = filters.maritalStatus,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(maritalStatus = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Diet") {
                ChipRow(
                    options = listOf("Any", "Vegetarian", "Non-Vegetarian", "Eggetarian"),
                    selected = filters.diet,
                    onSelect = { selected ->
                        viewModel.updateSearchFilters { it.copy(diet = selected) }
                    }
                )
            }

            FilterSectionCard(title = "Trust") {
                Surface(
                    onClick = {
                        viewModel.updateSearchFilters { it.copy(verifiedOnly = !it.verifiedOnly) }
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (filters.verifiedOnly) PrimaryEmerald else PureWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, if (filters.verifiedOnly) PrimaryEmerald else BorderLight
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = if (filters.verifiedOnly) PureWhite else PrimaryEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100% Verified Profiles Only",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (filters.verifiedOnly) PureWhite else TextPrimary
                        )
                    }
                }
            }

            // Search button
            Button(
                onClick = { viewModel.applySearchFilters() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search Matches", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Results
            if (hasSearched) {
                Text(
                    text = "${results.size} match${if (results.size == 1) "" else "es"} found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 6.dp)
                )
                if (results.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "No profiles match these filters",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                "Try widening the age range or clearing a criterion.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    results.forEach { profile ->
                        SearchResultCard(
                            profile = profile,
                            compatibility = viewModel.getCompatibilityReport(profile).overallScore,
                            onView = { viewModel.viewProfile(profile) },
                            onChat = { viewModel.openChat(profile) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FilterSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Surface(
                onClick = { onSelect(option) },
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) PrimaryBlue else WarmBackground,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, if (isSelected) PrimaryBlue else BorderLight
                )
            ) {
                Text(
                    text = option,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) PureWhite else TextSecondary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
private fun SmallNumberField(
    value: Int,
    label: String,
    onValue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { text -> text.toIntOrNull()?.let { onValue(it) } },
        label = { Text(label, fontSize = 11.sp) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderLight,
            focusedContainerColor = PureWhite,
            unfocusedContainerColor = PureWhite,
            cursorColor = PrimaryBlue
        ),
        modifier = modifier
    )
}

@Composable
private fun SearchResultCard(
    profile: Profile,
    compatibility: Int,
    onView: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onView)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${profile.name}, ${profile.age}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (profile.verified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = VerifiedBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = "${profile.profession} • ${profile.city}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${profile.caste} • ${profile.starNakshatra}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (compatibility >= 85) LightGreen else LightGold
                ) {
                    Text(
                        text = "$compatibility%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (compatibility >= 85) SuccessGreen else DarkGold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onChat,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }
                Button(
                    onClick = onView,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue, contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Biodata", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
