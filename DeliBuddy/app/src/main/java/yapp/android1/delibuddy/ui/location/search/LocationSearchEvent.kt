package yapp.android1.delibuddy.ui.location.search

import yapp.android1.delibuddy.model.Event

sealed class LocationSearchEvent : Event {
    class SearchAddress(val query: String) : LocationSearchEvent()
}