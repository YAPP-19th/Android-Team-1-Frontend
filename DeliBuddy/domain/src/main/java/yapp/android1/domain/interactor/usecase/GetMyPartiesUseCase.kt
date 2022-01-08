package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyInformationEntity
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject

class GetMyPartiesUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<List<PartyInformationEntity>>, Unit>() {

    override suspend fun run(params: Unit): NetworkResult<List<PartyInformationEntity>> {
        return partyRepository.getMyParties()
    }
}