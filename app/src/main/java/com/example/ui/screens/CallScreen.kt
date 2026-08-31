package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import kotlinx.coroutines.delay

/**
 * In-app secure Voice / Video call screen.
 * Privacy-first: the dialer shows both parties are protected and
 * numbers stay hidden. State machine: RINGING → ONGOING → Ended.
 */
@Composable
fun CallScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val callSession by viewModel.callSession.collectAsState()
    val session = callSession ?: return

    var micMuted by remember { mutableStateOf(false) }
    var speakerOn by remember { mutableStateOf(true) }
    var videoOn by remember { mutableStateOf(session.isVideo) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "call")
    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Restart),
        label = "rippleScale"
    )
    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Restart),
        label = "rippleAlpha"
    )

    // Back gesture ends the call (same as the red button)
    androidx.activity.compose.BackHandler {
        viewModel.endCall()
    }

    // Auto-connect after a short ring (simulated accept by partner)
    LaunchedEffect(session.callState) {
        if (session.callState == "RINGING") {
            delay(2600)
            viewModel.connectCall()
        }
    }

    // Call duration ticker
    LaunchedEffect(session.callState) {
        while (session.callState == "ONGOING") {
            delay(1000)
            elapsedSeconds += 1
        }
    }

    val isOngoing = session.callState == "ONGOING"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryTeal, PrimaryBlue, SapphireBlue)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Secure header
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0x26FFFFFF)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = LightGold,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Secure Matrimony Call • Numbers Hidden",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGold
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.8f))

        // Avatar with ripple
        Box(contentAlignment = Alignment.Center) {
            if (!isOngoing) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(rippleScale)
                        .alpha(rippleAlpha)
                        .background(LightGold, CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(3.dp, LightGold.copy(alpha = 0.7f), CircleShape)
            ) {
                AsyncImage(
                    model = session.profile.photoUrls.firstOrNull(),
                    contentDescription = session.profile.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${session.profile.name}, ${session.profile.age}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = PureWhite
        )
        Text(
            text = "${session.profile.city} • ${session.profile.profession}",
            fontSize = 13.sp,
            color = PureWhite.copy(alpha = 0.85f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = when {
                session.callState == "CONNECTING" -> "Connecting..."
                session.callState == "ENDED" -> "Call Ended"
                !isOngoing -> "Ringing..."
                micMuted -> "Muted • ${formatCallDuration(elapsedSeconds)}"
                else -> "Connected • ${formatCallDuration(elapsedSeconds)}"
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = LightGold
        )

        Spacer(modifier = Modifier.weight(1f))

        // Control pad
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            CallControlButton(
                icon = if (micMuted) Icons.Default.MicOff else Icons.Default.Mic,
                label = if (micMuted) "Unmute" else "Mute",
                active = micMuted,
                onClick = { micMuted = !micMuted }
            )
            CallControlButton(
                icon = if (speakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                label = "Speaker",
                active = speakerOn,
                onClick = { speakerOn = !speakerOn }
            )
            if (session.isVideo) {
                CallControlButton(
                    icon = if (videoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    label = if (videoOn) "Camera" else "Camera Off",
                    active = !videoOn,
                    onClick = { videoOn = !videoOn }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // End call button
        Surface(
            onClick = { viewModel.endCall() },
            shape = CircleShape,
            color = Color(0xFFE53935),
            modifier = Modifier.size(68.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = "End Call",
                    tint = PureWhite,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isOngoing) "Tap the red button to end the call"
            else "Waiting for ${session.profile.name.split(" ").first()} to accept...",
            fontSize = 11.sp,
            color = PureWhite.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun formatCallDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}

@Composable
private fun CallControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = if (active) LightGold else Color(0x33FFFFFF),
            modifier = Modifier.size(58.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (active) SapphireBlue else PureWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = PureWhite.copy(alpha = 0.8f)
        )
    }
}
