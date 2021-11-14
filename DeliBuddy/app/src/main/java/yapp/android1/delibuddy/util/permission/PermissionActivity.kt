package yapp.android1.delibuddy.util.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

enum class PermissionState {
    GRANTED,
    DENIED,
    NEED_PERMISSION
}

@Parcelize
data class PermissionBundle(
    val isRequestPermission: Boolean,
    val permissions: List<String>
) : Parcelable

class PermissionActivity : AppCompatActivity() {
    companion object {
        var callback: ((isGranted: PermissionState) -> Unit)? = null
    }

    private val bundle: PermissionBundle? by lazy {
        intent.getParcelableExtra("bundle")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        bundle?.let { it ->
            if (permissions.entries.filter { it.value }.size == it.permissions.size) {
                permissionCallBack(PermissionState.GRANTED)
            } else {
                permissionCallBack(PermissionState.DENIED)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        bundle?.let {
            launchPermission(it.permissions)
        }
    }

    private fun launchPermission(permissions: List<String>) {
        if (permissions.isEmpty())
            finish()

        when {
            isAllPermissionGranted(permissions) ->
                permissionCallBack(PermissionState.GRANTED)
            bundle?.isRequestPermission == true -> {
                requestPermissionLauncher.launch(permissions.toTypedArray())
            }
            else -> {
                permissionCallBack(PermissionState.NEED_PERMISSION)
            }
        }
    }

    private fun isAllPermissionGranted(
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            val grantedState = ContextCompat.checkSelfPermission(this, it)
            if (grantedState != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun permissionCallBack(state: PermissionState) {
        callback?.let {
            it(state)
            finish()
        }
    }
}


