package com.joshuakarl.serinotest.repo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.joshuakarl.serinotest.api.DummyJSONAPI
import com.joshuakarl.serinotest.db.ProductsDAO
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.util.Connection
import com.joshuakarl.serinotest.util.NumberFormatter
import com.joshuakarl.serinotest.worker.DatabaseWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

// Acts as single source of truth
// https://proandroiddev.com/enabling-cache-offline-support-on-android-using-room-4b82ae0c9c88
class ProductRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: DummyJSONAPI,
    private val dao: ProductsDAO,
    private val connection: Connection,
    private val pref: SharedPreferences)
{
    suspend fun purge(timestamp: Long) {
        dao.purge(timestamp)
        Log.i(TAG, "Purged cache older than ${NumberFormatter.getDateString(timestamp)}")
    }

    suspend fun insertProducts(products: List<Product>) = dao.insertProductsWithTimestamp(products)

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
        val total = pref.getInt(Product.Response.LAST_TOTAL, 0)

        return if ((products.size != limit) || (total <= 0)) Resource.Error("Not in cache!")
        else Resource.Success(Product.Response(products, total, skip, limit))
    }

    private suspend fun getProductsFromNetwork(skip: Int, limit: Int): Resource<Product.Response> {
        return try {
            if (connection.hasConnection()) {
                val response = api.getProducts(skip, limit)
                val resource = parseResponse(response)
                requestSaveToCache(resource)
                resource
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

    private fun requestSaveToCache(resource: Resource<Product.Response>) {
        if (resource is Resource.Error) return

        resource.data?.let {
            val productStrings = it.products.map { product ->
                Product.Serializer().serialize(product, null, null).toString()
            }.toTypedArray()

            val request = OneTimeWorkRequestBuilder<DatabaseWorker>()
                .setInputData(workDataOf(DatabaseWorker.PRODUCTS_KEY to productStrings))
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    companion object {
        private const val TAG = "ProductRepository"
    }
}