package com.example.firebasetest.lsy.imageShareApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView


class TestAdmobActivity : AppCompatActivity() {
    private var mAdView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.firebasetest.lsy.R.layout.activity_main)

    }
}