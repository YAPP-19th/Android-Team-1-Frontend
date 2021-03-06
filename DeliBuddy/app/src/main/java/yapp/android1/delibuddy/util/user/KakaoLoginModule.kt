package yapp.android1.delibuddy.util.user

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class KakaoLoginModule @Inject constructor(
    @ActivityContext private val context: Context
) {
    private var loginCallback: ((
        isLoginSuccess: Boolean,
        errorMsg: String?,
        kakaoToken: String?
    ) -> Unit)? = null

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            loginCallback?.let { it(false, error.message ?: "Unknown error", null) }
        } else if (token != null) {
            loginCallback?.let { it(true, null, token.accessToken) }
        }
    }

    fun kakaoLogin(call: (Boolean, String?, String?) -> Unit) {
        loginCallback = call

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)) {
            UserApiClient.instance.loginWithKakaoTalk(
                context = context,
                callback = kakaoLoginCallback
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context = context,
                callback = kakaoLoginCallback
            )
        }
    }
}