package com.example.firebasetest.lsy.imageShareApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lsy.databinding.ActivityItemDetailBinding

class ItemDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}