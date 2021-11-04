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
import yapp.android1.domain.interactor.NetworkErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDeliBuddyApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("BASE_URL")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor(true))
            .addNetworkInterceptor {
                val builder = it.request().newBuilder()
                it.proceed(builder.build())
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkHandler(retrofit: Retrofit): NetworkErrorHandler {
        return NetworkErrorHandlerImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideDeliBuddyApiService(retrofit: Retrofit): DeliBuddyApi {
        return retrofit.create(DeliBuddyApi::class.java)
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