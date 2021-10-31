package yapp.android1.delibuddy.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.model.Event


abstract class BaseViewModel<E : Event> : ViewModel() {

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _showToast = MutableSharedFlow<String>()
    val showToast: SharedFlow<String> = _showToast

    protected abstract suspend fun handleEvent(event: E)

    fun occurEvent(event: E) = viewModelScope.launch {
        handleEvent(event)
    }

    fun showProgress() {
        _loading.value = true
    }

    fun hideProgress() {
        _loading.value = false
    }

    suspend fun showToast(message: String) {
        _showToast.emit(message)
    }

}