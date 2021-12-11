package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yapp.android1.data.remote.AuthApi
import yapp.android1.data.repository.AuthRepositoryImpl
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
        deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, deliBuddyNetworkErrorHandler)
    }
}