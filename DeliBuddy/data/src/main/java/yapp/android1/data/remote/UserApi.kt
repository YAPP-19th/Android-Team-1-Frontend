package yapp.android1.data.remote

import retrofit2.http.GET
import retrofit2.http.POST
import yapp.android1.data.entity.UserModel

interface UserApi : DeliBuddyApi {
    @GET("api/v1/users/me")
    suspend fun getMyInfo(): UserModel

    @POST("api/v1/users/me/fcm_token")
    suspend fun setFcmToken()
}
