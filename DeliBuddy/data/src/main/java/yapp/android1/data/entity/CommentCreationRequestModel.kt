package yapp.android1.data.entity

import retrofit2.http.Body


data class CommentCreationRequestModel(
    val body: String,
    val parentId: Int,
    val partyId: Int
)