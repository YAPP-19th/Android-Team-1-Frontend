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
    fun provideRefreshAuthUseCase(authRepository: AuthRepository): RefreshAuthUseCase {
        return RefreshAuthUseCase(authRepository)
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

    @Provides
    fun provideCreatePartyUseCase(partyRepository: PartyRepository): CreatePartyUseCase {
        return CreatePartyUseCase(partyRepository)
    }

    @Provides
    fun provideGetMyInfoUseCase(userRepository: UserRepository): GetMyInfoUseCase {
        return GetMyInfoUseCase(userRepository)
    }

    @Provides
    fun provideJoinPartyUseCase(partyRepository: PartyRepository): JoinPartyUseCase {
        return JoinPartyUseCase(partyRepository)
    }

    @Provides
    fun provideCreateCommentUseCase(commentRepository: CommentRepository): CreateCommentUseCase {
        return CreateCommentUseCase(commentRepository)
    }

    @Provides
    fun provideFcmUseCase(userRepository: UserRepository): FcmTokenUseCase {
        return FcmTokenUseCase(userRepository)
    }
}

    @Provides
    fun provideDeletePartyUseCase(partyRepository: PartyRepository): DeletePartyUseCase {
        return DeletePartyUseCase(partyRepository)
    }

    @Provides
    fun provideDeleteCommentUseCase(commentRepository: CommentRepository): DeleteCommentUseCase {
        return DeleteCommentUseCase(commentRepository)
    }

    @Provides
    fun provideEditPartyUseCase(partyRepository: PartyRepository): EditPartyUseCase {
        return EditPartyUseCase(partyRepository)
    }

    @Provides
    fun provideBanFromPartyUseCase(partyRepository: PartyRepository): BanFromPartyUseCase {
        return BanFromPartyUseCase(partyRepository)
    }

    @Provides
    fun provideGetMyPartiesUseCase(partyRepository: PartyRepository): GetMyPartiesUseCase {
        return GetMyPartiesUseCase(partyRepository)
    }
}

