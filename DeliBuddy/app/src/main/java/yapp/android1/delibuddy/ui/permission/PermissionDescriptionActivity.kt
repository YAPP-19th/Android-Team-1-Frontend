package yapp.android1.delibuddy.ui.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding
import yapp.android1.delibuddy.ui.MainActivity
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType

@AndroidEntryPoint
class PermissionDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() = with(binding) {
        btnPermission.setOnClickListener {
            PermissionManager.requestPermission(
                this@PermissionDescriptionActivity,
                PermissionType.LOCATION
            ) {
                when (it) {
                    PermissionState.GRANTED -> intentMain()
                    PermissionState.DENIED -> showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun intentMain() {
        intentTo(MainActivity::class.java)
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(this).apply {
            negativeCallback = {
                finish()
            }
        }
        permissionDialog.show(this.supportFragmentManager, null)
    }
}