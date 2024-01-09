package com.sylovestp.firebasetest.imageShareApp.imageShareApp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sylovestp.firebasetest.imageShareApp.R
import com.sylovestp.firebasetest.imageShareApp.databinding.ActivitySettingEuBinding

class SettingEuActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingEuBinding
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingEuBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}