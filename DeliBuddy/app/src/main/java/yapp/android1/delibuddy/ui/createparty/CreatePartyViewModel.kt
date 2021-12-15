package yapp.android1.delibuddy.ui.createparty

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult

sealed class CreatePartyEvent : Event {
    class ChangeFlagsEvent(
        val partyElement: PartyElement,
        val isValid: Boolean
    ) : CreatePartyEvent()

    object ClearAddressEvent : CreatePartyEvent()
    object CheckFlagsEvent : CreatePartyEvent()
    object CreatePartyClickEvent : CreatePartyEvent()
}

class CreatePartyViewModel : BaseViewModel<CreatePartyEvent>() {
    private var _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    private var _canCreateParty = MutableStateFlow<Boolean>(false)
    val canCreateParty: MutableStateFlow<Boolean> = _canCreateParty

    private var _invalidElement = MutableStateFlow<PartyElement>(PartyElement.NONE)
    val invalidElement: MutableStateFlow<PartyElement> = _invalidElement

    private var createPartyFlags: MutableMap<PartyElement, Boolean> = mutableMapOf(
        PartyElement.TITLE to false,
        PartyElement.CATEGORY to false,
        PartyElement.TIME to true,
        PartyElement.MEMBER to false,
        PartyElement.CHAT_URL to false,
        PartyElement.ADDRESS to false,
        PartyElement.BODY to false
    )

    init {
        initCurrentAddress()
    }

    private fun initCurrentAddress() {
        _currentAddress.value = DeliBuddyApplication.prefs.getCurrentUserAddress()
    }

    override suspend fun handleEvent(event: CreatePartyEvent) {
        when (event) {
            is CreatePartyEvent.ClearAddressEvent -> {
                clearAddress()
            }

            is CreatePartyEvent.ChangeFlagsEvent -> {
                changeFlag(event.partyElement, event.isValid)
            }

            is CreatePartyEvent.CheckFlagsEvent -> {
                checkCanCreateParty()
            }

            is CreatePartyEvent.CreatePartyClickEvent -> {
                checkAndCreate()
            }
        }
    }

    private fun clearAddress() {
        _currentAddress.value = null
    }

    private fun changeFlag(partyElement: PartyElement, isValid: Boolean) {
        createPartyFlags[partyElement] = isValid
        checkCanCreateParty()
    }

    private fun checkCanCreateParty(): PartyElement {
        for (partyElement in createPartyFlags.keys) {
            if (createPartyFlags[partyElement] == false) {
                _canCreateParty.value = false
                return partyElement
            }
        }
        _canCreateParty.value = true
        return PartyElement.NONE
    }

    private suspend fun checkAndCreate() {
        val i = checkCanCreateParty()

        if (i == PartyElement.NONE) {
            showToast("성공")
        } else {
            _invalidElement.value = i
            showToast("파티글 생성에 실패하였습니다")
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        // TODO("Not yet implemented")
    }
}