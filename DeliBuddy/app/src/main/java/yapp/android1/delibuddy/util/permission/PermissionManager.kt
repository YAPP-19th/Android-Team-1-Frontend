package yapp.android1.delibuddy.util.permission

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

object PermissionManager {
    fun requestPermission(
        context: AppCompatActivity,
        vararg type: PermissionType,
        permissionCallback: (state: PermissionState) -> Unit
    ) {
        launchPermissionActivity(context, true, type, permissionCallback)
    }

    fun checkPermission(
        context: AppCompatActivity,
        vararg type: PermissionType,
        permissionCallback: (state: PermissionState) -> Unit
    ) {
        launchPermissionActivity(context, false, type, permissionCallback)
    }

    private fun launchPermissionActivity(
        context: AppCompatActivity,
        isRequestPermission: Boolean,
        type: Array<out PermissionType>,
        permissionCallback: (state: PermissionState) -> Unit
    ) {
        PermissionActivity.callback = permissionCallback

        val intent = Intent(context, PermissionActivity::class.java)
        intent.putExtra("bundle", PermissionBundle(
            isRequestPermission = isRequestPermission,
            permissions = getPermissionsFlag(type)
        ))
        context.startActivity(intent)
    }
}