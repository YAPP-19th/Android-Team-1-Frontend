package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyCreationRequestEntity
import yapp.android1.domain.entity.PartyInformationEntity
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject

class CreatePartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<PartyInformationEntity>, PartyCreationRequestEntity>() {
    override suspend fun run(params: PartyCreationRequestEntity): NetworkResult<PartyInformationEntity> {
        return partyRepository.createParty(params)
    }
}