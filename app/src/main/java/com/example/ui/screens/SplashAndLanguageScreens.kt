package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppConfig
import com.example.model.Language
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

/**
 * Landing screen — the app's first impression.
 *
 * Clean emerald-brand gradient (matches the JODI SOULMATE emblem colours),
 * the real logo, the brand wordmark and a single clear call-to-action.
 * No decorative background graphics — pure, premium and fast.
 */
@Composable
fun SplashScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.85f) }
    val alphaAnim = remember { Animatable(0f) }

    // Gentle breathing on the logo emblem — subtle, premium
    val infiniteTransition = rememberInfiniteTransition(label = "logo_breath")
    val logoBreath by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(500))
        scale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
    }

    val onGetStarted = {
        viewModel.navigateTo(ScreenState.ONBOARDING)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF064E3B),  // Deep brand emerald (emblem border green)
                        PrimaryEmerald,     // Rich Emerald Green 0xFF059669
                        PrimaryTeal         // Auspicious Teal Cyan 0xFF0D9488
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .alpha(alphaAnim.value)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Real JODI SOULMATE emblem in a crisp white disc
            Box(
                modifier = Modifier
                    .scale(scale.value * logoBreath)
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(PureWhite)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_jodii_logo),
                    contentDescription = "JODI Soulmate Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // App Title & Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "JODI SOULMATE",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = PureWhite,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "PREMIUM MATRIMONY",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGold,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Where Hearts Meet & Destinies Align",
                    fontSize = 14.sp,
                    color = PureWhite.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Verified Badge Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0x2EFFFFFF), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Verified",
                        tint = LightGold,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "100% Verified Profiles & Direct Contacts",
                        fontSize = 12.sp,
                        color = PureWhite,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Get Started Button
                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhite,
                        contentColor = Color(0xFF047857)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("splash_get_started_button")
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGreenBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(LightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = PrimaryEmerald,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Choose Your Language",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Select the language you are comfortable with",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(AppConfig.languages) { lang ->
                val isSelected = lang.code == selectedLanguage.code
                LanguageCard(
                    language = lang,
                    isSelected = isSelected,
                    onClick = {
                        viewModel.selectLanguage(lang)
                        viewModel.showToast("${lang.nativeName} selected")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = { viewModel.navigateTo(ScreenState.LOGIN) },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryEmerald,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("language_continue_button")
        ) {
            Text(
                text = "Continue in ${selectedLanguage.nativeName}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LanguageCard(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("lang_${language.code}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LightTeal else PureWhite
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, PrimaryEmerald)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = language.flag,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = language.nativeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PrimaryEmerald else TextPrimary
                )
                Text(
                    text = language.name,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = PrimaryEmerald,
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}
