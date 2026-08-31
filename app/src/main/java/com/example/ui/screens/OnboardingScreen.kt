package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import kotlinx.coroutines.launch

/**
 * First-launch onboarding carousel — three value-proposition slides
 * shown after the splash, before language selection.
 */
@Composable
fun OnboardingScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val slides = listOf(
        OnboardingSlide(
            icon = Icons.Default.Favorite,
            title = "Find Your Life Partner",
            body = "Browse thousands of verified matrimony profiles from your community, with families involved every step of the way — the traditional way, modernised.",
            imageRes = R.drawable.jodii_round_logo_1788189533916
        ),
        OnboardingSlide(
            icon = Icons.Default.Security,
            title = "100% Verified & Safe",
            body = "AI face liveness checks, DigiLocker government ID verification and Trust Scores keep fake profiles out. Your number stays private until you choose to share.",
            imageRes = R.drawable.jodii_round_logo_1788189533916
        ),
        OnboardingSlide(
            icon = Icons.Default.Stars,
            title = "Vedic Horoscope Matching",
            body = "Our engine computes all 10 sacred Poruthams and 36 Guna points instantly, so your families can match horoscopes with confidence.",
            imageRes = R.drawable.jodii_round_logo_1788189533916
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightGreenBackground, LightBlueBackground, PureWhite)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        // Skip
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { viewModel.navigateTo(ScreenState.LANGUAGE_SELECT) }) {
                Text(
                    "Skip",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingSlideView(slide = slides[page])
        }

        // Dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            slides.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(if (index == pagerState.currentPage) 11.dp else 8.dp)
                        .background(
                            if (index == pagerState.currentPage) PrimaryBlue
                            else PrimaryBlue.copy(alpha = 0.25f),
                            CircleShape
                        )
                )
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < slides.lastIndex) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    viewModel.navigateTo(ScreenState.LANGUAGE_SELECT)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = if (pagerState.currentPage < slides.lastIndex) "Next" else "Choose Language",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class OnboardingSlide(
    val icon: ImageVector,
    val title: String,
    val body: String,
    val imageRes: Int
)

@Composable
private fun OnboardingSlideView(slide: OnboardingSlide) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(LightGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(PureWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = slide.imageRes),
                    contentDescription = slide.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(56.dp)
                    .background(GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = slide.icon,
                    contentDescription = null,
                    tint = PureWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = slide.title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = slide.body,
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
