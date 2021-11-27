package yapp.android1.delibuddy.ui.login

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        loginWithKaKao()
    }

    private fun loginWithKaKao() {
        binding.login.setOnClickListener {
            with(UserApiClient.instance) {
                if (isKakaoTalkLoginAvailable(context = context)) {
                    loginWithKakaoTalk(context = context, callback = KaKaoLoginCallback.callback)
                } else {
                    loginWithKakaoAccount(context = context, callback = KaKaoLoginCallback.callback)
                }
            }
        }
    }

}