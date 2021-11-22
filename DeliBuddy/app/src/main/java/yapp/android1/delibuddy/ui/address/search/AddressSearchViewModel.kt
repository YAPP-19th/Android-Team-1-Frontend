package yapp.android1.delibuddy.ui.address.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.interactor.usecase.SearchAddressUseCase
import javax.inject.Inject

@HiltViewModel
class AddressSearchViewModel @Inject constructor(
    private val searchAddressUseCase: SearchAddressUseCase
) : BaseViewModel<AddressSearchEvent>() {
    private var job: Job? = null

    private val _searchResult = MutableStateFlow<Pair<String, List<Address>>>(Pair("", emptyList()))
    val searchResult: StateFlow<Pair<String, List<Address>>> = _searchResult

    override suspend fun handleEvent(event: AddressSearchEvent) {
        when (event) {
            is AddressSearchEvent.SearchAddress -> {
                job?.cancel()
                job = viewModelScope.launch {
                    val addressList = searchAddressUseCase(event.query)
                    _searchResult.value = Pair(event.query, addressList)
                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
    }

    override fun onCleared() {
        job = null
    }
}