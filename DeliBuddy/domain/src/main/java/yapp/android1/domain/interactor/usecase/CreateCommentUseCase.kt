package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.repository.CommentRepository
import javax.inject.Inject


class CreateCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseUseCase<NetworkResult<CommentEntity>, CreateCommentUseCase.Params>() {

    override suspend fun run(params: Params): NetworkResult<CommentEntity> {
        return commentRepository.createComment(params.requestEntity)
    }

    class Params(
        val requestEntity: CommentCreationRequestEntity
    )

}