package yapp.android1.delibuddy.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError

typealias RetryAction = () -> Unit

abstract class BaseViewModel<E : Event> : ViewModel() {

    private var job: Job? = null

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _showToast = MutableEventFlow<String>()
    val showToast: EventFlow<String> = _showToast

    protected abstract suspend fun handleEvent(event: E)

    protected abstract suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?)

    fun occurEvent(event: E) {
        job = viewModelScope.launch {
            handleEvent(event)
        }
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

    override fun onCleared() {
        job = null
        super.onCleared()
    }
}