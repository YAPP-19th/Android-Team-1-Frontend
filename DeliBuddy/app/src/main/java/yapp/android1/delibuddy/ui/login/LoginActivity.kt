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
import yapp.android1.delibuddy.databinding.ActivityLoginBinding
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collectData()
        loginWithKakaoApi()
    }

    private fun collectData() {
        repeatOnStarted {
            authViewModel.tokenResult.collect { auth ->
                Toast.makeText(this@LoginActivity, "TOKEN - $auth", Toast.LENGTH_SHORT).show()

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

