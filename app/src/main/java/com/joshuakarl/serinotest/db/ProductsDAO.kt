package com.joshuakarl.serinotest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joshuakarl.serinotest.model.Product

@Dao
interface ProductsDAO {
    // Use as a cache DB / regularly purge
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(products: List<Product>)

    @Query("SELECT * FROM products ORDER BY id LIMIT :limit OFFSET :skip")
    suspend fun getProducts(skip: Int, limit: Int): List<Product>

    @Query("DELETE FROM products WHERE timestamp < :timestamp")
    fun purge(timestamp: Long)
}