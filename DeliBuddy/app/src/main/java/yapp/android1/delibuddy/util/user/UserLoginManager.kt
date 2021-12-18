package yapp.android1.delibuddy.util.user

import android.content.Context
import com.kakao.sdk.user.UserApiClient
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.model.User
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

class UserLoginManager(
    private val context: Context,
    private val kakaoLoginModule: KakaoLoginModule,
    private val prefs: SharedPreferencesManager,
) {
    var user: User? = null

    fun isAlreadyLoggedInKakao(): Boolean {
        if (user != null) {
            return true
        }
        return UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)
    }

    fun kakaoLogin(call: (Boolean, String?, String?) -> Unit) {
        kakaoLoginModule.kakaoLogin(call)
    }

    fun setDeliBuddyAuth(auth: Auth) {
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