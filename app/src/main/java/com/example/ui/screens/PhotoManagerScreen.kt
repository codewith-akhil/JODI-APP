package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.UserPhoto
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DeepBurgundy
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LightGold
import com.example.ui.theme.LightGreen
import com.example.ui.theme.LightRose
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RosePrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmBackground
import com.example.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoManagerView(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val userPhotos by viewModel.userPhotos.collectAsState()
    var showAddPhotoSheet by remember { mutableStateOf(false) }
    var previewPhotoUrl by remember { mutableStateOf<String?>(null) }
    var isPrivacyEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Manage Photos (${userPhotos.size}/6)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Profiles with 3+ photos get 5x more responses",
                    fontSize = 12.sp,
                    color = RosePrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = { showAddPhotoSheet = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepBurgundy,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_photo_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Banner Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isPrivacyEnabled) Icons.Default.Lock else Icons.Default.Visibility,
                        contentDescription = "Privacy",
                        tint = DeepBurgundy,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Photo Privacy Shield",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isPrivacyEnabled) "Photos visible only to accepted requests" else "Photos visible to all verified members",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                Switch(
                    checked = isPrivacyEnabled,
                    onCheckedChange = {
                        isPrivacyEnabled = it
                        viewModel.showToast(if (it) "Photo Privacy Shield Enabled 🔒" else "Photo Privacy Shield Disabled")
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = DeepBurgundy)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Photos Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(userPhotos) { photo ->
                UserPhotoCard(
                    photo = photo,
                    onPreview = { previewPhotoUrl = photo.url },
                    onSetPrimary = { viewModel.setAsProfilePhoto(photo.id) },
                    onDelete = { viewModel.removeUserPhoto(photo.id) }
                )
            }

            // Empty Slots
            val emptySlots = (6 - userPhotos.size).coerceAtLeast(0)
            items(emptySlots) {
                EmptyPhotoSlot(onClick = { showAddPhotoSheet = true })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Guidelines Checklist
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LightGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📸 Photo Upload Guidelines",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                PhotoGuidelineItem("Front facing close-up with good natural lighting")
                PhotoGuidelineItem("Solo portrait (No group photos or sunglasses)")
                PhotoGuidelineItem("Aadhaar photo match verified for trust badge")
            }
        }
    }

    // Add Photo Bottom Sheet
    if (showAddPhotoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddPhotoSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = PureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "Add Photo to Your Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Select a source to upload your high resolution picture",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                PhotoSourceOption(
                    title = "Take with Camera",
                    subtitle = "Capture a fresh selfie or portrait",
                    icon = Icons.Default.CameraAlt,
                    onClick = {
                        showAddPhotoSheet = false
                        viewModel.addUserPhoto("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=80")
                    }
                )

                PhotoSourceOption(
                    title = "Choose from Gallery / Album",
                    subtitle = "Select from device folders & recents",
                    icon = Icons.Default.PhotoAlbum,
                    onClick = {
                        showAddPhotoSheet = false
                        viewModel.addUserPhoto("https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop&q=80")
                    }
                )

                PhotoSourceOption(
                    title = "Import Festival / Traditional Photo",
                    subtitle = "Add Kerala traditional attire / saree photo",
                    icon = Icons.Default.AddPhotoAlternate,
                    onClick = {
                        showAddPhotoSheet = false
                        viewModel.addUserPhoto("https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop&q=80")
                    }
                )
            }
        }
    }

    // Fullscreen Preview Dialog
    if (previewPhotoUrl != null) {
        Dialog(
            onDismissRequest = { previewPhotoUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = previewPhotoUrl,
                    contentDescription = "Preview",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { previewPhotoUrl = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(16.dp)
                        .size(44.dp)
                        .background(Color(0x66000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = PureWhite
                    )
                }
            }
        }
    }
}

@Composable
fun UserPhotoCard(
    photo: UserPhoto,
    onPreview: () -> Unit,
    onSetPrimary: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0))
            .clickable(onClick = onPreview)
    ) {
        AsyncImage(
            model = photo.url,
            contentDescription = "My Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Profile Picture Star Badge
        if (photo.isProfilePicture) {
            Surface(
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                color = DeepBurgundy,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Primary", tint = GoldAccent, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Main", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                }
            }
        }

        // Action icons bottom bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0x99000000))
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!photo.isProfilePicture) {
                Text(
                    text = "Set Main",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    modifier = Modifier
                        .clickable(onClick = onSetPrimary)
                        .padding(2.dp)
                )
            } else {
                Text(text = "Primary", fontSize = 9.sp, color = PureWhite)
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(22.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = CrimsonRed,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyPhotoSlot(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PureWhite)
            .border(
                width = 1.5.dp,
                color = BorderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add slot",
                tint = RosePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Add Photo",
                fontSize = 11.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PhotoSourceOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LightRose, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = DeepBurgundy)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun PhotoGuidelineItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "OK",
            tint = SuccessGreen,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 11.sp, color = TextPrimary)
    }
}
