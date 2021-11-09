package yapp.android1.delibuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private val viewModel: PermissionViewModel by viewModels()

    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            viewModel.checkPermissions()
        }
    }
}