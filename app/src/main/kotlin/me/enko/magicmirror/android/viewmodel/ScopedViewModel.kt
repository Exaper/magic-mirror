package me.enko.magicmirror.android.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import timber.log.Timber

open class ScopedViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler(handler = { context, throwable ->
            if (context.isActive) { // We don't care what happens after the job is cancelled.
                Timber.e(throwable)
            }
        })

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}