package com.joshuakarl.serinotest.di

import android.content.Context
import androidx.room.Room
import com.joshuakarl.serinotest.db.ProductsDAO
import com.joshuakarl.serinotest.db.ProductsDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesProductsDB(@ApplicationContext context: Context): ProductsDB = Room.databaseBuilder(
        context,
        ProductsDB::class.java,
        ProductsDB.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesProductsDAO(productsDB: ProductsDB): ProductsDAO = productsDB.getProductsDAO()
}