package com.ankursamarya.flickr.network

import com.ankursamarya.flickr.BuildConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkingProvider {

    private val okHttpClient: OkHttpClient by lazy { provideOkHttpClient(provideLoggingInterceptor()) }
    private val gsonConverterFactory: GsonConverterFactory by lazy {
        provideGsonConverterFactory(
            provideGson()
        )
    }
    private val baseRetrofit: Retrofit by lazy {
        provideRetrofit(
            provideBaseUrl(),
            okHttpClient,
            gsonConverterFactory
        )
    }

    fun <T> apiService(service: Class<T>): T = baseRetrofit.create(service)

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private fun provideGson(): Gson = Gson()

    private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    private fun provideBaseUrl(): String {
        return "https://api.flickr.com/services/rest/"
    }

    private fun provideRetrofit(
        baseUrl: String,
        callFactory: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(callFactory)
            .addConverterFactory(converterFactory)
            .build()
    }


}