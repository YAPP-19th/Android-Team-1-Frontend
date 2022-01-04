package yapp.android1.delibuddy.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.databinding.ActivityLoginBinding
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.delibuddy.ui.terms.TermsActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.user.UserAuthManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var userAuthManager: UserAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        collectData()
        loginWithKakaoApi()
    }

    private fun initView() {
        binding.policyInfoTermsOfService.setOnClickListener {
            intentToTerms(TermsActivity.FILE_TERMS_OF_SERVICE)
        }

        binding.policyInfoPrivacyPolicy.setOnClickListener {
            intentToTerms(TermsActivity.PRIVACY_POLICY)
        }
    }

    private fun intentToTerms(termsIndex: Int) {
        val intent = Intent(this, TermsActivity::class.java)
        intent.putExtra(TermsActivity.EXTRA_TERMS, termsIndex)
        startActivity(intent)
    }

    private fun collectData() {
        repeatOnStarted {
            authViewModel.tokenResult.collect { auth ->
                if (auth.isAvailable()) {
                    userAuthManager.setDeliBuddyAuth(auth)
                    intentTo(HomeActivity::class.java)
                } else {
                    AuthViewModel.AuthEvent.OnKakaoLoginFailed("다시 시도해 주세요.")
                }
            }
        }
    }

    private fun loginWithKakaoApi() {
        binding.buttonKakaoLogin.setOnClickListener {
            userAuthManager.kakaoLogin { isLoginSuccess, errorMessage, kakaoToken ->
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

