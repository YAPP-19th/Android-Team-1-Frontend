package yapp.android1.delibuddy.util.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

object CheckPermission {
    fun checkPermission(
        context: AppCompatActivity,
        permissionCallback: (isGranted: Boolean) -> Unit,
        vararg type: PermissionType
    ) {
        val requestedPermissions: List<String> = getPermissionsFlag(type)

        if (requestedPermissions.isEmpty())
            return

        val requestPermissionLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted =
                permissions.entries.filter { it.value }.size == requestedPermissions.size
            permissionCallback(isGranted)
        }

        when {
            checkPermissionIsGranted(context, requestedPermissions) ->
                permissionCallback(true)
            checkAnyPermissionIsDenied(context, requestedPermissions) -> {
                
            }
            else -> requestPermissionLauncher.launch(requestedPermissions.toTypedArray())
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