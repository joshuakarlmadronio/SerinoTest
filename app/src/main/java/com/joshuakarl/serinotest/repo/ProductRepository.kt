package com.joshuakarl.serinotest.repo

import com.joshuakarl.serinotest.api.DummyJSONAPI
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: DummyJSONAPI) {
    suspend fun getProducts(skip: Int, limit: Int = 10) = api.getProducts(skip, limit)
}