package yapp.android1.delibuddy.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.core.text.set
import androidx.core.text.toSpannable
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityMainBinding
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {
    private val viewModel: PermissionViewModel by viewModels()

    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTextColor()

        binding.apply {
            btnPermission.setOnClickListener {
                viewModel.checkPermissions()
            }
        }
    }

    private fun initTextColor() = with(binding) {
        val color = resources.getColor(R.color.main_orange)
        val foregroundColorSpan = ForegroundColorSpan(color)

        val locationSpan = tvPermissionLocation.text.toSpannable()
        locationSpan.setSpan(foregroundColorSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        val foregroundSpan = tvPermissionForeground.text.toSpannable()
        foregroundSpan.setSpan(foregroundColorSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}