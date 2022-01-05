package yapp.android1.delibuddy.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import yapp.android1.domain.entity.UserEntity
import yapp.android1.domain.interactor.usecase.GetMyInfoUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : BaseViewModel<MyPageViewModel.MyPageEvent>() {

    private val _myInfo = MutableStateFlow<UserEntity?>(null)
    val myInfo: MutableStateFlow<UserEntity?> = _myInfo

    private val _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    sealed class MyPageEvent : Event

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            when (val result = getMyInfoUseCase.invoke()) {
                is NetworkResult.Success -> {
                    _myInfo.value = result.data
                    _currentAddress.value = DeliBuddyApplication.prefs.getCurrentUserAddress()
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleEvent(event: MyPageEvent) {}
    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {}
}
