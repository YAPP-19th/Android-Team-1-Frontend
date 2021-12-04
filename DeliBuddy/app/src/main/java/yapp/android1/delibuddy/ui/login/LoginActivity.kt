package yapp.android1.delibuddy.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.databinding.ActivityLoginBinding
import yapp.android1.delibuddy.ui.login.viewmodel.AuthViewModel
import yapp.android1.domain.interactor.usecase.FetchAuthUseCase
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        loginWithKakao()
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("TAG", "로그인 실패", error)

        } else if (token != null) {
            Log.i("TAG", "로그인 성공 ${token.accessToken}")

            authViewModel.test(token.accessToken)
        }
    }

    private fun loginWithKakao() {
        binding.buttonKakaoLogin.setOnClickListener {
            with(UserApiClient.instance) {
                if (isKakaoTalkLoginAvailable(context = context)) {
                    loginWithKakaoTalk(context = context, callback = kakaoLoginCallback)
                } else {
                    loginWithKakaoAccount(context = context, callback = kakaoLoginCallback)
                }
            }
        }
    }

}