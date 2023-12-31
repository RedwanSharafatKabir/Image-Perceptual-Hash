package com.perceptual.hash.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.image.perceptual_hash.utils.ImagePerceptualHash
import com.perceptual.hash.R
import com.perceptual.hash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_image)

        val imagePerceptualHash = ImagePerceptualHash(bitmap)

        imagePerceptualHash.setData(8)
        val hashResult8Bit = imagePerceptualHash.getHash()

        imagePerceptualHash.setData(16)
        val hashResult16Bit = imagePerceptualHash.getHash()

        Log.i("hashResult8Bit_obj", hashResult8Bit)
        Log.i("hashResult16Bit_obj", hashResult16Bit)
    }
}
