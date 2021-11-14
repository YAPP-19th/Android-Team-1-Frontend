package yapp.android1.delibuddy.util.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

object PermissionManager {
    fun checkPermission(
        context: AppCompatActivity,
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
        permissionCallback: (isGranted: Boolean) -> Unit,
        requestedPermissions: List<String>
    ) {
        if (requestedPermissions.isEmpty())
            return

        when {
            isPermissionGranted(context, requestedPermissions) ->
                permissionCallback(true)
            isPermissionDenied(context, requestedPermissions) -> {
                permissionCallback(false)
            }
            else -> {
                // 권한 요청을 한 번도 하지 않은 경우, 시스템 팝업으로 권한 요청
                requestPermissionLauncher.launch(requestedPermissions.toTypedArray())
            }
        }
    }

    private fun isPermissionGranted(
        context: AppCompatActivity,
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            val isGranted = ContextCompat.checkSelfPermission(context, it)
            if (isGranted != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun isPermissionDenied(
        context: AppCompatActivity,
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            if(shouldShowRequestPermissionRationale(context, it)) {
                return true
            }
        }
        return false
    }
}