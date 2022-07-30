package com.joshuakarl.serinotest.di

import android.content.Context
import android.content.SharedPreferences
import com.joshuakarl.serinotest.BuildConfig
import com.joshuakarl.serinotest.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.preference_file_key, BuildConfig.APPLICATION_ID), Context.MODE_PRIVATE)
}