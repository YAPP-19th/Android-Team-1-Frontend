package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address

interface CoordToAddressRepository {
    suspend fun coordToAddress(lat: Double, lng: Double): NetworkResult<Address>
}