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

interface DadsJokeRetrofitService {
    @GET("/")
    fun getNextJoke(): Deferred<DadsJoke>

    companion object {
        @Volatile
        private var instance: DadsJokeRetrofitService? = null

        @WorkerThread
        fun get(context: Context) = instance ?: synchronized(this) {
            instance ?: create(context).also { instance = it }
        }

        private fun create(context: Context): DadsJokeRetrofitService {
            val client = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request().newBuilder().header("Accept", "application/json").build())
                }
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://icanhazdadjoke.com/")
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            return retrofit.create<DadsJokeRetrofitService>(DadsJokeRetrofitService::class.java)
        }
    }
}