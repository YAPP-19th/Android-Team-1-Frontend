package yapp.android1.delibuddy.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.ui.MainActivity
import yapp.android1.delibuddy.ui.permission.PermissionDescriptionActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var intentJob: Job? = null
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash)

        PermissionManager.checkPermission(this, PermissionType.LOCATION) {
            when(it) {
                PermissionState.NEED_DESCRIPTION -> intentPermissionDescription()
                PermissionState.GRANTED -> intentMain()
            }
        }
    }

    private fun intentMain() {
        intentJob = lifecycleScope.launch {
            delay(2000L)
            intentTo(MainActivity::class.java)
        }
    }

    private fun intentPermissionDescription() {
        intentJob = lifecycleScope.launch {
            delay(2000L)
            intentTo(PermissionDescriptionActivity::class.java)
        }
    }

    override fun onBackPressed() {
        intentJob?.cancel()
        super.onBackPressed()
    }

    override fun onDestroy() {
        intentJob?.cancel()
        super.onDestroy()
    }
}