package yapp.android1.delibuddy.ui.address

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.ui.home.viewmodel.PartiesViewModel
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.CoordToAddressUseCase
import yapp.android1.domain.interactor.usecase.SearchAddressUseCase
import javax.inject.Inject

sealed class AddressSharedEvent : Event {
    class SelectAddress(val address: Address) : AddressSharedEvent()
    class SearchAddress(val query: String) : AddressSharedEvent()
    class CoordToAddress(val lat: Double, val lng: Double) : AddressSharedEvent()
    object SelectCurrentLocation : AddressSharedEvent()
}

@HiltViewModel
class AddressSharedViewModel @Inject constructor(
    private val searchAddressUseCase: SearchAddressUseCase,
    private val coordToAddressUseCase: CoordToAddressUseCase,
) : BaseViewModel<Event>() {
    private var job: Job? = null

    private val _selectedAddress = MutableStateFlow<Address>(Address.DEFAULT)
    val selectedAddress: StateFlow<Address> = _selectedAddress

    private val _searchResults =
        MutableStateFlow<Pair<String, List<Address>>>(Pair("", emptyList()))
    val searchResults: StateFlow<Pair<String, List<Address>>> = _searchResults

    private val _isActivate = MutableEventFlow<Boolean>()
    val isActivate: EventFlow<Boolean> = _isActivate

    private val _isCurrentLocation = MutableStateFlow<Boolean>(false)
    val isCurrentLocation: MutableStateFlow<Boolean> = _isCurrentLocation

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AddressSharedEvent.SelectAddress -> {
                _isCurrentLocation.value = false
                selectAddress(event.address)
            }

            is AddressSharedEvent.SearchAddress -> {
                searchAddress(event.query)
            }

            is AddressSharedEvent.CoordToAddress -> {
                convertCoordToAddress(event.lat, event.lng)
            }

            is AddressSharedEvent.SelectCurrentLocation -> {
                _isCurrentLocation.value = true
            }
        }
    }

    private fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    private fun searchAddress(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = searchAddressUseCase(query)) {
                is NetworkResult.Success -> {
                    val addressList = result.data.map {
                        Address.mapToAddress(it)
                    }
                    _searchResults.value = Pair(query, addressList)
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    private fun convertCoordToAddress(lat: Double, lng: Double) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = coordToAddressUseCase(Pair<Double, Double>(lat, lng))) {
                is NetworkResult.Success -> {
                    Timber.w("success to convert")
                    _selectedAddress.value = Address.mapToAddress(result.data)
                }

                is NetworkResult.Error -> {
                    _isActivate.emit(false)
                    Timber.w("fail to convert")
                    showToast("????????? ????????? ?????? ????????? ????????? ?????????")
                    handleError(result) { }
                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when (result.errorType) {
            is NetworkError.Unknown -> {
                showToast("??? ??? ?????? ????????? ??????????????????.")
            }
            is NetworkError.Timeout -> {
                showToast("?????? ?????? ????????? ??????????????????.")
            }
            is NetworkError.InternalServer -> {
                showToast("?????? ???????????? ????????? ??????????????????.")
            }
            is NetworkError.BadRequest -> {
                val code = (result.errorType as NetworkError.BadRequest).code
                val msg = (result.errorType as NetworkError.BadRequest).message
                showToast("?????? ?????? ${code}, $msg")
            }
        }
    }

    override fun onCleared() {
        job = null
        super.onCleared()
    }
}
