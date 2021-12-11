package yapp.android1.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import yapp.android1.data.entity.PartyModel

interface PartyApi : DeliBuddyApi {
    @GET("/v1/parties/circle")
    suspend fun getPartiesInCircle(
        @Query("point") point: String,
        @Query("distance") distance: Int,
    ): List<PartyModel>

    @GET("/v1/parties/api/v1/parties/geom")
    suspend fun getPartiesInGeom(): Unit
}
