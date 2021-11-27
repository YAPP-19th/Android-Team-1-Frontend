package yapp.android1.data.repository

import yapp.android1.data.entity.MapAddress
import yapp.android1.data.remote.KakaoLocalApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler
import yapp.android1.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val api: KakaoLocalApi,
    private val kakaoNetworkErrorHandler: KakaoNetworkErrorHandler
) : AddressRepository {
    override suspend fun searchAddressByKeyword(
        keyword: String
    ): NetworkResult<List<Address>> {
        return try {
            val response = api.searchByKeyword(keyword)

            NetworkResult.Success(
                response.documents.map {
                    MapAddress.toAddress(it)
                }
            )
        } catch (e: Exception) {
            val errorType = kakaoNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun searchAddressByAddress(
        address: String
    ): NetworkResult<List<Address>> {
        return try {
            val response = api.searchByAddress(address, "similar")

            NetworkResult.Success(
                response.documents.map {
                    MapAddress.toAddress(it)
                }
            )
        } catch (e: Exception) {
            val errorType = kakaoNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }
}