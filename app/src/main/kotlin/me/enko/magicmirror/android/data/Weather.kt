package me.enko.magicmirror.android.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "name") val cityName: String,
    @Json(name = "main") val detailed: Details,
    @Json(name = "weather") val meta: List<Metadata>

) {
    companion object {
        val absent = Weather("Offline city", Details.absent, Metadata.absent)
    }

    @JsonClass(generateAdapter = true)
    data class Details(
        @Json(name = "temp") val temperature: Float,
        @Json(name = "humidity") val humidity: Int
    ) {
        companion object {
            val absent = Details(Float.MIN_VALUE, Int.MIN_VALUE)
        }
    }

    @JsonClass(generateAdapter = true)
    data class Metadata(
        @Json(name = "main") val main: String,
        @Json(name = "description") val description: String,
        @Json(name = "icon") val icon: String
    ) {
        companion object {
            val absent = arrayListOf(Metadata("No clue", "No idea", ""))
        }
    }
}