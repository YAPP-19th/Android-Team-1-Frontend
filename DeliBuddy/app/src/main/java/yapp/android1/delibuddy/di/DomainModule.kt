package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yapp.android1.domain.interactor.usecase.*
import yapp.android1.domain.repository.*

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideFetchAuthUseCase(authRepository: AuthRepository): FetchAuthUseCase {
        return FetchAuthUseCase(authRepository)
    }

    @Provides
    fun provideGetPartiesInCircleUseCase(partyRepository: PartyRepository): GetPartiesInCircleUseCase {
        return GetPartiesInCircleUseCase(partyRepository)
    }

    @Provides
    fun provideFetchPartyCommentsUseCase(commentRepository: CommentRepository): FetchPartyCommentsUseCase {
        return FetchPartyCommentsUseCase(commentRepository)
    }

    @Provides
    fun provideFetchPartyInformationUseCase(partyRepository: PartyRepository): FetchPartyInformationUseCase {
        return FetchPartyInformationUseCase(partyRepository)
    }

    @Provides
    fun provideSearchAddressUseCase(addressRepository: AddressRepository): SearchAddressUseCase {
        return SearchAddressUseCase(addressRepository)
    }

    @Provides
    fun provideCoordToAddressUseCase(coordToAddressRepository: CoordToAddressRepository): CoordToAddressUseCase {
        return CoordToAddressUseCase(coordToAddressRepository)
    }

    @Provides
    fun provideCategoryListUseCase(categoryListRepository: CategoryListRepository): CategoryListUseCase {
        return CategoryListUseCase(categoryListRepository)
    }
}