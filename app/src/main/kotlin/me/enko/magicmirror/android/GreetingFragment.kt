package me.enko.magicmirror.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_greeting.*
import me.enko.magicmirror.android.viewmodel.GreetingViewModel

class GreetingFragment : Fragment() {
    private val greetingViewModel
        get() = ViewModelProviders.of(this).get(GreetingViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_greeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        greetingViewModel.greetingLiveData.observe(this, Observer {
            greetingText.text = it
        })
    }
}