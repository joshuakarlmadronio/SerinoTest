package com.joshuakarl.serinotest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joshuakarl.serinotest.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Product.UriTypeConverter::class)
abstract class ProductsDB: RoomDatabase() {
    abstract fun getProductsDAO(): ProductsDAO

    companion object {
        const val DATABASE_NAME = "products_db.db"
    }
}