package yapp.android1.delibuddy.ui.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionDescriptionBinding
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.login.LoginActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType
import javax.inject.Inject

@AndroidEntryPoint
class PermissionDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionDescriptionBinding.inflate(layoutInflater)
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
                    PermissionState.GRANTED -> intentLogin()
                    PermissionState.DENIED -> showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun intentLogin() {
        intentTo(LoginActivity::class.java)
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(this).apply {
            negativeCallback = {
                intentLogin()
            }
        }
        permissionDialog.show(this.supportFragmentManager, null)
    }
}