package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.screens.AccountVerificationScreen
import com.example.ui.screens.CallScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.ErrorScreen
import com.example.ui.screens.FaceVerificationScreen
import com.example.ui.screens.GovtIdVerificationScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.HoroscopeReportScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.LoadingScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainAppScreen
import com.example.ui.screens.MembershipScreen
import com.example.ui.screens.NoInternetScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.screens.PaymentFailedScreen
import com.example.ui.screens.PaymentHistoryScreen
import com.example.ui.screens.PaymentSuccessScreen
import com.example.ui.screens.PhotoManagerView
import com.example.ui.screens.PhotoViewerScreen
import com.example.ui.screens.PrivacyControlsScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileCreationScreen
import com.example.ui.screens.ProfileDetailScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.screens.SafetyCenterScreen
import com.example.ui.screens.SearchFilterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SubscribedScreen
import com.example.ui.screens.SuccessScreen
import com.example.ui.screens.SuccessStoriesScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TermsOfServiceScreen
import com.example.ui.screens.VerificationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
  private val viewModel: AppViewModel by viewModels()

  private val notificationPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (!granted) {
        // App remains fully usable; only push alerts are muted.
        Toast.makeText(this, "You can enable notifications anytime in Settings.", Toast.LENGTH_SHORT).show()
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    // Android 13+ runtime permission for push notifications
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
      notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
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
  val canGoBack by viewModel.canGoBack.collectAsState()
  val toastMessage by viewModel.toastMessage.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(toastMessage) {
    toastMessage?.let {
      Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  // System back button: pop the in-app navigation stack first.
  // At the root of the app (MAIN_APP / auth screens) the default exit applies,
  // guarded by a double-back-to-exit confirmation so the app never closes on
  // a single accidental tap.
  val rootScreens = setOf(
    ScreenState.MAIN_APP, ScreenState.LOGIN, ScreenState.SPLASH,
    ScreenState.ONBOARDING, ScreenState.LANGUAGE_SELECT
  )
  var lastBackPressAt = androidx.compose.runtime.remember { androidx.compose.runtime.mutableLongStateOf(0L) }
  BackHandler(enabled = true) {
    if (viewModel.navigateBack()) return@BackHandler
    if (currentScreen == ScreenState.MAIN_APP) {
      val now = System.currentTimeMillis()
      if (now - lastBackPressAt.longValue < 2000) {
        (context as? android.app.Activity)?.finish()
      } else {
        lastBackPressAt.longValue = now
        Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
      }
    }
    // On other root screens (splash/onboarding/login) a single back exits —
    // standard Android behaviour for pre-auth flows.
  }

  AnimatedContent(
      targetState = currentScreen,
      transitionSpec = {
        // Direction-aware: entering a NEW screen slides in from the right,
        // going BACK slides in from the left — a natural stack feel.
        val goingBack = targetScreenOrder(targetState) <= targetScreenOrder(initialState)
        val moveSpec = tween<IntOffset>(durationMillis = 220)
        val fadeSpec = tween<Float>(durationMillis = 220)
        if (goingBack) {
          (slideInHorizontally(moveSpec) { -it / 3 } + fadeIn(fadeSpec)) togetherWith
            (slideOutHorizontally(moveSpec) { it / 3 } + fadeOut(fadeSpec))
        } else {
          (slideInHorizontally(moveSpec) { it / 3 } + fadeIn(fadeSpec)) togetherWith
            (slideOutHorizontally(moveSpec) { -it / 3 } + fadeOut(fadeSpec))
        }
      },
      label = "ScreenTransition"
  ) { screen ->
    androidx.compose.runtime.key(screen) {
      when (screen) {
        ScreenState.SPLASH -> SplashScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.ONBOARDING -> OnboardingScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.LANGUAGE_SELECT -> LanguageSelectionScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.LOGIN -> LoginScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.OTP_VERIFY -> OtpVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PROFILE_CREATION -> ProfileCreationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.MAIN_APP -> MainAppScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PROFILE_DETAIL -> ProfileDetailScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.EDIT_PROFILE -> EditProfileScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PHOTO_MANAGER -> PhotoManagerView(viewModel = viewModel, modifier = modifier.fillMaxSize(), showBackButton = true)
        ScreenState.CHAT_DETAIL -> ChatDetailScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.MEMBERSHIP -> MembershipScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.VERIFICATION_CENTER -> VerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.FACE_VERIFICATION -> FaceVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.GOVT_ID_VERIFICATION -> GovtIdVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())

        // ---- 18 new feature pages ----
        ScreenState.SETTINGS -> SettingsScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PRIVACY_CONTROLS -> PrivacyControlsScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.NOTIFICATIONS -> NotificationsScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.SEARCH_FILTER -> SearchFilterScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PRIVACY_POLICY -> PrivacyPolicyScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.TERMS_OF_SERVICE -> TermsOfServiceScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.SAFETY_CENTER -> SafetyCenterScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.HELP_SUPPORT -> HelpSupportScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.HOROSCOPE_REPORT -> HoroscopeReportScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.CALL_SCREEN -> CallScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PAYMENT_HISTORY -> PaymentHistoryScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PHOTO_VIEWER -> PhotoViewerScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.SUCCESS_STORIES -> SuccessStoriesScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.REFERRAL -> ReferralScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.ACCOUNT_VERIFICATION -> AccountVerificationScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())

        // ---- Global status screens ----
        ScreenState.LOADING -> LoadingScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.SUCCESS -> SuccessScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.ERROR -> ErrorScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.NO_INTERNET -> NoInternetScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.SUBSCRIBED -> SubscribedScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PAYMENT_SUCCESS -> PaymentSuccessScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
        ScreenState.PAYMENT_FAILED -> PaymentFailedScreen(viewModel = viewModel, modifier = modifier.fillMaxSize())
      }
    }
  }
}

/** Stable ordering used to infer forward vs backward navigation direction. */
private fun targetScreenOrder(screen: ScreenState): Int = when (screen) {
  ScreenState.SPLASH -> 0
  ScreenState.ONBOARDING -> 1
  ScreenState.LANGUAGE_SELECT -> 2
  ScreenState.LOGIN -> 3
  ScreenState.OTP_VERIFY -> 4
  ScreenState.PROFILE_CREATION -> 5
  ScreenState.MAIN_APP -> 6
  ScreenState.LOADING -> 7
  ScreenState.SUCCESS -> 7
  ScreenState.ERROR -> 7
  ScreenState.NO_INTERNET -> 7
  ScreenState.PAYMENT_SUCCESS -> 7
  ScreenState.PAYMENT_FAILED -> 7
  ScreenState.SUBSCRIBED -> 7
  // Stack screens above MAIN_APP
  ScreenState.PROFILE_DETAIL -> 8
  ScreenState.EDIT_PROFILE -> 8
  ScreenState.PHOTO_MANAGER -> 8
  ScreenState.CHAT_DETAIL -> 8
  ScreenState.MEMBERSHIP -> 8
  ScreenState.VERIFICATION_CENTER -> 8
  ScreenState.FACE_VERIFICATION -> 9
  ScreenState.GOVT_ID_VERIFICATION -> 9
  ScreenState.SETTINGS -> 8
  ScreenState.PRIVACY_CONTROLS -> 9
  ScreenState.NOTIFICATIONS -> 8
  ScreenState.SEARCH_FILTER -> 8
  ScreenState.PRIVACY_POLICY -> 9
  ScreenState.TERMS_OF_SERVICE -> 9
  ScreenState.SAFETY_CENTER -> 9
  ScreenState.HELP_SUPPORT -> 9
  ScreenState.HOROSCOPE_REPORT -> 9
  ScreenState.CALL_SCREEN -> 10
  ScreenState.PAYMENT_HISTORY -> 9
  ScreenState.PHOTO_VIEWER -> 10
  ScreenState.SUCCESS_STORIES -> 9
  ScreenState.REFERRAL -> 9
  ScreenState.ACCOUNT_VERIFICATION -> 9
}
