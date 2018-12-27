package me.enko.magicmirror.android.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.enko.magicmirror.android.data.DadsJoke
import me.enko.magicmirror.android.data.DadsJokeRetrofitService
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DadsJokeViewModel(application: Application) : ScopedViewModel(application) {
    val jokeLiveData = MutableLiveData<DadsJoke>()

    init {
        launch {
            startServingDadJokes()
        }
    }

    private suspend fun startServingDadJokes() {
        while (isActive) {
            val joke = withContext(IO) {
                try {
                    DadsJokeRetrofitService.get(getApplication()).getNextJoke().await()
                } catch (e: Exception) {
                    Timber.e(e)
                    null
                }
            }

            if (joke != null) {
                jokeLiveData.postValue(joke)
            }
            delay(REFRESH_INTERVAL)
        }
    }

    companion object {
        private val REFRESH_INTERVAL = TimeUnit.MINUTES.toMillis(1) // New jokeLiveData every minute.
    }
}