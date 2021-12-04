package yapp.android1.delibuddy.ui.address.search

import yapp.android1.delibuddy.model.Event

sealed class AddressSearchEvent : Event {
    class SearchAddress(val query: String) : AddressSearchEvent()
    class SearchError() : AddressSearchEvent()
}