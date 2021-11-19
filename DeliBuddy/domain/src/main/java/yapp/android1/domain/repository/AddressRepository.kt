package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address

interface AddressRepository {
    suspend fun fetchAddressByKeyword(keyword: String): NetworkResult<List<Address>>
    suspend fun fetchAddressByAddress(address: String): NetworkResult<List<Address>>
}