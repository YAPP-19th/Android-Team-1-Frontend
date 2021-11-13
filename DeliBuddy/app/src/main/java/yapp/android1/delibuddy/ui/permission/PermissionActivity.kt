package yapp.android1.delibuddy.ui.permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding
import yapp.android1.delibuddy.ui.MainActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.permission.CheckPermission

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() = with(binding) {
        btnPermission.setOnClickListener {
            CheckPermission.checkPermission(
                this@PermissionActivity,
                {
                    when(it) {
                        true -> permissionGranted()
                        false -> permissionDenied()
                    }
                }

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