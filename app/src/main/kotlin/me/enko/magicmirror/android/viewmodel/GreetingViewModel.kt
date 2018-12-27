package me.enko.magicmirror.android.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.enko.magicmirror.android.R
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GreetingViewModel(application: Application) : ScopedViewModel(application) {
    val greetingLiveData = MutableLiveData<String>()

    init {
        launch {
            startServingGreetings()
        }
    }

    private suspend fun startServingGreetings() {
        val greetings = getApplication<Application>().resources.getStringArray(R.array.greetings)
        while (isActive) {
            greetingLiveData.value = greetings[Random.nextInt(greetings.size)]
            delay(REFRESH_INTERVAL)
        }
    }

    companion object {
        private val REFRESH_INTERVAL = TimeUnit.HOURS.toMillis(12)
    }
}