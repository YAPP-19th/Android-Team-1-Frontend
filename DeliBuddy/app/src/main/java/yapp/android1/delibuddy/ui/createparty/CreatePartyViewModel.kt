package yapp.android1.delibuddy.ui.createparty

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
import yapp.android1.delibuddy.model.Category
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.interactor.usecase.CategoryListUseCase
import javax.inject.Inject

sealed class CreatePartyEvent : Event {
    class ChangeFlagsEvent(
        val partyElement: PartyElement,
        val isValid: Boolean
    ) : CreatePartyEvent()

    class SelectedAddressEvent(
        val address: Address?
    ) : CreatePartyEvent()

    object ClearAddressEvent : CreatePartyEvent()
    object CheckFlagsEvent : CreatePartyEvent()
    object CreatePartyClickEvent : CreatePartyEvent()
}

@HiltViewModel
class CreatePartyViewModel @Inject constructor(
    private val categoryListUseCase: CategoryListUseCase,
) : BaseViewModel<CreatePartyEvent>() {
    private var job: Job? = null

    private val _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    private val _canCreateParty = MutableStateFlow<Boolean>(false)
    val canCreateParty: MutableStateFlow<Boolean> = _canCreateParty

    private val _invalidElement = MutableStateFlow<PartyElement>(PartyElement.NONE)
    val invalidElement: MutableStateFlow<PartyElement> = _invalidElement

    private val _categoryList = MutableStateFlow<Array<String>>(arrayOf())
    val categoryList: MutableStateFlow<Array<String>> = _categoryList

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
        getCategoryListFromServer()
    }

    private fun initCurrentAddress() {
        _currentAddress.value = DeliBuddyApplication.prefs.getCurrentUserAddress()
    }

    private fun getCategoryListFromServer() {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = categoryListUseCase.invoke()) {
                is NetworkResult.Success -> {
                    val categoryList = result.data.map {
                        Category.mapToCategory(it)
                    }
                    val list = (categoryList.map { it.name }).toMutableList()
                    list.add(0, "음식 카테고리")
                    _categoryList.value = list.toTypedArray()
                    Timber.w("categoryList: ${_categoryList.value}")
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleEvent(event: CreatePartyEvent) {
        when (event) {
            is CreatePartyEvent.ClearAddressEvent -> {
                clearAddress()
            }

            is CreatePartyEvent.SelectedAddressEvent -> {
                changeCurrentAddress(event.address)
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

    private fun changeCurrentAddress(newAddress: Address?) {
        _currentAddress.value = newAddress
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