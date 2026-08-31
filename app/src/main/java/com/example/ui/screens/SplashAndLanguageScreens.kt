package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.Language
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// 3D Star Particle for Ambient Background
private data class StarParticle(
    val xRatio: Float,
    val yRatio: Float,
    val zDepth: Float,
    val speed: Float,
    val size: Float,
    val baseAlpha: Float
)

@Composable
fun SplashScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.7f) }
    val alphaAnim = remember { Animatable(0f) }

    // 3D Gyroscopic Rotations
    val infiniteTransition = rememberInfiniteTransition(label = "3d_rotation")
    val rotationAngleY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle_y"
    )
    val ringTiltAngle by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_tilt"
    )
    val pulseAura by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_aura"
    )
    val sparkleGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle"
    )

    // Precompute random 3D star particles
    val particles = remember {
        List(40) {
            val rng = Random(it * 101)
            StarParticle(
                xRatio = rng.nextFloat(),
                yRatio = rng.nextFloat(),
                zDepth = rng.nextFloat() * 0.8f + 0.2f,
                speed = rng.nextFloat() * 1.5f + 0.5f,
                size = rng.nextFloat() * 4.5f + 2f,
                baseAlpha = rng.nextFloat() * 0.6f + 0.3f
            )
        }
    }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(600))
        scale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
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
                        Color(0xFF034A6E), // Radiant Deep Ocean Blue
                        PrimaryEmerald,    // Rich Emerald Green
                        PrimaryTeal,       // Auspicious Teal Cyan
                        Color(0xFF065F46)  // Forest Emerald
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onGetStarted
            ),
        contentAlignment = Alignment.Center
    ) {
        // FULLSCREEN 3D CANVAS ANIMATION (Cosmic Stars, Sacred Halo, Interlocking 3D Rings)
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height * 0.32f

            // 1. Draw 3D Celestial Particles
            particles.forEach { particle ->
                val px = (particle.xRatio * width + (rotationAngleY * particle.speed * 0.3f)) % width
                val py = particle.yRatio * height
                val particleRadius = particle.size * particle.zDepth
                val particleAlpha = (particle.baseAlpha * sparkleGlow).coerceIn(0.1f, 1f)

                drawCircle(
                    color = SkyBlue.copy(alpha = particleAlpha),
                    radius = particleRadius,
                    center = Offset(px, py)
                )
            }

            // 2. Glowing Sacred Aura Halo
            val auraRadius = (130.dp.toPx()) * pulseAura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        SkyBlue.copy(alpha = 0.35f * sparkleGlow),
                        MintGreen.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = auraRadius
                ),
                radius = auraRadius,
                center = Offset(centerX, centerY)
            )

            // 3. Sacred Geometry 12-Ray Star Mandala (Rotating)
            rotate(degrees = rotationAngleY * 0.2f, pivot = Offset(centerX, centerY)) {
                val rayCount = 12
                val innerRadius = 70.dp.toPx()
                val outerRadius = 105.dp.toPx() * (1f + (pulseAura - 1f) * 0.3f)

                for (i in 0 until rayCount) {
                    val angle = Math.toRadians((i * (360.0 / rayCount)))
                    val startX = (centerX + innerRadius * cos(angle)).toFloat()
                    val startY = (centerY + innerRadius * sin(angle)).toFloat()
                    val endX = (centerX + outerRadius * cos(angle)).toFloat()
                    val endY = (centerY + outerRadius * sin(angle)).toFloat()

                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(LightBlue.copy(alpha = 0.6f), Color.Transparent),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY)
                        ),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // 4. Interlocking 3D Gyroscopic Rings
            draw3DWeddingRings(
                centerX = centerX,
                centerY = centerY,
                rotationAngle = rotationAngleY,
                tiltAngle = ringTiltAngle,
                sparkle = sparkleGlow
            )
        }

        // FOREGROUND CONTENT (Logo, Branding, Buttons)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // 512x512 Round Shaped Emblem Logo
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(105.dp)
                    .clip(CircleShape)
                    .background(PureWhite)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_jodii_logo),
                    contentDescription = "Soulmate Logo",
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
                    text = "SOULMATE",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = PureWhite,
                    letterSpacing = 6.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Sparkle",
                        tint = SkyBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREMIUM MATRIMONY",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightTeal,
                        letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Sparkle",
                        tint = SkyBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Where Hearts Meet & Destinies Align\nവിശ്വസനീയമായ സൗൾമേറ്റ് മാട്രിമോണി",
                    fontSize = 13.sp,
                    color = PureWhite.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Verified Badge Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0x33FFFFFF), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Verified",
                        tint = LightTeal,
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

                Spacer(modifier = Modifier.height(24.dp))

                // Get Started Button
                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhite,
                        contentColor = PrimaryEmerald
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("splash_get_started_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Get Started / ആരംഭിക്കൂ",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryEmerald
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = PrimaryEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3D Projection Canvas Drawing for Interlocking Wedding Rings
 */
private fun DrawScope.draw3DWeddingRings(
    centerX: Float,
    centerY: Float,
    rotationAngle: Float,
    tiltAngle: Float,
    sparkle: Float
) {
    val ringRadius = 46.dp.toPx()
    val ringStroke = 6.dp.toPx()

    // Ring 1 (Emerald Cyan Band) - Tilted Left
    val ring1Offset = Offset(centerX - 24.dp.toPx(), centerY)
    val ring1ScaleX = cos(Math.toRadians((rotationAngle).toDouble())).toFloat().coerceIn(-1f, 1f)
    val ring1Width = (ringRadius * (0.6f + 0.4f * kotlin.math.abs(ring1ScaleX)))

    drawOval(
        brush = Brush.sweepGradient(
            colors = listOf(
                MintGreen,
                SkyBlue,
                PureWhite,
                PrimaryEmerald,
                MintGreen
            ),
            center = ring1Offset
        ),
        topLeft = Offset(ring1Offset.x - ring1Width, ring1Offset.y - ringRadius),
        size = androidx.compose.ui.geometry.Size(ring1Width * 2, ringRadius * 2),
        style = Stroke(width = ringStroke)
    )

    // Ring 2 (Sapphire Blue Diamond Band) - Interlocking Right
    val ring2Offset = Offset(centerX + 24.dp.toPx(), centerY)
    val ring2ScaleX = cos(Math.toRadians((rotationAngle + 60).toDouble())).toFloat().coerceIn(-1f, 1f)
    val ring2Width = (ringRadius * (0.6f + 0.4f * kotlin.math.abs(ring2ScaleX)))

    drawOval(
        brush = Brush.sweepGradient(
            colors = listOf(
                PureWhite,
                SkyBlue,
                SapphireBlue,
                LightTeal,
                PureWhite
            ),
            center = ring2Offset
        ),
        topLeft = Offset(ring2Offset.x - ring2Width, ring2Offset.y - ringRadius),
        size = androidx.compose.ui.geometry.Size(ring2Width * 2, ringRadius * 2),
        style = Stroke(width = ringStroke)
    )

    // Solitaire Diamond Gem on Ring 2 Apex
    val gemOffset = Offset(ring2Offset.x, ring2Offset.y - ringRadius)
    drawCircle(
        color = PureWhite,
        radius = 5.dp.toPx() * sparkle,
        center = gemOffset
    )
    drawCircle(
        color = SkyBlue,
        radius = 2.dp.toPx(),
        center = gemOffset
    )

    // Center Heart Glyph joining the 3D union
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(PureWhite.copy(alpha = 0.9f * sparkle), Color.Transparent),
            center = Offset(centerX, centerY),
            radius = 16.dp.toPx()
        ),
        radius = 16.dp.toPx(),
        center = Offset(centerX, centerY)
    )
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
                    .background(LightBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = PrimaryBlue,
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
                    text = "നിങ്ങളുടെ ഭാഷ തിരഞ്ഞെടുക്കുക",
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
            items(SampleData.languages) { lang ->
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
                text = "Continue with ${selectedLanguage.nativeName} (${selectedLanguage.name})",
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
