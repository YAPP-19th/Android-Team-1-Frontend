package yapp.android1.delibuddy.ui.permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding
import yapp.android1.delibuddy.ui.MainActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.CheckPermission
import yapp.android1.delibuddy.util.permission.PermissionType
import yapp.android1.delibuddy.util.permission.getPermissionsFlag
import java.util.jar.Manifest

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var requestedPermissions = emptyList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPermissionLauncher()
        init()
    }

    private fun initPermissionLauncher() {
        requestedPermissions = getPermissionsFlag(
            arrayOf(PermissionType.LOCATION)
        )

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when (permissions.entries.filter { it.value }.size == requestedPermissions.size) {
                true -> permissionGranted()
                false -> permissionDenied()
            }
        }
    }

    private fun init() = with(binding) {
        btnPermission.setOnClickListener {
            CheckPermission.checkPermission(
                this@PermissionActivity,
                requestPermissionLauncher,
                {
                    when (it) {
                        true -> permissionGranted()
                        false -> permissionDenied()
                    }
                },
                requestedPermissions
            )
        }
    }

    private fun permissionGranted() {
        intentTo(MainActivity::class.java)
    }

    private fun permissionDenied() {
        // Dialog
    }
}