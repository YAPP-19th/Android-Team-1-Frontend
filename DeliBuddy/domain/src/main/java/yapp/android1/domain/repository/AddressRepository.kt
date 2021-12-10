package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AddressEntity

interface AddressRepository {
    suspend fun searchAddressByKeyword(keyword: String): NetworkResult<List<AddressEntity>>
    suspend fun searchAddressByAddress(address: String): NetworkResult<List<AddressEntity>>
}