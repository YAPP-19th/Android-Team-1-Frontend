package yapp.android1.delibuddy.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.LoginActivity
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
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

    @Inject
    lateinit var userAuthManager: UserAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkFirstLaunch()

        initObserve()
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
            intentTo(LoginActivity::class.java)
        } else {
            userAuthManager.checkAuthStatus { status ->
                when (status) {
                    AuthManagementModule.AUTH_TOKEN_EXPIRED_STATUS -> {
                        userAuthManager.setDeliBuddyAuth(Auth.EMPTY)
                        intentTo(LoginActivity::class.java)
                    }
                    AuthManagementModule.AUTH_TOKEN_REFRESH_REQUIRED_STATUS -> {
                        authViewModel.occurEvent(
                            AuthViewModel.AuthEvent.OnAuthTokenRefresh()
                        )
                    }
                    AuthManagementModule.AUTH_TOKEN_AVAILABLE_STATUS -> {
                        intentTo(HomeActivity::class.java)
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

                    intentJob = lifecycleScope.launch {
                        delay(2000L)
                        intentTo(HomeActivity::class.java)
                    }
                }
            }
        }
    }

    private fun intentPermissionDescription() {
        intentJob = lifecycleScope.launch {
            delay(2000L)
            intentTo(PermissionDescriptionActivity::class.java)
        }
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(this).apply {
            negativeCallback = {
                checkLoginAndIntent()
            }
        }
        permissionDialog.show(this.supportFragmentManager, null)
    }

    override fun onBackPressed() {
        intentJob?.cancel()
        super.onBackPressed()
    }

    override fun onDestroy() {
        intentJob?.cancel()
        super.onDestroy()
    }
}
