package yapp.android1.delibuddy.ui.home.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.interactor.usecase.GetPartiesInCircleUseCase
import javax.inject.Inject

typealias LocationRange = Pair<String, Int>

@HiltViewModel
class PartiesViewModel @Inject constructor(
    private val getPartiesInCircleUseCase: GetPartiesInCircleUseCase
) : BaseViewModel<Event>() {

    private val _partiesResult = MutableStateFlow<List<Party>>(emptyList())
    val partiesResult: StateFlow<List<Party>> = _partiesResult

    sealed class PartiesEvent : Event {
        class GetPartiesInCircle(val locationRange: LocationRange) : PartiesEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is PartiesEvent.GetPartiesInCircle -> getPartiesInCircle(event.locationRange)
        }
    }

    private suspend fun getPartiesInCircle(locationRange: LocationRange) {
        when (val result = getPartiesInCircleUseCase.invoke(locationRange)) {
            is NetworkResult.Success -> {
                val parties = result.data.map { Party.toParty(it) }
                _partiesResult.emit(parties)
            }
            is NetworkResult.Error -> handleError(result) {}
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
    }
}