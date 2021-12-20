package yapp.android1.delibuddy.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.databinding.ActivityLoginBinding
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthCondition()

        collectData()
        toastErrorMessage()
        loginWithKakaoApi()
    }

    private fun checkAuthCondition() {
        if (DeliBuddyApplication.prefs.getAuth().isNotEmpty()) {
            Timber.tag("------------[TAG]").d("- JWT : ${DeliBuddyApplication.prefs.getAuth().token}")
            intentTo(HomeActivity::class.java)
        }
    }

    private fun collectData() {
        repeatOnStarted {
            authViewModel.tokenResult.collect { auth ->
                if (auth.isNotEmpty()) {
                    DeliBuddyApplication.prefs.saveAuthData(auth)
                    intentTo(HomeActivity::class.java)
                } else {
                    AuthViewModel.AuthEvent.OnKakaoLoginFailed("다시 로그인해주세요.")
                }
            }
        }
    }

    private fun toastErrorMessage() {
        repeatOnStarted {
            authViewModel.showToast.collect {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            authViewModel.occurEvent(
                AuthViewModel.AuthEvent.OnKakaoLoginFailed(error.message ?: "Unknown error")
            )
        } else if (token != null) {
            authViewModel.occurEvent(
                AuthViewModel.AuthEvent.OnKakaoLoginSuccess(token.accessToken)
            )
        }
    }

    private fun loginWithKakaoApi() {
        binding.buttonKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context = this@LoginActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(
                    context = this@LoginActivity,
                    callback = kakaoLoginCallback
                )
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    context = this@LoginActivity,
                    callback = kakaoLoginCallback
                )
            }
        }
    }

}

