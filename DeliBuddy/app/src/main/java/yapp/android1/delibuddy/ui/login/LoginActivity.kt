package yapp.android1.delibuddy.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import yapp.android1.delibuddy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        loginWithKakao()
    }

    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null) {
            Log.e("TAG", "로그인 실패", error)

        } else if(token != null) {
            Log.i("TAG", "로그인 성공 ${token.accessToken}")

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