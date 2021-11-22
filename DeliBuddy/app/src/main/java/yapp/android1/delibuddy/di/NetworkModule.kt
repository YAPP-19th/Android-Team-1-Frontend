package yapp.android1.delibuddy.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yapp.android1.data.interactor.NetworkErrorHandlerImpl
import yapp.android1.data.remote.DeliBuddyApi
import yapp.android1.data.remote.KakaoLocalApi
import yapp.android1.delibuddy.BuildConfig
import yapp.android1.domain.interactor.NetworkErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://dapi.kakao.com/"

    @DeliBuddyRetrofit
    @Provides
    fun provideDeliBuddyApiRetrofit(@DeliBuddyOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("BASE_URL")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @KakaoRetrofit
    @Provides
    fun provideKakaoApiRetrofit(@KakaoOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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
            .addNetworkInterceptor {
                val builder = it.request().newBuilder()
                it.proceed(builder.build())
            }
            .build()
    }

    @Provides
    fun provideNetworkHandler(retrofit: Retrofit): NetworkErrorHandler {
        return NetworkErrorHandlerImpl(retrofit)
    }

    @Provides
    fun provideDeliBuddyApiService(@DeliBuddyRetrofit retrofit: Retrofit): DeliBuddyApi {
        return retrofit.create(DeliBuddyApi::class.java)
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

}