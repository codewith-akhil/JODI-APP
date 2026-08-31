package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchmaking.MatchmakingEngine
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import com.example.viewmodel.BottomTab

/**
 * Full Horoscope / Jathakam Match Report — complete 10-Porutham & 36-Guna
 * breakdown with summary verdict and a share/export action.
 */
@Composable
fun HoroscopeReportScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val selectedProfile by viewModel.selectedProfile.collectAsState()
    val myProfile by viewModel.myProfile.collectAsState()
    val context = LocalContext.current

    val partner = selectedProfile ?: return
    val report = MatchmakingEngine.calculateCompatibility(myProfile, partner)

    val verdict = when {
        report.overallScore >= 90 -> "EXCELLENT MATCH"
        report.overallScore >= 80 -> "VERY GOOD MATCH"
        report.overallScore >= 65 -> "GOOD MATCH"
        else -> "MODERATE MATCH"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
    ) {
        // Hero header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(colors = listOf(PrimaryTeal, PrimaryBlue))
                )
                .padding(vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(ScreenState.PROFILE_DETAIL) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PureWhite
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Jathakam Match Report",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = PureWhite
                    )
                    Text(
                        text = "${myProfile.name.split(" ").first()} & ${partner.name.split(" ").first()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGold
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = LightGold,
                    modifier = Modifier
                        .padding(end = 14.dp)
                        .size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Score hero card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(LightGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${report.overallScore}%",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkGold
                            )
                            Text(
                                text = "Overall",
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (report.overallScore >= 80) LightGreen else LightGold
                    ) {
                        Text(
                            text = verdict,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (report.overallScore >= 80) SuccessGreen else DarkGold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        ScoreStat("Poruthams", "${report.poruthamCount}/10")
                        ScoreStat("Guna Milan", "${report.gunaScore}/36")
                        ScoreStat("Dosham", if (partner.dosham.contains("No", true)) "Clear" else "Check")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = report.astrologicalSummary,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 10 Porutham breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "10-Porutham Breakdown",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    report.poruthams.forEach { porutham ->
                        val isMatched = porutham.isMatched
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isMatched) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = if (isMatched) "Matched" else "Not Matched",
                                tint = if (isMatched) SuccessGreen else DarkGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = porutham.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = porutham.description,
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    lineHeight = 14.sp
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(7.dp),
                                color = if (isMatched) LightGreen else LightGold
                            ) {
                                Text(
                                    text = if (isMatched) "YES" else "NO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isMatched) SuccessGreen else DarkGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Astrologer note
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Astrologer Note (Premium)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Platinum members receive a certified astrologer's personalised reading of this match, including muhurtham (auspicious wedding dates) and remedy suggestions.",
                        fontSize = 11.sp,
                        color = PrimaryBlue,
                        lineHeight = 15.sp
                    )
                }
            }

            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = {
                        val shareText = buildString {
                            appendLine("Jathakam Match Report — Soulmate Matrimony")
                            appendLine("${myProfile.name} & ${partner.name}")
                            appendLine("Overall Match: ${report.overallScore}% | Poruthams: ${report.poruthamCount}/10 | Guna: ${report.gunaScore}/36")
                            appendLine("Verdict: $verdict")
                            appendLine("Computed by the Vedic 10-Porutham engine on Soulmate.")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Match Report"))
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Report", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }

                Button(
                    onClick = {
                        viewModel.selectBottomTab(BottomTab.PREMIUM)
                        viewModel.navigateTo(ScreenState.MEMBERSHIP)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue, contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Upgrade for PDF", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ScoreStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryBlue
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}
