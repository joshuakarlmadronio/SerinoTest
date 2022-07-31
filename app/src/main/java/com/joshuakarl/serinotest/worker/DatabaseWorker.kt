package com.joshuakarl.serinotest.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.repo.ProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseWorker(context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams)
{
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface Injector {
        fun getRepo(): ProductRepository
        fun getGson(): Gson
    }

    private val injector = EntryPoints.get(context, Injector::class.java)
    private val repo = injector.getRepo()
    private val gson = injector.getGson()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val productStrings = inputData.getStringArray(PRODUCTS_KEY)
            val type = object : TypeToken<Product>() { }.type
            val products = productStrings?.let {
                productStrings.map { string ->
                    gson.fromJson<Product>(string, type)
                }
            }
            if (products != null) {
                repo.insertProducts(products)
                Log.d(TAG, "Cached ${products.size} products. ${products.map { it.id }}")
                Result.success()
            } else {
                Log.e(TAG, "No products found to insert")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "DatabaseWorker"

        const val PRODUCTS_KEY = "PRODUCTS_KEY"
    }
}