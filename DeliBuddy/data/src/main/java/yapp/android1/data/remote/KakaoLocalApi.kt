package yapp.android1.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import yapp.android1.data.entity.ResponseAddress
import yapp.android1.data.entity.ResponseKeyword

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun searchByKeyword(
        @Query("query") query: String
    ): ResponseKeyword

    @GET("v2/local/search/address.json")
    suspend fun searchByAddress(
        @Query("query") query: String,
        @Query("analyze_type") analyze_type: String
    ): ResponseAddress
}