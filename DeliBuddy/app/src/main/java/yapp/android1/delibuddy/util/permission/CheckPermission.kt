package yapp.android1.delibuddy.util.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment

object CheckPermission {
    fun checkPermission(
        context: AppCompatActivity,
        requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
        permissionCallback: (isGranted: Boolean) -> Unit,
        requestedPermissions: List<String>
    ) {
        if (requestedPermissions.isEmpty())
            return

        when {
            checkPermissionIsGranted(context, requestedPermissions) ->
                permissionCallback(true)
            checkAnyPermissionIsDenied(context, requestedPermissions) -> {
                requestPermissionLauncher.launch(requestedPermissions.toTypedArray())
            }
            else -> {
                val permissionDialog = PermissionDialogFragment(context)
                permissionDialog.show(context.supportFragmentManager, null)
            }
        }
    }

    private fun checkPermissionIsGranted(
        context: AppCompatActivity,
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun checkAnyPermissionIsDenied(
        context: AppCompatActivity,
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            if(shouldShowRequestPermissionRationale(context, it)) {
                return false
            }
        }
        return true
    }
}