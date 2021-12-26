package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.entity.PartyInformationEntity
import yapp.android1.domain.entity.StatusChangeRequestEntity
import yapp.android1.domain.repository.CommentRepository
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject


typealias PartyId = Int
class FetchPartyInformationUseCase @Inject constructor (
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<PartyInformationEntity>, PartyId>() {

    override suspend fun run(params: PartyId): NetworkResult<PartyInformationEntity> {
        return partyRepository.getPartyInformation(id = params)
    }

}

class FetchPartyCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseUseCase<NetworkResult<List<CommentEntity>>, PartyId>() {

    override suspend fun run(params: PartyId): NetworkResult<List<CommentEntity>> {
        return commentRepository.getCommentsInParty(partyId = params)
    }

}

class JoinPartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<Boolean>, PartyId>() {

    override suspend fun run(params: PartyId): NetworkResult<Boolean> {
        return partyRepository.joinParty(params)
    }

}

class ChangeStatusUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<Boolean>, ChangeStatusUseCase.Params>() {

    override suspend fun run(params: Params): NetworkResult<Boolean> {
        return partyRepository.changeStatus(params.partyId, StatusChangeRequestEntity.fromStringToEntity(params.changedStatus))
    }

    data class Params(
        val partyId: Int,
        val changedStatus: String
    )
}