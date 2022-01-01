package yapp.android1.delibuddy.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import yapp.android1.data.remote.PartyApi
import yapp.android1.data.repository.PartyRepositoryImpl
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.util.DispatcherProvider
import yapp.android1.delibuddy.util.user.AuthManagementModule
import yapp.android1.delibuddy.util.user.KakaoLoginModule
import yapp.android1.delibuddy.util.user.UserAuthManager
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
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
    fun provideKakaoLoginModule(
        @ApplicationContext context: Context,
    ): KakaoLoginModule {
        return KakaoLoginModule(context)
    }

    @Provides
    fun provideAuthManagementModule(
    ): AuthManagementModule {
        return AuthManagementModule()
    }

    @Provides
    fun provideUserLoginModule(
        @ApplicationContext context: Context,
        kakaoLoginModule: KakaoLoginModule,
        authManagementModule: AuthManagementModule
    ): UserAuthManager {
        return UserAuthManager(
            context,
            kakaoLoginModule,
            authManagementModule,
            DeliBuddyApplication.prefs
        )
    }

    @Provides
    fun providePartyRepository(
        partyApi: PartyApi,
        deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
    ): PartyRepository {
        return PartyRepositoryImpl(partyApi, deliBuddyNetworkErrorHandler)
    }
}
