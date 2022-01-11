package yapp.android1.data.remote

import retrofit2.http.*
import yapp.android1.data.entity.CommentCreationRequestModel
import yapp.android1.data.entity.CommentModel
import yapp.android1.data.entity.PostResponseModel


interface CommentApi : DeliBuddyApi {

    @GET("api/v1/parties/{id}/comments")
    suspend fun getCommentsInParty(
        @Path("id") id: Int
    ): List<CommentModel>

    @DELETE("api/v1/comments/{id}")
    suspend fun deleteComment(
        @Path("id") commentId: Int
    ): PostResponseModel

    @POST("api/v1/comments")
    suspend fun createComment(
        @Body commentCreationRequest: CommentCreationRequestModel
    ): CommentModel

}