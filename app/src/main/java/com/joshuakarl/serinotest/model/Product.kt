package com.joshuakarl.serinotest.model

import android.net.Uri
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String, // TODO Make Category class?
    val thumbnail: Uri,
    val images: List<Uri>
) {
    data class Response (
        @SerializedName("products")
        val products: List<Product>,
        @SerializedName("total")
        val total: Int,
        @SerializedName("skip")
        val skip: Int,
        @SerializedName("limit")
        val limit: Int,
    )

    class Deserializer: JsonDeserializer<Product> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Product {
            json as JsonObject

            val imagesJson = json.get("images")
            val type = object : TypeToken<List<String>>() { }.type
            val imagesString = Gson().fromJson<List<String>>(imagesJson, type)
            val imagesUri = imagesString.map { Uri.parse(it) }

            return Product(
                id = json.get("id").asInt,
                title = json.get("title").asString,
                description = json.get("description").asString,
                price = json.get("price").asFloat,
                discountPercentage = json.get("discountPercentage").asFloat,
                rating = json.get("rating").asFloat,
                stock = json.get("stock").asInt,
                brand = json.get("brand").asString,
                category = json.get("category").asString,
                thumbnail = Uri.parse(json.get("thumbnail").asString),
                images = imagesUri
            )
        }
    }
}
