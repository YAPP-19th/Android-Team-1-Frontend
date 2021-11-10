package yapp.android1.delibuddy.ui

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult

class PermissionViewModel : BaseViewModel<PermissionViewModel.PermissionActivityEvent>() {

    private val _isPermissionGranted =  MutableStateFlow<Boolean>(false)
    val isPermissionGranted = _isPermissionGranted as StateFlow<Boolean>

    override suspend fun handleEvent(event: PermissionActivityEvent) {
        when(event) {
            is PermissionActivityEvent.OnCheckPermission -> {
                _isPermissionGranted.value = event.isGranted
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) = Unit

    sealed class PermissionActivityEvent : Event {
        class OnCheckPermission(val isGranted: Boolean) : PermissionActivityEvent()
    }
}