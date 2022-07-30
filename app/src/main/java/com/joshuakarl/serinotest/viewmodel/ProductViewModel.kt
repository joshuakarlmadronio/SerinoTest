package com.joshuakarl.serinotest.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.repo.ProductRepository
import com.joshuakarl.serinotest.util.Connection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val connection: Connection,
    private val repo: ProductRepository,
): ViewModel() {

    // Observable
    val products: MutableLiveData<Resource<Product.Response>> = MutableLiveData()

    init {
        // Load the last viewed page
        val skip = pref.getInt(LAST_SKIP, 0)
        getProducts(skip)
    }

    fun getProducts(skip: Int, limit: Int = 10) = viewModelScope.launch {
        safeGetProducts(skip, limit)

        // Save the viewed page
        with (pref.edit()) {
            putInt(LAST_SKIP, skip)
            apply()
        }
    }

    private suspend fun safeGetProducts(skip: Int, limit: Int) {
        try {
            if (connection.hasConnection()) {
                products.postValue(Resource.Loading())
                val response = repo.getProducts(skip, limit)
                products.postValue(parseResponse(response))
            } else {
                products.postValue(Resource.Error("No connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> products.postValue(Resource.Error("Network failure!"))
                else -> products.postValue(Resource.Error("Parse error. Cannot parse response!"))
            }
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
        private const val LAST_SKIP = "LAST_SKIP"
    }
}