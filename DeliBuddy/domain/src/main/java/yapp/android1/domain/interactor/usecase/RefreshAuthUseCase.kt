package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AuthEntity
import yapp.android1.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<NetworkResult<AuthEntity>, Unit>() {

    override suspend fun run(params: Unit): NetworkResult<AuthEntity> {
        return authRepository.refreshAuthToken()
    }

}