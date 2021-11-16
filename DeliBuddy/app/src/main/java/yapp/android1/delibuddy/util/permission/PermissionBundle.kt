package yapp.android1.delibuddy.util.permission

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionBundle(
    val isRequestPermission: Boolean,
    val permissions: List<String>
) : Parcelable