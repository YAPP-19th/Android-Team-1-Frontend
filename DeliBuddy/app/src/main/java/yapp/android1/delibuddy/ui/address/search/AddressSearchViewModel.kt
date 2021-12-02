package yapp.android1.delibuddy.ui.address.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.SearchAddressUseCase
import javax.inject.Inject

sealed class AddressSearchEvent : Event {
    class SearchAddress(val query: String) : AddressSearchEvent()
}

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    private val searchAddressUseCase: SearchAddressUseCase
) : BaseViewModel<Event>() {
    private var job: Job? = null

    private val _searchResult = MutableStateFlow<Pair<String, List<Address>>>(Pair("", emptyList()))
    val searchResult: StateFlow<Pair<String, List<Address>>> = _searchResult

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AddressSearchEvent.SearchAddress -> {
                searchAddress(event.query)
            }
        }
    }

    private fun searchAddress(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            when(val result = searchAddressUseCase(query)) {
                is NetworkResult.Success -> {
                    val addressList = result.data
                    _searchResult.value = Pair(query, addressList)
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when (result.errorType) {
            is NetworkError.Unknown -> {
                showToast("알 수 없는 에러가 발생했습니다.")
            }
            is NetworkError.Timeout -> {
                showToast("타임 아웃 에러가 발생했습니다.")
            }
            is NetworkError.InternalServer -> {
                showToast("내부 서버에서 오류가 발생했습니다.")
            }
            is NetworkError.BadRequest -> {
                val code = (result.errorType as NetworkError.BadRequest).code
                val msg = (result.errorType as NetworkError.BadRequest).message
                showToast("에러 코드 ${code}, $msg")
            }
        }
    }

    override fun onCleared() {
        job = null
        super.onCleared()
    }
}