package com.joshuakarl.serinotest.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.joshuakarl.serinotest.BuildConfig
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.preference_file_key, BuildConfig.APPLICATION_ID), Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesGSON(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Product::class.java, Product.Deserializer())
            .registerTypeAdapter(Product::class.java, Product.Serializer())
            .create()
    }

    @Provides
    @Singleton
    fun providesGSONConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }
}