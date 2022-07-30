package com.joshuakarl.serinotest.repo

import android.util.Log
import com.joshuakarl.serinotest.api.DummyJSONAPI
import com.joshuakarl.serinotest.db.ProductsDAO
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.util.Connection
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

// Acts as single source of truth
// https://proandroiddev.com/enabling-cache-offline-support-on-android-using-room-4b82ae0c9c88
class ProductRepository @Inject constructor(
    private val api: DummyJSONAPI,
    private val dao: ProductsDAO,
    private val connection: Connection
) {
    suspend fun getProducts(skip: Int, limit: Int = 10): Resource<Product.Response> {
        return when (val daoResponse = getProductsFromDAO(skip, limit)) {
            // Try cache
            is Resource.Success -> {
                Log.i(TAG, "Using data from cache")
                daoResponse
            }
            // No? Try network
            else -> {
                Log.i(TAG, "Using data from network")
                getProductsFromNetwork(skip, limit)
            }
        }
    }

    private suspend fun getProductsFromDAO(skip: Int, limit: Int): Resource<Product.Response> {
        val products = dao.getProducts(skip, limit)
        return if (products.size != limit) Resource.Error("Not in cache!")
        else Resource.Success(Product.Response(products, 0, skip, limit))
    }

    private suspend fun getProductsFromNetwork(skip: Int, limit: Int): Resource<Product.Response> {
        return try {
            if (connection.hasConnection()) {
                val response = api.getProducts(skip, limit)
                parseResponse(response)
            } else {
                Resource.Error("No connection!")
            }
        } catch (t: Throwable) {
            if (t is IOException) Resource.Error("Network failure!")
            else Resource.Error(t.message ?: "")
        }
    }

    private fun parseResponse(response: Response<Product.Response>): Resource<Product.Response> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }

    companion object {
        private const val TAG = "ProductRepository"
    }
}