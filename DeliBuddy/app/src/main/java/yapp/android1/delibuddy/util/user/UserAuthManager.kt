package yapp.android1.delibuddy.util.user

import android.app.Activity
import android.content.Context
import com.kakao.sdk.user.UserApiClient
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.model.User
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

class UserAuthManager(
    private val context: Context,
    private val kakaoLoginModule: KakaoLoginModule,
    private val authManagementModule: AuthManagementModule,
    private val prefs: SharedPreferencesManager,
) {
    var user: User? = null

    fun isAlreadyLoggedInKakao(): Boolean {
        if (user != null) {
            return true
        }
        return UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)
    }

    fun kakaoLogin(context: Activity, call: (Boolean, String?, String?) -> Unit) {
        kakaoLoginModule.kakaoLogin(context, call)
    }

    fun checkAuthStatus(callback: (Int) -> Unit) {
        authManagementModule.checkAuthStatus(getDeliBuddyUserToken(), callback)
    }

    fun setDeliBuddyAuth(auth: Auth) {
        prefs.saveAuthData(auth)
    }

    fun getDeliBuddyAuth(): Auth {
        return prefs.getAuth()
    }

    fun getDeliBuddyUserToken(): String {
        return prefs.getUserToken()
    }

    fun getDeliBuddyUserId(): Int {
        return prefs.getUserId()
    }
}