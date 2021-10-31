package yapp.android1.delibuddy.base

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.base.TestViewModel.TestEvent.*
import yapp.android1.delibuddy.model.Event

@HiltViewModel
internal class TestViewModel : BaseViewModel<TestViewModel.TestEvent>() {

    private val _number = MutableStateFlow<Int>(0)
    val number: StateFlow<Int> get() = _number

    sealed class TestEvent : Event {
        object OnIncreaseClicked : TestEvent()
        object OnDecreaseClicked : TestEvent()
    }

    override suspend fun handleEvent(event: TestEvent) {
        when(event) {
            is OnIncreaseClicked -> _number.value = _number.value + 1
            is OnDecreaseClicked -> _number.value = _number.value - 1
        }
    }

}