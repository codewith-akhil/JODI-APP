package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.FaceVerificationScreen
import com.example.ui.screens.GovtIdVerificationScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainAppScreen
import com.example.ui.screens.MembershipScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.screens.PhotoManagerView
import com.example.ui.screens.ProfileCreationScreen
import com.example.ui.screens.ProfileDetailScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.VerificationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
  private val viewModel: AppViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        SoulmateApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SoulmateApp(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val toastMessage by viewModel.toastMessage.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(toastMessage) {
    toastMessage?.let {
      Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  AnimatedContent(
      targetState = currentScreen,
      transitionSpec = { fadeIn() togetherWith fadeOut() },
      label = "ScreenTransition"
  ) { screen ->
    when (screen) {
      ScreenState.SPLASH -> SplashScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.LANGUAGE_SELECT -> LanguageSelectionScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.LOGIN -> LoginScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.OTP_VERIFY -> OtpVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.PROFILE_CREATION -> ProfileCreationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.MAIN_APP -> MainAppScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.PROFILE_DETAIL -> ProfileDetailScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.EDIT_PROFILE -> EditProfileScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.PHOTO_MANAGER -> PhotoManagerView(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.CHAT_DETAIL -> ChatDetailScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.MEMBERSHIP -> MembershipScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.VERIFICATION_CENTER -> VerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.FACE_VERIFICATION -> FaceVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      ScreenState.GOVT_ID_VERIFICATION -> GovtIdVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
    }
  }
}

