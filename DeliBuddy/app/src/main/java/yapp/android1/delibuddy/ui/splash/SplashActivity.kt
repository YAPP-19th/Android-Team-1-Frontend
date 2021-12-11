package yapp.android1.delibuddy.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.ui.login.LoginActivity
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
        setContentView(binding.root)
        intentTo(HomeActivity::class.java)

//        PermissionManager.checkPermission(this, PermissionType.LOCATION) {
//            when (it) {
//                PermissionState.NEED_PERMISSION -> intentPermissionDescription()
//                PermissionState.DENIED -> showPermissionDeniedDialog()
//                PermissionState.GRANTED -> intentLogin()
//            }
//        }
    }

    private fun intentLogin() {
        intentJob = lifecycleScope.launch {
            delay(2000L)
            intentTo(LoginActivity::class.java)
        }
    }

    private fun intentPermissionDescription() {
        intentJob = lifecycleScope.launch {
            delay(2000L)
           // intentTo(PermissionDescriptionActivity::class.java)
        }
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(this).apply {
            negativeCallback = {
                finish()
            }
        }
        permissionDialog.show(this.supportFragmentManager, null)
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
