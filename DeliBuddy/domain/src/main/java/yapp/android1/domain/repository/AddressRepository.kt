package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address

interface AddressRepository {
    suspend fun searchAddressByKeyword(keyword: String): NetworkResult<List<Address>>
    suspend fun searchAddressByAddress(address: String): NetworkResult<List<Address>>
}