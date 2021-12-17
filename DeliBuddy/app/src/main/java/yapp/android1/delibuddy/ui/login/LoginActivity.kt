package yapp.android1.delibuddy.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.databinding.ActivityLoginBinding
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.user.UserManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthCondition()

        collectData()
        loginWithKakaoApi()
    }

    private fun checkAuthCondition() {
        if (userManager.getDeliBuddyAuth().isAvailable()) {
            intentTo(HomeActivity::class.java)
        }
    }

    private fun collectData() {
        repeatOnStarted {
            authViewModel.tokenResult.collect { auth ->
                when (auth.isAvailable()) {
                    true -> {
                        DeliBuddyApplication.prefs.saveAuthData(auth)
                        intentTo(HomeActivity::class.java)
                    }
                    false -> AuthViewModel.AuthEvent.OnKakaoLoginFailed("다시 시도해 주세요.")
                }
            }
        }
    }

    private fun loginWithKakaoApi() {
        binding.buttonKakaoLogin.setOnClickListener {
            userManager.kakaoLogin { isLoginSuccess, errorMessage, kakaoToken ->
                when (isLoginSuccess) {
                    true -> authViewModel.occurEvent(
                        AuthViewModel.AuthEvent.OnKakaoLoginSuccess(
                            kakaoToken!!
                        )
                    )
                    false -> {
                        Toast.makeText(
                            this,
                            "카카오 로그인 실패\nERROR: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

}

