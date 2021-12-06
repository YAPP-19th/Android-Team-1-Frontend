package yapp.android1.delibuddy.ui.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionDescriptionBinding
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.HomeActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType

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
                    PermissionState.GRANTED -> intentMain()
                    PermissionState.DENIED -> showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun intentMain() {
        //intentTo(HomeActivity::class.java)
        intentTo(AddressActivity::class.java)
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