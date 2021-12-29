package yapp.android1.delibuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yapp.android1.data.remote.*
import yapp.android1.data.repository.*
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler
import yapp.android1.domain.repository.*

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
    fun provideCategoryListRepository(
        categoryListApi: CategoryListApi,
        deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler
    ): CategoryListRepository {
        return CategoryListRepositoryImpl(categoryListApi, deliBuddyNetworkErrorHandler)
    }
}