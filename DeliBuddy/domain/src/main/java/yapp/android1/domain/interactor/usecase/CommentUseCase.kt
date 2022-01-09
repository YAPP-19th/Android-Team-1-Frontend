package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.repository.CommentRepository
import javax.inject.Inject


typealias CommentId = Int
class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseUseCase<NetworkResult<Boolean>, CommentId>() {

    override suspend fun run(params: CommentId): NetworkResult<Boolean> {
        return commentRepository.deleteComment(params)
    }

}

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