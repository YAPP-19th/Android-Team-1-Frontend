package yapp.android1.delibuddy.util.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yapp.android1.delibuddy.util.intentTo

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

        val bundle = Bundle().apply {
            putParcelable(
                "bundle", PermissionBundle(
                    isRequestPermission = isRequestPermission,
                    permissions = getPermissionsFlag(type)
                )
            )
        }
        context.intentTo(PermissionActivity::class.java, bundle)
    }
}