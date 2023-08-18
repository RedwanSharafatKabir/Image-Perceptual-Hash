package com.perceptual.hash.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.perceptual.hash.R
import com.perceptual.hash.databinding.ActivityMainBinding
import com.perceptual.hash.utils.ImagePerceptualHash

class MainActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_image)

        ImagePerceptualHash.setData(bitmap, 8)
        val hashResult8Bit = ImagePerceptualHash.getHash()

        ImagePerceptualHash.setData(bitmap, 16)
        val hashResult16Bit = ImagePerceptualHash.getHash()

        Log.i("hashResult8Bit_obj", hashResult8Bit)
        Log.i("hashResult16Bit_obj", hashResult16Bit)
    }
}
