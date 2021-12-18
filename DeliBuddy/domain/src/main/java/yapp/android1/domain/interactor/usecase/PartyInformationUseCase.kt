package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.repository.CommentRepository
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject


typealias PartyId = Int
class FetchPartyInformationUseCase @Inject constructor (
    private val partyRepository: PartyRepository
) : BaseUseCase<NetworkResult<PartyEntity>, PartyId>() {

    override suspend fun run(params: PartyId): NetworkResult<PartyEntity> {
        return partyRepository.getParty(id = params)
    }

}

class FetchPartyCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseUseCase<NetworkResult<List<CommentEntity>>, PartyId>() {

    override suspend fun run(params: PartyId): NetworkResult<List<CommentEntity>> {
        return commentRepository.getCommentsInParty(partyId = params)
    }

}