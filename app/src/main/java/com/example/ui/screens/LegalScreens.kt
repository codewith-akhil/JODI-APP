package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

/**
 * Legal pages: Privacy Policy and Terms of Service.
 * Shared section renderer keeps both pages visually identical.
 */

@Composable
fun PrivacyPolicyScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    LegalScreen(
        viewModel = viewModel,
        title = "Privacy Policy",
        subtitle = "Last updated: 1 September 2026",
        icon = Icons.Default.Policy,
        intro = "Soulmate Matrimony is built on trust. This policy explains exactly what data we collect, why we need it, and the safeguards protecting you and your family's information.",
        sections = listOf(
            LegalSection(
                "1. Information We Collect",
                listOf(
                    "Account data: mobile number (verified via Firebase SMS OTP), optional e-mail (verified via e-mail OTP link), Google account profile when you sign in with Google.",
                    "Biodata: name, age, height, religion, caste, gothram, nakshatra, rasi, education, profession, income bracket, family details and partner preferences you enter.",
                    "Photos: profile images uploaded to encrypted Firebase Cloud Storage buckets.",
                    "Verification: face liveness scan result (match score only) and government ID type with masked number — full ID images are stored privately and never shown to other members.",
                    "Communications: chat messages stored in Firebase Realtime Database to deliver real-time messaging across devices.",
                    "Subscriptions: plan purchases and transaction receipts (order id, payment id, status) retained for accounting and support."
                )
            ),
            LegalSection(
                "2. How Your Data Is Protected",
                listOf(
                    "Phone numbers are never displayed publicly. Contact unlock is always explicit and consent-based.",
                    "Photos carry automatic watermark protection; downloads by other members are traceable.",
                    "All network traffic uses TLS 1.2+ encryption to Firebase endpoints.",
                    "Firebase Security Rules restrict profile, chat and transaction records to authenticated owners.",
                    "Destructive actions (deactivate / delete) are protected by fresh mobile OTP verification."
                )
            ),
            LegalSection(
                "3. Your Privacy Rights",
                listOf(
                    "Visibility control: switch profile visibility to Everyone, Verified Only, or Hidden anytime from Privacy Controls.",
                    "Granular hiding: independently hide horoscope details, income and family information.",
                    "Incognito browsing: view profiles without appearing in visitors lists.",
                    "Data portability & erasure: deactivate instantly or permanently delete your account — both trigger removal of biodata, photos, chats and subscription records from our Realtime Database and Storage.",
                    "Report & block: flag any profile for Trust & Safety review; blocked members can no longer see or contact you."
                )
            ),
            LegalSection(
                "4. Data Sharing",
                listOf(
                    "We never sell your personal data to third parties.",
                    "Payment processing is handled by Razorpay; card/UPI details never touch our servers.",
                    "SMS delivery is performed by Google Firebase Authentication infrastructure.",
                    "Law-enforcement requests are honoured only when legally compelled and documented."
                )
            ),
            LegalSection(
                "5. Contact",
                listOf(
                    "Grievance Officer: privacy@soulmatematrimony.com",
                    "Response commitment: within 72 hours for any privacy request."
                )
            )
        )
    )
}

@Composable
fun TermsOfServiceScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    LegalScreen(
        viewModel = viewModel,
        title = "Terms of Service",
        subtitle = "Effective from: 1 September 2026",
        icon = Icons.Default.Description,
        intro = "Welcome to Soulmate Matrimony. By creating a profile you agree to these terms, which keep our community safe, respectful and intentioned for marriage.",
        sections = listOf(
            LegalSection(
                "1. Eligibility",
                listOf(
                    "You must be at least 21 years (men) or 18 years (women) to use Soulmate, per Indian law.",
                    "Profiles must be created for yourself or with explicit consent of the person being represented.",
                    "One genuine profile per person; duplicate or fake profiles are removed without refund."
                )
            ),
            LegalSection(
                "2. Community Conduct",
                listOf(
                    "Be truthful: biodata, photos and horoscope details must be accurate and current.",
                    "Respect boundaries: harassment, abusive language, or unwanted contact after a decline results in permanent suspension.",
                    "Never share financial information, OTPs, or send money to other members — Soulmate never mediates monetary transactions between members.",
                    "Report suspicious behaviour through the Safety Center; our team reviews every report within 24 hours."
                )
            ),
            LegalSection(
                "3. Membership & Payments",
                listOf(
                    "Plans (Silver, Gold, Platinum) unlock premium features for the stated duration and renew only with your explicit consent.",
                    "Payments are processed securely via Razorpay (UPI, cards, net-banking); receipts are stored in your Transaction History.",
                    "Refunds: full refund within 7 days if no contact unlock has occurred; raise a ticket via Help & Support. Chargebacks initiated without contacting support may lead to account suspension.",
                    "Failed payments are never charged; retry anytime from the payment screen."
                )
            ),
            LegalSection(
                "4. Verification & Trust",
                listOf(
                    "Face liveness verification and Government ID validation via DigiLocker are performed to maintain authenticity.",
                    "Trust Scores are computed algorithmically from verification milestones and may be displayed to other members.",
                    "Submitting fraudulent documents leads to immediate removal and reporting to authorities where required."
                )
            ),
            LegalSection(
                "5. Account Lifecycle",
                listOf(
                    "Deactivation hides your profile instantly and is reversible on next login.",
                    "Deletion permanently erases biodata, photos, chats and subscriptions from our systems; this requires OTP verification and cannot be reversed.",
                    "We may suspend accounts violating these terms after notice, where feasible."
                )
            ),
            LegalSection(
                "6. Disclaimers",
                listOf(
                    "Astrological reports (10-Porutham / 36 Guna) follow traditional Vedic computation and are provided for guidance; marriage outcomes are the responsibility of members and families.",
                    "Soulmate facilitates introductions and is not liable for off-platform interactions.",
                    "Governing law: Courts of Kerala, India."
                )
            )
        )
    )
}

// ---------------------------------------------------------------------------
// Shared renderer
// ---------------------------------------------------------------------------

private data class LegalSection(
    val heading: String,
    val bullets: List<String>
)

@Composable
private fun LegalScreen(
    viewModel: AppViewModel,
    title: String,
    subtitle: String,
    icon: ImageVector,
    intro: String,
    sections: List<LegalSection>
) {
    Column(
        modifier = Modifier
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
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
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
            // Document header card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(LightGreen, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = subtitle,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = intro,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }

            sections.forEach { section ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = section.heading,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        section.bullets.forEach { bullet ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "•",
                                    fontSize = 12.sp,
                                    color = PrimaryEmerald,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.width(14.dp)
                                )
                                Text(
                                    text = bullet,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
