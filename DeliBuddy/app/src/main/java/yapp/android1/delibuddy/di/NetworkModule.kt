package yapp.android1.delibuddy.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yapp.android1.data.interactor.DeliBuddyNetworkErrorHandlerImpl
import yapp.android1.data.interactor.KakaoNetworkErrorHandlerImpl
import yapp.android1.data.remote.*
import yapp.android1.delibuddy.BuildConfig
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val API_BASE_URL =
        "http://ec2-54-180-186-128.ap-northeast-2.compute.amazonaws.com/"
    private const val KAKAO_BASE_URL = "https://dapi.kakao.com/"

    @DeliBuddyRetrofit
    @Provides
    fun provideDeliBuddyApiRetrofit(
        @DeliBuddyOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @KakaoRetrofit
    @Provides
    fun provideKakaoApiRetrofit(
        @KakaoOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @KakaoOkHttpClient
    @Provides
    fun provideKakaoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor(true))
            .addNetworkInterceptor {
                val builder = it.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.KAKAO_LOCAL_API_KEY)
                it.proceed(builder.build())
            }
            .build()
    }

    @DeliBuddyOkHttpClient
    @Provides
    fun provideDeliBuddyOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor(true))
            .addInterceptor(headerInterceptor)
            .addNetworkInterceptor {
                val builder = it.request().newBuilder()
                it.proceed(builder.build())
            }
            .build()
    }

    @Provides
    fun provideDeliBuddyNetworkHandler(
        @DeliBuddyRetrofit retrofit: Retrofit,
    ): DeliBuddyNetworkErrorHandler {
        return DeliBuddyNetworkErrorHandlerImpl(retrofit)
    }

    @Provides
    fun provideKaKaoNetworkErrorHandler(
        @KakaoRetrofit retrofit: Retrofit,
    ): KakaoNetworkErrorHandler {
        return KakaoNetworkErrorHandlerImpl(retrofit)
    }

    @Provides
    fun provideDeliBuddyApiService(@DeliBuddyRetrofit retrofit: Retrofit): DeliBuddyApi {
        return retrofit.create(DeliBuddyApi::class.java)
    }

    @Provides
    fun providePartyApiService(@DeliBuddyRetrofit retrofit: Retrofit): PartyApi {
        return retrofit.create(PartyApi::class.java)
    }

    @Provides
    fun provideAuthApiService(@DeliBuddyRetrofit retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideCommentApiService(@DeliBuddyRetrofit retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    fun provideCategoryListApiService(@DeliBuddyRetrofit retrofit: Retrofit): CategoryListApi {
        return retrofit.create(CategoryListApi::class.java)
    }

    @Provides
    fun provideKakaoApiService(@KakaoRetrofit retrofit: Retrofit): KakaoLocalApi {
        return retrofit.create(KakaoLocalApi::class.java)
    }

    private fun makeLoggingInterceptor(debug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (debug)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }

    private val headerInterceptor = Interceptor {
        var token = DeliBuddyApplication.prefs.getUserToken()
        val request = it.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return@Interceptor it.proceed(request)
    }
}
