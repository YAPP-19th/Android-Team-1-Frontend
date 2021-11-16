package yapp.android1.delibuddy.util.permission

import android.Manifest

enum class PermissionType {
    LOCATION
}

fun getPermissionsFlag(type: Array<out PermissionType>): List<String> {
    return type.map {
        when(it) {
            PermissionType.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
        }
    }
}