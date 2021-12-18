package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yapp.android1.domain.interactor.usecase.FetchAuthUseCase
import yapp.android1.domain.interactor.usecase.FetchPartyCommentsUseCase
import yapp.android1.domain.interactor.usecase.FetchPartyInformationUseCase
import yapp.android1.domain.interactor.usecase.GetPartiesInCircleUseCase
import yapp.android1.domain.repository.AuthRepository
import yapp.android1.domain.repository.CommentRepository
import yapp.android1.domain.repository.PartyRepository

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
}