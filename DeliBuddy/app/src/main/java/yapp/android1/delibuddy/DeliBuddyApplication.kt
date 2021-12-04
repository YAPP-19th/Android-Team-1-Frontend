package yapp.android1.delibuddy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

@HiltAndroidApp
internal class DeliBuddyApplication : Application() {
    companion object {
        lateinit var prefs: SharedPreferencesManager
    }

    override fun onCreate() {
        super.onCreate()
        prefs = SharedPreferencesManager(applicationContext)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_APIKEY_ID)

        plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, "Debug[$tag]", message, t)
            }
        })
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}