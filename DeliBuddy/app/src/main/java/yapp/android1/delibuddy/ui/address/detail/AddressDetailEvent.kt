package yapp.android1.delibuddy.ui.address.detail

import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.entity.Address

sealed class AddressDetailEvent : Event {
    class SaveAddress(val address: Address) : AddressDetailEvent()
}