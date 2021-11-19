package yapp.android1.delibuddy.ui.location.search

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
class LocationSearchViewModel @Inject constructor(
    private val searchAddressUseCase: SearchAddressUseCase
) : BaseViewModel<LocationSearchEvent>() {
    private var job: Job? = null
    private val _addressList = MutableStateFlow<List<Address>>(emptyList())
    val addressList: StateFlow<List<Address>> = _addressList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    override suspend fun handleEvent(event: LocationSearchEvent) {
        when (event) {
            is LocationSearchEvent.SearchAddress -> {
                job?.cancel()
                job = viewModelScope.launch {
                    _searchQuery.value = event.query
                    _addressList.value = searchAddressUseCase(event.query)
                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
    }
}