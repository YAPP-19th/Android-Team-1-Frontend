package yapp.android1.delibuddy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class DeliBuddyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}