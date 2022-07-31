package com.joshuakarl.serinotest.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = "products", primaryKeys = ["id"])
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
    val images: List<Uri>,
    var timestamp: Long? = null
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
    ) {
        companion object {
            const val LAST_SKIP = "LAST_SKIP"
            const val LAST_TOTAL = "LAST_TOTAL"
        }
    }

    class Serializer: JsonSerializer<Product> {
        override fun serialize(
            src: Product?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            val json = JsonObject()
            src?.let {
                json.apply {
                    add("id", JsonPrimitive(it.id))
                    add("title", JsonPrimitive(it.title))
                    add("description", JsonPrimitive(it.description))
                    add("price", JsonPrimitive(it.price))
                    add("discountPercentage", JsonPrimitive(it.discountPercentage))
                    add("rating", JsonPrimitive(it.rating))
                    add("stock", JsonPrimitive(it.stock))
                    add("brand", JsonPrimitive(it.brand))
                    add("category", JsonPrimitive(it.category))
                    add("thumbnail", JsonPrimitive(it.thumbnail.toString()))
                    val imagesString = it.images.map { image -> image.toString() }
                    val imagesJson = Gson().toJsonTree(imagesString)
                    add("images", imagesJson)
                }
            }
            return json
        }
    }

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

    // TODO put this in own entity but my eyes are tired
    class UriTypeConverter {
        @TypeConverter
        fun fromUri(uri: Uri): String {
            return uri.toString()
        }
        @TypeConverter
        fun toUri(uriString: String): Uri {
            return Uri.parse(uriString)
        }
        @TypeConverter
        fun fromUris(uris: List<Uri>): String {
            val urisJson = urisToJsonElement(uris)
            return urisJson.toString()
        }
        @TypeConverter
        fun toUris(urisString: String): List<Uri> {
            val type = object : TypeToken<List<String>>() { }.type
            val jsonElement = Gson().fromJson<List<String>>(urisString, type)
            return jsonElement.map { Uri.parse(it) }
        }
    }

    companion object {
        fun urisToJsonElement(uris: List<Uri>): JsonElement {
            val urisString = uris.map { uri -> uri.toString() }
            return Gson().toJsonTree(urisString)
        }
    }
}
