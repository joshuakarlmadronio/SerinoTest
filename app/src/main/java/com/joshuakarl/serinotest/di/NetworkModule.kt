package com.joshuakarl.serinotest.di

import com.google.gson.GsonBuilder
import com.joshuakarl.serinotest.api.DummyJSONAPI
import com.joshuakarl.serinotest.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val API_BASE_URL = "https://dummyjson.com"

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun providesGSONConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapter(Product::class.java, Product.Deserializer())
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun providesAPI(retrofit: Retrofit) : DummyJSONAPI = retrofit.create(DummyJSONAPI::class.java)
}