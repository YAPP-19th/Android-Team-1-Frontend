package yapp.android1.delibuddy.ui

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.repeatOnLifecycle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding
import yapp.android1.delibuddy.ui.PermissionViewModel.PermissionActivityEvent.OnCheckPermission
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo
import java.util.*

class PermissionActivity : AppCompatActivity() {

    private val viewModel: PermissionViewModel by viewModels()

    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        initializeCollector()
    }

    private fun initializeCollector() = repeatOnStarted {
        viewModel.isPermissionGranted.collect { isGranted ->
            binding.btnPermission.setOnClickListener(initializeButtonListener(isGranted))
        }
    }

    private fun initializeButtonListener(isGranted: Boolean): (View) -> Unit {
        return if(isGranted) {
            { intentTo(MainActivity::class.java) }
        } else {
            { checkPermission() }
        }
    }

    private fun checkPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                viewModel.occurEvent(OnCheckPermission(true))
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                viewModel.occurEvent(OnCheckPermission(false))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("편리한 딜리버디 이용을 위해 권한을 허용해 주세요.\n [설정] > [권한] 에서 사용으로 활성화해 주세요.")
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE
                ).check()
        }
    }

}