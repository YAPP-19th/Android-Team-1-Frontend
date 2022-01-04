package yapp.android1.delibuddy.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.datatransport.runtime.scheduling.persistence.EventStoreModule_DbNameFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.home.fragments.PARTY
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
import java.net.URI
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
        when(uri.host) {
            "comment" -> {
                val partyId = uri.getQueryParameter("partyId")?.toInt() ?: -1
                val commentId = uri.getQueryParameter("commentId")?.toInt() ?: -1

                if (partyId > -1 && commentId > -1) {
                    val intent = Intent(this, PartyInformationActivity::class.java)
                    intent.putExtra("partyId", partyId)
                    intent.putExtra("commentId", commentId)
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

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Timber.w(token)

            // TODO: 서버로 토큰 보내주기
        })
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
