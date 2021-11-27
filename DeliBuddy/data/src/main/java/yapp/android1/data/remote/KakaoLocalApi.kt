package yapp.android1.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import yapp.android1.data.entity.AddressModel
import yapp.android1.data.entity.CoordAddressModel
import yapp.android1.data.entity.KeywordModel

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun searchByKeyword(
        @Query("query") query: String
    ): KeywordModel

    @GET("v2/local/search/address.json")
    suspend fun searchByAddress(
        @Query("query") query: String,
        @Query("analyze_type") analyze_type: String
    ): AddressModel

    @GET("/v2/local/geo/coord2address.json")
    suspend fun coordToAddress(
        @Query("y") lat: Double,
        @Query("x") lng: Double
    ) : CoordAddressModel
}