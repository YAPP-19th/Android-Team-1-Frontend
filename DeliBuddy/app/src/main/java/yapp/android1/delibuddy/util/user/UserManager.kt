package yapp.android1.delibuddy.util.user

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.model.User
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

class UserManager(
    private val context: Context,
    private val prefs: SharedPreferencesManager
) {
    var user: User? = null

    private var loginCallback: ((
        isLoginSuccess: Boolean,
        errorMsg: String?,
        kakaoToken: String?
    ) -> Unit)? = null

    val isLogin: Boolean
        get() {
            if (user != null) {
                return true
            }
            return UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)
        }

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

    fun setDeliBuddyToken(auth: Auth) {
        prefs.saveAuthData(auth)
    }

    fun getDeliBuddyAuth(): Auth {
        return prefs.getAuth()
    }

    fun getDeliBuddyToken(): String {
        return prefs.getUserToken()
    }

    fun getDeliBuddyUserId(): Int {
        return prefs.getUserId()
    }
}