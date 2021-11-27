package yapp.android1.delibuddy.ui.login

import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken

object KaKaoLoginCallback {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null) {
            Log.e("TAG", "로그인 실패", error)

        } else if(token != null) {
            Log.i("TAG", "로그인 성공 ${token.accessToken}")
        }
    }

}