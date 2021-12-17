package yapp.android1.delibuddy.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper.get
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper.set

class SharedPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = SharedPreferenceHelper.getDefaultPrefs(context)

    fun saveUserAddress(address: Address) {
        prefs["addressName"] = address.addressName
        prefs["address"] = address.address
        prefs["roadAddress"] = address.roadAddress ?: ""
        prefs["addressDetail"] = address.addressDetail
        prefs["lat"] = address.lat
        prefs["lon"] = address.lng
    }

    fun getCurrentUserAddress(): Address? {
        return try {
            Address(
                addressName = prefs["addressName", "주소를 입력해 주세요"],
                address = prefs["address", "주소를 입력해 주세요"],
                roadAddress = prefs["roadAddress", "주소를 입력해 주세요"],
                addressDetail = prefs["addressDetail", "상세 주소를 입력해 주세요"],
                lat = prefs["lat", 0.0],
                lng = prefs["lon", 0.0]
            )
        } catch (e: NullPointerException) {
            null
        }
    }

    fun saveAuthData(auth: Auth) {
        prefs["userToken"] = auth.token
        prefs["userId"] = auth.userId
    }

    fun getAuth(): Auth {
        return Auth(
            getUserToken(),
            getUserId()
        )
    }

    fun getUserToken(): String {
        return prefs["userToken", ""]
    }

    fun getUserId(): Int {
        return prefs["userId", -1]
    }
}
