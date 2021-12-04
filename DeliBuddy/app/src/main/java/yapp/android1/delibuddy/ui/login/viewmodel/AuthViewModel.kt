package yapp.android1.delibuddy.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import yapp.android1.domain.interactor.usecase.FetchAuthUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val fetchAuthUseCase: FetchAuthUseCase
): ViewModel() {

    fun test(token: String) {
        viewModelScope.launch {
            val response = fetchAuthUseCase.run(token)
        }
    }
}