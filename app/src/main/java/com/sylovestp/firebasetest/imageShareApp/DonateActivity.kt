package com.sylovestp.firebasetest.imageShareApp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sylovestp.firebasetest.imageShareApp.databinding.ActivityDonateBinding

class DonateActivity : AppCompatActivity() {
    lateinit var binding: ActivityDonateBinding
    //admob광고
    lateinit var mAdView: AdView

    //전면 광고
    private var mInterstitialAd: InterstitialAd? = null
    //전면 광고
    var adRequest = AdRequest.Builder().build()

    //보상 광고
    private var rewardedAd: RewardedAd? = null

    private final var TAG = "lsy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //보상형 광고
        RewardedAd.load(this,"ca-app-pub-1537114386989171/1168332157", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                rewardedAd = ad
            }
        })

        //보상형 광고
        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        //전면 광고
        InterstitialAd.load(this,"ca-app-pub-1537114386989171/2397653345", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        }) //load

        //광고 admob 샘플 코드
        MobileAds.initialize(this)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        binding.donateRewardAdBtn.setOnClickListener {
            rewardAdshowDialog()
        }

        binding.donateFowardAdBtn.setOnClickListener {
            fowardAdshowDialog()
        }


        //전면광고
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

    }// onCreate




    private fun rewardAdshowDialog(){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("개발자 도와주기")
            .setMessage("보상 광고 보기로 개발자에게 도움을 줄까요?")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("YES") { dialog , which ->
                // 기능구현
                rewardedAd?.let { ad ->
                    ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                        // Handle the reward.
                        val rewardAmount = rewardItem.amount
                        val rewardType = rewardItem.type
                        Log.d(TAG, "User earned the reward.")
                    })
                } ?: run {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.")
                }

            }.setNegativeButton("NO") {dialog, which ->

            }

            .create()
            .show()
    }

    private fun fowardAdshowDialog(){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("개발자 도와주기")
            .setMessage("전면 광고 보기로 개발자에게 도움을 줄까요?")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("YES") { dialog , which ->
                // 기능구현
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.")
                }

            }.setNegativeButton("NO") {dialog, which ->

            }

            .create()
            .show()
    }
}