package com.sylovestp.firebasetest.imageShareApp.imageShareApp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.sylovestp.firebasetest.imageShareApp.databinding.ActivitySettingEuBinding

class SettingEuActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingEuBinding
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingEuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(this)
        binding.btnGdprSetting.isVisible = googleMobileAdsConsentManager.isPrivacyOptionsRequired
        binding.btnGdprSetting.setOnClickListener {
            googleMobileAdsConsentManager.showPrivacyOptionsForm(this) { formError ->
                if (formError != null) {
                    Toast.makeText(this, formError.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}