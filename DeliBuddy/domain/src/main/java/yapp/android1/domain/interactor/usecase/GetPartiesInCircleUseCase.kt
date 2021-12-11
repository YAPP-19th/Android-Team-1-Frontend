package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject

typealias LocationRange = Pair<String, Int>

class GetPartiesInCircleUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<List<PartyEntity>>, LocationRange>() {

    override suspend fun run(params: LocationRange): NetworkResult<List<PartyEntity>> {

        val data =  partyRepository.getPartiesInCircle(params.first, params.second)
        println("second ${data.toString()}")
        return data
    }

}