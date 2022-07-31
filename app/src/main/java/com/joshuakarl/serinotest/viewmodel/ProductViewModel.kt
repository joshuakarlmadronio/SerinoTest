package com.joshuakarl.serinotest.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshuakarl.serinotest.model.Product
import com.joshuakarl.serinotest.model.Resource
import com.joshuakarl.serinotest.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repo: ProductRepository,
): ViewModel() {

    // Observable
    val products: MutableLiveData<Resource<Product.Response>> = MutableLiveData()

    init {
        // Load the last viewed page
        val skip = pref.getInt(Product.Response.LAST_SKIP, 0)
        getProducts(skip)
    }

    fun getProducts(skip: Int, limit: Int = 10) = viewModelScope.launch {
        safeGetProducts(skip, limit)

        // Save the viewed page
        with (pref.edit()) {
            putInt(Product.Response.LAST_SKIP, skip)
            apply()
        }
    }

    private suspend fun safeGetProducts(skip: Int, limit: Int) {
        products.postValue(Resource.Loading())
        products.postValue(repo.getProducts(skip, limit))
    }
}