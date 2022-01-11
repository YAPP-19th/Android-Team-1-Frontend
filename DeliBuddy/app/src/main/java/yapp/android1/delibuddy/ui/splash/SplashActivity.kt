package yapp.android1.delibuddy.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.LoginActivity
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationActivity
import yapp.android1.delibuddy.ui.permission.PermissionDescriptionActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType
import yapp.android1.delibuddy.util.user.AuthManagementModule
import yapp.android1.delibuddy.util.user.UserAuthManager
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var intentJob: Job? = null
    private lateinit var binding: ActivitySplashBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var userAuthManager: UserAuthManager

    enum class SplashIntent {
        LOGIN,
        PERMISSION,
        HOME
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.w("splash")
        getFCMToken()

        val route = intent.getStringExtra("route")
        Timber.w("route: $route")
        route?.let {
            moveTo(it)
        } ?: kotlin.run {
            checkFirstLaunch()
            initObserve()
        }
    }

    private fun moveTo(route: String) {
        val uri = Uri.parse(route)
        Timber.w("uri path: ${uri.host}")
        when (uri.host) {
            URI_PATH_COMMENT -> {
                val partyId = uri.getQueryParameter(KEY_PARTY_ID)?.toInt() ?: -1
                val commentId = uri.getQueryParameter(KEY_COMMENT_ID)?.toInt() ?: -1

                if (partyId > -1 && commentId > -1) {
                    val intent = Intent(this, PartyInformationActivity::class.java)
                    intent.putExtra(KEY_PARTY_ID, partyId)
                    intent.putExtra(KEY_COMMENT_ID, commentId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "존재하지 않는 게시글입니다", Toast.LENGTH_SHORT).show()
                }
            }

            URI_PATH_PARTY -> {
                val partyId = uri.getQueryParameter(KEY_PARTY_ID)?.toInt() ?: -1
                if (partyId > -1) {
                    val intent = Intent(this, PartyInformationActivity::class.java)
                    intent.putExtra(KEY_PARTY_ID, partyId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "존재하지 않는 게시글입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkFirstLaunch() {
        val isFirstLaunch = DeliBuddyApplication.prefs.getIsFirstLaunch()
        if (isFirstLaunch) {
            DeliBuddyApplication.prefs.saveIsFirstLaunch(false)
            intentPermissionDescription()
        } else {
            PermissionManager.checkPermission(this, PermissionType.LOCATION) {
                when (it) {
                    PermissionState.GRANTED -> checkLoginAndIntent()
                    else -> showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun checkLoginAndIntent() {
        if (!userAuthManager.getDeliBuddyAuth().isAvailable()) {
            splashIntent(SplashIntent.LOGIN)
        } else {
            userAuthManager.checkAuthStatus { status ->
                when (status) {
                    AuthManagementModule.AUTH_TOKEN_EXPIRED_STATUS -> {
                        userAuthManager.setDeliBuddyAuth(Auth.EMPTY)
                        splashIntent(SplashIntent.LOGIN)
                    }
                    AuthManagementModule.AUTH_TOKEN_REFRESH_REQUIRED_STATUS -> {
                        authViewModel.occurEvent(
                            AuthViewModel.AuthEvent.OnAuthTokenRefresh()
                        )
                    }
                    AuthManagementModule.AUTH_TOKEN_AVAILABLE_STATUS -> {
                        splashIntent(SplashIntent.HOME)
                    }
                }
            }
        }

    }

    private fun initObserve() {
        repeatOnStarted {
            authViewModel.tokenResult.collect { auth ->
                if (auth.isAvailable()) {
                    userAuthManager.setDeliBuddyAuth(auth)
                    splashIntent(SplashIntent.HOME)
                }
            }
        }

        repeatOnStarted {
            splashViewModel.setFcmTokenResult.collect { isSuccess ->
                Timber.w("fcm server response: $isSuccess")
            }
        }
    }

    private fun intentPermissionDescription() {
        splashIntent(SplashIntent.PERMISSION)
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(this).apply {
            negativeCallback = {
                checkLoginAndIntent()
            }
        }
        permissionDialog.show(this.supportFragmentManager, null)
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            splashViewModel.occurEvent(SplashViewModel.SplashEvent.SetFcmTokenEvent(token))
        })
    }

    private fun splashIntent(destination: SplashActivity.SplashIntent) {
        intentJob = lifecycleScope.launch {
            delay(2000L)
            when (destination) {
                SplashIntent.LOGIN -> intentTo(LoginActivity::class.java)
                SplashIntent.PERMISSION -> intentTo(PermissionDescriptionActivity::class.java)
                SplashIntent.HOME -> intentTo(HomeActivity::class.java)
            }
        }
    }

    override fun onBackPressed() {
        intentJob?.cancel()
        super.onBackPressed()
    }

    override fun onDestroy() {
        intentJob?.cancel()
        super.onDestroy()
    }

    companion object {
        const val URI_PATH_PARTY = "party"
        const val URI_PATH_COMMENT = "comment"
        const val KEY_PARTY_ID = "partyId"
        const val KEY_COMMENT_ID = "commentId"
    }
}
