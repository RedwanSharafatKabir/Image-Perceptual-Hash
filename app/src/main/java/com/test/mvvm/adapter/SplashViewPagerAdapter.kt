package com.test.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.mvvm.ui.splash.HireCar
import com.test.mvvm.ui.splash.LookForRide
import com.test.mvvm.ui.splash.LookForTruck
import com.test.mvvm.ui.splash.PayOnline

class SplashViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                LookForTruck()
            }

            1 -> {
                LookForRide()
            }

            2 -> {
                HireCar()
            }

            3 -> {
                PayOnline()
            }

            else -> {
                Fragment()
            }
        }
    }

}
