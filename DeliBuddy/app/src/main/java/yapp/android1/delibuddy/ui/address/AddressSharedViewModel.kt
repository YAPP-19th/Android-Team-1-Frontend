package yapp.android1.delibuddy.ui.address

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult

sealed class AddressSharedEvent : Event {
    class SelectAddress(val address: Address) : AddressSharedEvent()
}

class AddressSharedViewModel : BaseViewModel<Event>() {
    private var _selectedAddress = MutableStateFlow<Address>(
        Address(
            addressName = "서울역",
            roadAddress = "서울특별시 중구 한강대로 405",
            address = "",
            addressDetail = "",
            lat = 37.5283169,
            lng = 126.9294254
        )
    )
    val selectedAddress: StateFlow<Address> = _selectedAddress

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AddressSharedEvent.SelectAddress -> {
                selectAddress(event.address)
            }
        }
    }

    private fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {

    }
}