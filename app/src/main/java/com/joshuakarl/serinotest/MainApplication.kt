package com.joshuakarl.serinotest

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.joshuakarl.serinotest.worker.PurgeWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import kotlin.math.max

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Create and start the PurgeWorker
        Log.d(TAG, "Starting the PurgeWorker")
        val duration = Duration.ofMillis(
            max(PurgeWorker.CACHE_EXPIRY_MS, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)
        )
        val request = PeriodicWorkRequestBuilder<PurgeWorker>(duration)
            .build()
        val name = getString(R.string.purge_worker_name, BuildConfig.APPLICATION_ID)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            name,
            ExistingPeriodicWorkPolicy.REPLACE,
            request)
    }

    companion object {
        private const val TAG = "MainApplication"
    }
}