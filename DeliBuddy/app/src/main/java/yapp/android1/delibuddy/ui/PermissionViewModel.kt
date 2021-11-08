package yapp.android1.delibuddy.ui

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class PermissionViewModel : ViewModel() {
    private val TAG = "PermissionViewModel"

    val permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Log.w(TAG, "Permission Granted!")
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Log.w(TAG, "Permission Denied!")
        }
    }

    fun checkPermissions() {
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