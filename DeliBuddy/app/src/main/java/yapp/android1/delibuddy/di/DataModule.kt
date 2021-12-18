package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yapp.android1.data.remote.AuthApi
import yapp.android1.data.remote.CommentApi
import yapp.android1.data.repository.AuthRepositoryImpl
import yapp.android1.data.repository.CommentRepositoryImpl
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.AuthRepository
import yapp.android1.domain.repository.CommentRepository

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

    @Provides
    fun provideCommentRepository(
        commentApi: CommentApi,
        deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler
    ): CommentRepository {
        return CommentRepositoryImpl(commentApi, deliBuddyNetworkErrorHandler)
    }

}