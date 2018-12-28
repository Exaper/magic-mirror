package me.enko.magicmirror.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dads_joke.*
import me.enko.magicmirror.android.data.DadsJoke
import me.enko.magicmirror.android.viewmodel.DadsJokeViewModel

class DadsJokeFragment : Fragment() {
    private val dadsJokeViewModel
        get() = ViewModelProviders.of(this).get(DadsJokeViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dads_joke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dadsJokeViewModel.jokeLiveData.observe(this, Observer { joke ->
            if (joke != null) {
                onDadsJokeAvailable(joke)
            }
        })
    }

    private fun onDadsJokeAvailable(dadsJoke: DadsJoke) {
        dadsJokeText.text = dadsJoke.joke
    }
}