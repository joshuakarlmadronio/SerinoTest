package com.joshuakarl.serinotest.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.joshuakarl.serinotest.repo.ProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class PurgeWorker(context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams)
{
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface Injector {
        fun getRepo(): ProductRepository
    }
    private val injector = EntryPoints.get(context, Injector::class.java)
    private val repo = injector.getRepo()

    override suspend fun doWork(): Result {
        return try {
            repo.purge(System.currentTimeMillis() - CACHE_EXPIRY_MS)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.failure()
        }
    }

    companion object {
        // Purge cache every 15 minutes
        const val CACHE_EXPIRY_MS = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS

        private const val TAG = "PurgeWorker"
    }
}