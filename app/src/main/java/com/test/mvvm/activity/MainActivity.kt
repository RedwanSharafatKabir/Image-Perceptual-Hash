package com.test.mvvm.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.mvvm.databinding.ActivityMainBinding
import com.test.mvvm.utils.SharedPrefs
import java.util.*

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPrefs
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(applicationContext)

        language = sharedPrefs.read("SettingsKey", "").toString()
        if(language!=""){
            setLocale(language)
        }

        binding.navHostFragment
    }

    override fun onStop() {
        sharedPrefs.write("fragmentName", "").toString()
        super.onStop()
    }

    override fun onDestroy() {
        sharedPrefs.write("fragmentName", "").toString()
        super.onDestroy()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)

        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

        sharedPrefs.write("SettingsKey", lang).toString()
    }
}
