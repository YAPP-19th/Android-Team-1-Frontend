package yapp.android1.data.remote

import retrofit2.http.*
import yapp.android1.data.entity.*

interface PartyApi : DeliBuddyApi {
    @GET("api/v1/parties/circle")
    suspend fun getPartiesInCircle(
        @Query("point") point: String,
        @Query("distance") distance: Int,
    ): List<PartyModel>

    @GET("api/v1/parties/api/v1/parties/geom")
    suspend fun getPartiesInGeom(): Unit

    @POST("api/v1/parties")
    suspend fun createParty(
        @Body partyCreationRequest: PartyCreationRequestModel,
        @Header("Content-Type") content_type: String = "application/json"
    ): PartyInformationModel

    // "okay": true 만 오는 경우, 우선은 Unit 으로 대체하고 추후에 한번에 고치기 !
    @PUT("api/v1/parties/{id}")
    suspend fun editParty(
        @Path("id") id: Int,
        @Body partyEditRequestModel: PartyEditRequestModel
    ): PostResponseModel

    @DELETE("api/v1/parties/{id}")
    suspend fun deleteParty(
        @Path("id") id: Int,
    ): PostResponseModel

    @POST("api/v1/parties/{id}/ban")
    suspend fun banFromParty(
        @Path("id") id: Int,
        @Body partyBanRequestModel: PartyBanRequestModel,
    ): PostResponseModel

    @POST("api/v1/parties/{id}/join")
    suspend fun joinParty(
        @Path("id") id: Int,
    ): PostResponseModel

    @POST("api/v1/parties/{id}/leave")
    suspend fun leaveParty(
        @Path("id") id: Int,
    ): PostResponseModel

    @PUT("api/v1/parties/{id}/status")
    suspend fun changeStatus(
        @Path("id") id: Int,
        @Body statusChangeRequestModel: StatusChangeRequestModel
    ): PostResponseModel

    @GET("api/v1/parties/{id}")
    suspend fun getPartyInformation(
        @Path("id") id: Int
    ): PartyInformationModel

    @GET("api/v1/parties/me")
    suspend fun getMyParties(): List<PartyInformationModel>
}