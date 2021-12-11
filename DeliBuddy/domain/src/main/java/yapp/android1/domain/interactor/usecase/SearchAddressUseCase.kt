package yapp.android1.domain.interactor.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AddressEntity
import yapp.android1.domain.repository.AddressRepository
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) : BaseUseCase<NetworkResult<List<AddressEntity>>, String>() {
    override suspend fun run(
        params: String
    ): NetworkResult<List<AddressEntity>> = withContext(Dispatchers.IO) {
        val responseAddressJob = async { addressRepository.searchAddressByAddress(params) }
        val responseKeywordJob = async { addressRepository.searchAddressByKeyword(params) }

        val responseAddress = responseAddressJob.await()
        val responseKeyword = responseKeywordJob.await()

        return@withContext when {
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