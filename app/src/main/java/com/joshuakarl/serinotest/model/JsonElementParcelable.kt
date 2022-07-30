package com.joshuakarl.serinotest.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonElement

class JsonElementParcelable(val jsonElement: JsonElement) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jsonElement.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JsonElementParcelable> {
        override fun createFromParcel(parcel: Parcel): JsonElementParcelable {
            val jsonElement = Gson().toJsonTree(parcel.readString())
            return JsonElementParcelable(jsonElement)
        }

        override fun newArray(size: Int): Array<JsonElementParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
