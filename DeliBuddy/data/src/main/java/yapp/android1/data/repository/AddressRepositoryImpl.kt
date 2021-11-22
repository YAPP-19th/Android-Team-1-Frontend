package yapp.android1.data.repository

import yapp.android1.data.entity.AddressResponseMapper
import yapp.android1.data.remote.KakaoLocalApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val api: KakaoLocalApi
) : AddressRepository {
    override suspend fun searchAddressByKeyword(
        keyword: String
    ): NetworkResult<List<Address>> {
        return try {
            val response = api.searchByKeyword(keyword)

            NetworkResult.Success(
                response.documents.map {
                    AddressResponseMapper.toAddress(it)
                }
            )
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.Unknown)
        }
    }

    override suspend fun searchAddressByAddress(
        address: String
    ): NetworkResult<List<Address>> {
        return try {
            val response = api.searchByAddress(address, "similar")

            NetworkResult.Success(
                response.documents.map {
                    AddressResponseMapper.toAddress(it)
                }
            )
        } catch (e: Exception) {
            NetworkResult.Error(NetworkError.Unknown)
        }
    }
}