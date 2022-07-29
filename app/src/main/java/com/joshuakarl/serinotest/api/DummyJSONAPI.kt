package com.joshuakarl.serinotest.api

import com.joshuakarl.serinotest.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DummyJSONAPI {
    // Returns list of n-limit products
    // https://dummyjson.com/products?skip=5&limit=1
    @GET("/products?")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): Response<Product.Response>
}