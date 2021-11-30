package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.AddressRepository
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) : BaseUseCase<NetworkResult<List<Address>>, String>() {
    override suspend fun run(params: String): NetworkResult<List<Address>> {
        val responseAddress = addressRepository.searchAddressByAddress(params)
        val responseKeyword = addressRepository.searchAddressByKeyword(params)

        return when {
            responseAddress is NetworkResult.Success &&
                    responseKeyword is NetworkResult.Success -> {
                if (responseAddress.data.size > responseKeyword.data.size) {
                    NetworkResult.Success(responseAddress.data + responseKeyword.data)
                } else {
                    NetworkResult.Success(responseKeyword.data + responseAddress.data)
                }
            }
            responseAddress is NetworkResult.Success -> responseAddress
            responseKeyword is NetworkResult.Success -> responseKeyword
            else -> responseAddress
        }
    }
}