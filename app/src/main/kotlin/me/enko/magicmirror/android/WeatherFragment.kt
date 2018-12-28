package me.enko.magicmirror.android

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
        if (!context!!.hasLocationPermission()) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
    }


    private fun onWeatherAvailable(weather: Weather) {
        detailsTextView.text = "${weather.cityName}\n${weather.meta[0].description.capitalizeWords()}"
        temperatureTextView.text = getString(R.string.degrees_in_fahrenheit, weather.detailed.temperature.toInt())

        val imageId = weather.meta[0].icon

        Picasso.get()
            .load("https://openweathermap.org/img/w/$imageId.png")
            .into(iconImageView)
    }
}