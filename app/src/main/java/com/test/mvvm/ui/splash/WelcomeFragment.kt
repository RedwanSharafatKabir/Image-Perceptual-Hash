package com.test.mvvm.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mvvm.R
import com.test.mvvm.databinding.FragmentWelcomeBinding
import java.util.*

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    var zoom: Animation? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)

        zoom = AnimationUtils.loadAnimation(requireActivity(), R.anim.zoom)

        Timer().schedule(object : TimerTask() {

            override fun run() {
                activity?.runOnUiThread {
                    findNavController().navigate(R.id.slideFragment)
                }
            }

        }, 2500)

        return binding.root
    }
}
