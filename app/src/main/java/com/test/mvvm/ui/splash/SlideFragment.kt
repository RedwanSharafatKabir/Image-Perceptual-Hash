package com.test.mvvm.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.mvvm.R
import com.test.mvvm.adapter.SplashViewPagerAdapter
import com.test.mvvm.databinding.FragmentSlideBinding

class SlideFragment : Fragment() {

    private lateinit var binding: FragmentSlideBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSlideBinding.inflate(layoutInflater)

        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        val adapter = SplashViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.splashViewPager2.adapter = adapter

        binding.next.setOnClickListener {
            viewPagerController()
        }

        return binding.root
    }

    private fun viewPagerController(){
        val fragmentIndex: Int = binding.splashViewPager2.currentItem

        if(fragmentIndex == 0){
            binding.splashViewPager2.currentItem = 1
            binding.circleView2.setBackgroundResource(R.drawable.purple_curved_shape)

        } else if(fragmentIndex == 1){
            binding.splashViewPager2.currentItem = 2
            binding.circleView3.setBackgroundResource(R.drawable.purple_curved_shape)

        } else if(fragmentIndex == 2){
            binding.splashViewPager2.currentItem = 3
            binding.circleView4.setBackgroundResource(R.drawable.purple_curved_shape)

        } else if(fragmentIndex == 3){
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
