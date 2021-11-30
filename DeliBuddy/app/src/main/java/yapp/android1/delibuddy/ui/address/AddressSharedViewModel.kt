package yapp.android1.delibuddy.ui.address

import kotlinx.coroutines.flow.MutableStateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address

class AddressSharedViewModel : BaseViewModel<Event>() {
    private var _selectedAddress = MutableStateFlow<Address>(
        Address(
            addressName = "서울역",
            roadAddress = "서울특별시 중구 한강대로 405",
            address = "",
            lat = 37.5283169,
            lon = 126.9294254
        )
    )
    val selectedAddress: MutableStateFlow<Address> = _selectedAddress

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    override suspend fun handleEvent(event: Event) {

    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {

    }
}