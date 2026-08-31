package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

/**
 * Full-screen immersive photo viewer with swipeable gallery,
 * page indicator dots and quick actions (share, shortlist, download).
 */
@Composable
fun PhotoViewerScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val urls by viewModel.photoViewerUrls.collectAsState()
    val currentIndex by viewModel.photoViewerIndex.collectAsState()
    val selectedProfile by viewModel.selectedProfile.collectAsState()

    if (urls.isEmpty()) return
    val profile = selectedProfile

    val pagerState = rememberPagerState(
        initialPage = currentIndex.coerceIn(0, urls.lastIndex),
        pageCount = { urls.size }
    )

    // Sync pager swipes back to VM state
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setPhotoViewerIndex(pagerState.currentPage)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Photo pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = urls[page],
                contentDescription = "Photo ${page + 1} of ${urls.size}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {}
            )
        }

        // Top bar: close + counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.closePhotoViewer() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = PureWhite
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (profile != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                    if (profile.verified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = VerifiedBlue,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${pagerState.currentPage + 1}/${urls.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                modifier = Modifier.padding(end = 10.dp)
            )
        }

        // Bottom action bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .background(Color(0x66000000), CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profile != null) {
                IconButton(onClick = { viewModel.toggleShortlist(profile.id) }) {
                    Icon(
                        imageVector = if (profile.isShortlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Shortlist",
                        tint = if (profile.isShortlisted) GoldAccent else PureWhite
                    )
                }
            }
            IconButton(onClick = { viewModel.showToast("Photo link copied — share with family!") }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = PureWhite
                )
            }
            IconButton(onClick = { viewModel.showToast("Photo saved to gallery") }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = PureWhite
                )
            }
        }

        // Page dots
        if (urls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                urls.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 9.dp else 7.dp)
                            .background(
                                if (index == pagerState.currentPage) GoldAccent
                                else PureWhite.copy(alpha = 0.4f),
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}
