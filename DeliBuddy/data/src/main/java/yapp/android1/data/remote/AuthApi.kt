package yapp.android1.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import yapp.android1.data.entity.AuthModel

interface AuthApi: DeliBuddyApi {
    @POST("api/v1/auth")
    suspend fun fetchAuthToken(
        @Body token: String,
    ): AuthModel

    @POST("api/v1/auth/refresh")
    suspend fun refreshAuthToken(): AuthModel
}