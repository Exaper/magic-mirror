package me.enko.magicmirror.android.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DadsJoke(@Json(name = "joke") val joke: String)