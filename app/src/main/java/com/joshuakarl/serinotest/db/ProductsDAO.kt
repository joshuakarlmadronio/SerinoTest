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

    suspend fun insertProductsWithTimestamp(products: List<Product>) {
        for (product in products) product.timestamp = System.currentTimeMillis()
        insertProducts(products)
    }

    @Query("SELECT * FROM products WHERE id > :skip AND id <= (:skip + :limit) ORDER BY id")
    suspend fun getProducts(skip: Int, limit: Int): List<Product>

    @Query("DELETE FROM products WHERE timestamp < :timestamp")
    suspend fun purge(timestamp: Long)
}