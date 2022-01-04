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
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.entity.PartyCreationRequestEntity
import yapp.android1.domain.interactor.usecase.CategoryListUseCase
import yapp.android1.domain.interactor.usecase.CreatePartyUseCase
import javax.inject.Inject

sealed class CreatePartyEvent : Event {
    class ChangeFlags(
        val partyElement: PartyElement,
        val isValid: Boolean
    ) : CreatePartyEvent()

    class SelectedAddress(
        val address: Address?
    ) : CreatePartyEvent()

    class SelectedCategory(val index: Int) : CreatePartyEvent()

    object ClearAddress : CreatePartyEvent()
    object CheckFlags : CreatePartyEvent()
    class CreatePartyClick(
        val newParty: PartyCreationRequestEntity
    ) : CreatePartyEvent()
}

@HiltViewModel
class CreatePartyViewModel @Inject constructor(
    private val categoryListUseCase: CategoryListUseCase,
    private val createPartyUseCase: CreatePartyUseCase,
) : BaseViewModel<CreatePartyEvent>() {
    private var job: Job? = null

    private val _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    private val _canCreateParty = MutableStateFlow<Boolean>(false)
    val canCreateParty: MutableStateFlow<Boolean> = _canCreateParty

    private val _invalidElement = MutableStateFlow<PartyElement>(PartyElement.NONE)
    val invalidElement: MutableStateFlow<PartyElement> = _invalidElement

    private val _categoryList = MutableStateFlow<List<Category>>(listOf())
    val categoryList: MutableStateFlow<List<Category>> = _categoryList

    private val _currentCategoryIndex = MutableStateFlow<Int>(0)
    val currentCategoryIndex: MutableStateFlow<Int> = _currentCategoryIndex

    private val _isSuccessToCreateParty = MutableEventFlow<Boolean>()
    val isSuccessToCreateParty: MutableEventFlow<Boolean> = _isSuccessToCreateParty

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
                    _categoryList.value = categoryList
                    Timber.w("categoryList: ${_categoryList.value}")
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleEvent(event: CreatePartyEvent) {
        when (event) {
            is CreatePartyEvent.ClearAddress -> clearAddress()
            is CreatePartyEvent.SelectedAddress -> changeCurrentAddress(event.address)
            is CreatePartyEvent.ChangeFlags -> changeFlag(event.partyElement, event.isValid)
            is CreatePartyEvent.CheckFlags -> checkCanCreateParty()
            is CreatePartyEvent.CreatePartyClick -> checkAndCreate(event.newParty)
            is CreatePartyEvent.SelectedCategory -> selectCategory(event.index)
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

    private suspend fun checkAndCreate(newParty: PartyCreationRequestEntity) {
        val i = checkCanCreateParty()

        if (i == PartyElement.NONE) {
            Timber.w("new party: ${newParty.toString()}")
            createParty(newParty)
        } else {
            _invalidElement.value = i
            showToast("파티글 생성에 실패하였습니다")
        }
    }

    private fun createParty(newParty: PartyCreationRequestEntity) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = createPartyUseCase.invoke(newParty)) {
                is NetworkResult.Success -> {
                    val resultParty = PartyInformation.toPartyInformation(result.data)
                    Timber.w("resultParty: ${resultParty.title}")
                    showToast("파티글 생성에 성공하였습니다")
                    _isSuccessToCreateParty.emit(true)
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    private fun selectCategory(idx: Int) {
        _currentCategoryIndex.value = idx
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when (result.errorType) {
            is NetworkError.Unknown -> {
                showToast("알 수 없는 에러가 발생했습니다.")
            }
            is NetworkError.Timeout -> {
                showToast("타임 아웃 에러가 발생했습니다.")
            }
            is NetworkError.InternalServer -> {
                showToast("내부 서버에서 오류가 발생했습니다.")
            }
            is NetworkError.BadRequest -> {
                val code = (result.errorType as NetworkError.BadRequest).code
                val msg = (result.errorType as NetworkError.BadRequest).message
                showToast("에러 코드 ${code}, $msg")
            }
        }
    }
}