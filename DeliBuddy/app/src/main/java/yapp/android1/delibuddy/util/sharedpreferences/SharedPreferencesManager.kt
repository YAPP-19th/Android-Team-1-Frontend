package yapp.android1.delibuddy.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper.set
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper.get
import yapp.android1.domain.entity.Address

class SharedPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = SharedPreferenceHelper.getDefaultPrefs(context)

    fun saveUserAddress(address: Address) {
        prefs["addressName"] = address.addressName
        prefs["address"] = address.address
        prefs["roadAddress"] = address.roadAddress
        prefs["lat"] = address.lat
        prefs["lon"] = address.lon
    }

    fun getCurrentUserAddress(): Address {
        return Address(
            addressName = prefs["addressName", "주소를 입력해 주세요"],
            address = prefs["address", "주소를 입력해 주세요"],
            roadAddress = prefs["roadAddress", "주소를 입력해 주세요"],
            lat = prefs["lat", 0.0],
            lon = prefs["lon", 0.0]
        )
    }
}
