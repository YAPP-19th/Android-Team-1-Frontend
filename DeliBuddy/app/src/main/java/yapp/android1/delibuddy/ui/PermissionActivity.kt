package yapp.android1.delibuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.core.text.toSpannable
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {
    private val viewModel: PermissionViewModel by viewModels()

    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPermission.setOnClickListener {
                viewModel.checkPermissions()
            }
        }
    }
}