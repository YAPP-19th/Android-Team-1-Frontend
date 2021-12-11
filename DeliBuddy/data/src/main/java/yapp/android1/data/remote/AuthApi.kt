package yapp.android1.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import yapp.android1.data.entity.AuthModel

interface AuthApi: DeliBuddyApi {
    @POST("authenticate")
    suspend fun fetchAuthToken(
        @Body token: String,
    ): AuthModel
}