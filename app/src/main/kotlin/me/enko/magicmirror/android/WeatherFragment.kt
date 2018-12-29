package me.enko.magicmirror.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_weather.*
import me.enko.magicmirror.android.data.Weather
import me.enko.magicmirror.android.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {
    private val weatherViewModel
        get() = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.weatherLiveData.observe(this, Observer { weather ->
            if (weather != null) {
                onWeatherAvailable(weather)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Just making sure we have it at all times.
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                weatherViewModel.startWeatherUpdates()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun onWeatherAvailable(weather: Weather) {
        cityTextView.text = weather.cityName
        detailsTextView.text = weather.meta[0].description.capitalizeWords()
        temperatureTextView.text = getString(R.string.degrees_in_fahrenheit, weather.detailed.temperature.toInt())

        val imageId = weather.meta[0].icon
        Picasso.get()
            .load("https://openweathermap.org/img/w/$imageId.png")
            .error(R.drawable.weather_default)
            .placeholder(R.drawable.weather_default)
            .into(iconImageView)
    }

    companion object {
        private val PERMISSION_REQUEST_CODE = 1
    }
}