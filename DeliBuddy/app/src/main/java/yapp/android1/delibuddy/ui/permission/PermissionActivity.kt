package yapp.android1.delibuddy.ui.permission

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.databinding.ActivityPermissionBinding
import yapp.android1.delibuddy.ui.MainActivity
import yapp.android1.delibuddy.util.intentTo

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPermission.setOnClickListener {
                checkPermission()
            }
        }
    }

    private fun checkPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                intentTo(MainActivity::class.java)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // TODO: Dialog
            }
        }

        val permissionBuilder = TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("편리한 딜리버디 이용을 위해 권한을 허용해 주세요.\n[설정] > [권한] 에서 사용으로 활성화해 주세요.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionBuilder.setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE
            )
        } else {
            permissionBuilder.setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        permissionBuilder.check()
    }
}