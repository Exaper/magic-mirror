package me.enko.magicmirror.android.data

import android.content.Context
import androidx.annotation.WorkerThread
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import me.enko.magicmirror.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRetrofitService {
    @GET("/data/2.5/weather?units=imperial")
    fun getForecast(@Query("q") city: String): Deferred<Weather>

    @GET("/data/2.5/weather?units=imperial")
    fun getForecast(@Query("lat") lat: Double, @Query("lon") lon: Double): Deferred<Weather>

    companion object {
        @Volatile
        private var instance: WeatherRetrofitService? = null

        @WorkerThread
        fun get(context: Context) = instance ?: synchronized(this) {
            instance ?: create(context).also { instance = it }
        }

        private fun create(context: Context): WeatherRetrofitService {
            val client = OkHttpClient.Builder()
                .addInterceptor {
                    val request = it.request()
                    val urlWithAppId = request.url()
                        .newBuilder()
                        .setQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_APP_ID)
                        .build()

                    it.proceed(request.newBuilder().url(urlWithAppId).build())
                }
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit.create<WeatherRetrofitService>(WeatherRetrofitService::class.java)
        }
    }
}