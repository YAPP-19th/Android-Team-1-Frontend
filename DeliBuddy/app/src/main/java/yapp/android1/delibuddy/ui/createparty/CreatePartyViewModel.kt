package yapp.android1.delibuddy.ui.createparty

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult

sealed class CreatePartyEvent : Event {
    object ClearAddressEvent : CreatePartyEvent()
    object SearchAddressEvent : CreatePartyEvent()
    class ChangeFlagsEvent(val flag: Flag, val tf: Boolean) : CreatePartyEvent()
    object CheckFlagsEvent : CreatePartyEvent()
    object ClickCreatePartyEvent : CreatePartyEvent()
}

enum class Flag {
    TITLE,
    CATEGORY,
    TIME,
    MEMBER,
    CHAT_URL,
    ADDRESS,
    BODY,
    NONE
}

class CreatePartyViewModel : BaseViewModel<CreatePartyEvent>() {
    private var _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    private var _searchAddressIsClicked = MutableEventFlow<Boolean>()
    val searchAddressIsClicked: MutableEventFlow<Boolean> = _searchAddressIsClicked

    private var _createPartyFlags: Array<Boolean> =
        arrayOf(
            false, // title
            false, // category
            true, // time
            false, // member
            false, // chat url
            false, // address
            false // body
        )

    private var _canCreateParty = MutableStateFlow<Boolean>(false)
    val canCreateParty: MutableStateFlow<Boolean> = _canCreateParty

    private var _wrong = MutableStateFlow<Flag>(Flag.NONE)
    val wrong: MutableStateFlow<Flag> = _wrong

    init {
        getCurrentAddress()
    }

    private fun getCurrentAddress() {
        _currentAddress.value = DeliBuddyApplication.prefs.getCurrentUserAddress()
    }

    override suspend fun handleEvent(event: CreatePartyEvent) {
        when (event) {
            is CreatePartyEvent.ClearAddressEvent -> {
                clearAddress()
            }

            is CreatePartyEvent.SearchAddressEvent -> {
                searchAddress()
            }

            is CreatePartyEvent.ChangeFlagsEvent -> {
                changeFlag(event.flag, event.tf)
            }

            is CreatePartyEvent.CheckFlagsEvent -> {
                checkCanCreateParty()
            }

            is CreatePartyEvent.ClickCreatePartyEvent -> {
                checkAndCreate()
            }
        }
    }

    private fun clearAddress() {
        _currentAddress.value = null
    }

    private suspend fun searchAddress() {
        _searchAddressIsClicked.emit(true)
    }

    private suspend fun checkAndCreate() {
        val i = checkCanCreateParty()

        if (i == 7) {
            showToast("성공")
        } else {
            _wrong.value = Flag.values()[i]
        }
    }

    private fun checkCanCreateParty(): Int {
        for (index in _createPartyFlags.indices) {
            if (!_createPartyFlags[index]) {
                _canCreateParty.value = false
                return index
            }
        }
        _canCreateParty.value = true
        return 7
    }

    private fun changeFlag(flag: Flag, tf: Boolean) {
        _createPartyFlags[flag.ordinal] = tf
        checkCanCreateParty()
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        // TODO("Not yet implemented")
    }
}