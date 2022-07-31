package com.joshuakarl.serinotest.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import com.joshuakarl.serinotest.BuildConfig
import com.joshuakarl.serinotest.R
import com.joshuakarl.serinotest.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop the PurgeWorker
        Log.d(TAG, "Cancelling PurgeWorker...")
        val name = getString(R.string.purge_worker_name, BuildConfig.APPLICATION_ID)
        WorkManager.getInstance(this).cancelUniqueWork(name)
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}