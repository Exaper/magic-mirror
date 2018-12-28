package me.enko.magicmirror.android.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.enko.magicmirror.android.data.Weather
import me.enko.magicmirror.android.data.WeatherRetrofitService
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherViewModel(application: Application) : ScopedViewModel(application) {
    val weatherLiveData = MutableLiveData<Weather>()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    init {
        launch {
            startServingWeather()
        }
    }

    private suspend fun startServingWeather() {
        while (isActive) {
            val weather = withContext(IO) {
                val location = getLocation()
                try {
                    WeatherRetrofitService.get(getApplication()).getForecast(
                        location.latitude,
                        location.longitude
                    ).await()
                } catch (e: Exception) {
                    weatherLiveData.value ?: Weather.absent
                }
            }
            weatherLiveData.postValue(weather)
            delay(REFRESH_INTERVAL)
        }
    }

    private suspend fun getLocation(): Location = suspendCoroutine { cont ->
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    cont.resume(if (location != null) location else FALLBACK_LOCATION)
                }
        } catch (e: SecurityException) {
            cont.resume(FALLBACK_LOCATION)
        }
    }

    companion object {
        private val REFRESH_INTERVAL = TimeUnit.HOURS.toMillis(2)
        private val FALLBACK_LOCATION = Location("mock").apply {
            latitude = 41.895692
            longitude = -87.639730
        }
    }
}