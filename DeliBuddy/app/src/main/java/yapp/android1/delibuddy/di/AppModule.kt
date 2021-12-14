package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import yapp.android1.data.remote.KakaoLocalApi
import yapp.android1.data.remote.PartyApi
import yapp.android1.data.repository.AddressRepositoryImpl
import yapp.android1.data.repository.CoordToAddressRepositoryImpl
import yapp.android1.data.repository.PartyRepositoryImpl
import yapp.android1.delibuddy.util.DispatcherProvider
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler
import yapp.android1.domain.interactor.usecase.CoordToAddressUseCase
import yapp.android1.domain.interactor.usecase.SearchAddressUseCase
import yapp.android1.domain.repository.AddressRepository
import yapp.android1.domain.repository.CoordToAddressRepository
import yapp.android1.domain.repository.PartyRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideCoroutineDispatcher(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }

    @Provides
    fun providePartyRepository(
        partyApi: PartyApi,
        deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
    ): PartyRepository {
        return PartyRepositoryImpl(partyApi, deliBuddyNetworkErrorHandler)
    }

    @Provides
    fun provideAddressRepository(
        kakaoLocalApi: KakaoLocalApi,
        kakaoNetworkErrorHandler: KakaoNetworkErrorHandler,
    ): AddressRepository {
        return AddressRepositoryImpl(kakaoLocalApi, kakaoNetworkErrorHandler)
    }

    @Provides
    fun provideCoordToAddressRepository(
        kakaoLocalApi: KakaoLocalApi,
        kakaoNetworkErrorHandler: KakaoNetworkErrorHandler,
    ): CoordToAddressRepository {
        return CoordToAddressRepositoryImpl(kakaoLocalApi, kakaoNetworkErrorHandler)
    }

    @Provides
    fun provideSearchAddressUseCase(addressRepository: AddressRepository): SearchAddressUseCase {
        return SearchAddressUseCase(addressRepository)
    }

    @Provides
    fun provideCoordToAddressUseCase(coordToAddressRepository: CoordToAddressRepository): CoordToAddressUseCase {
        return CoordToAddressUseCase(coordToAddressRepository)
    }
}
