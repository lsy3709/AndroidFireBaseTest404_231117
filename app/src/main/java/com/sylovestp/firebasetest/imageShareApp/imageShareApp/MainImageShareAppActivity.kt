package com.sylovestp.firebasetest.imageShareApp.imageShareApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.sylovestp.firebasetest.imageShareApp.AuthActivity
import com.sylovestp.firebasetest.imageShareApp.DonateActivity
import com.sylovestp.firebasetest.imageShareApp.MyApplication
import com.sylovestp.firebasetest.imageShareApp.R
import com.sylovestp.firebasetest.imageShareApp.Utils.MyUtil
import com.sylovestp.firebasetest.imageShareApp.databinding.ActivityMainImageShareAppBinding
import com.sylovestp.firebasetest.imageShareApp.imageShareApp.model.ItemData
import com.sylovestp.firebasetest.imageShareApp.imageShareApp.recycler.MyAdapter
import java.util.concurrent.atomic.AtomicBoolean

// 스토어, 스토리지에서  데이터를 받아서, 리사이클러뷰 로 출력할 예정.
// 인증, 구글인증, 이메일 , 패스워드 인증 재사용.
// 인증이 되어야 글쓰기 가능하게 하고,
// 일단, 삭제한번 도전해보고,
// 수정 도전해보기.
class MainImageShareAppActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainImageShareAppBinding
    lateinit var myAdapter : MyAdapter
    // admob 광고
    lateinit var mAdView : AdView

    private final var TAG = "lsy"

    // 유럽 연합 , 영국 개인정보 관련
    private lateinit var consentInformation: ConsentInformation

    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainImageShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 유럽 연합 , 영국 개인정보 관련
        // Create a ConsentRequestParameters object.
        val params = ConsentRequestParameters
            .Builder()
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            ConsentInformation.OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainImageShareAppActivity,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        if (loadAndShowError != null) {
                            Log.w(TAG, String.format("%s: %s",
                                loadAndShowError.errorCode,
                                loadAndShowError.message
                            ))
                        }

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            ConsentInformation.OnConsentInfoUpdateFailureListener {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w(TAG, String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message
                ))
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }





        //광고 admob 샘플 코드
//        MobileAds.initialize(this)
//
//        MobileAds.initialize(this) {}
//
//        mAdView = findViewById(R.id.adView)
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)


        // 툴바 붙이기
        setSupportActionBar(binding.toolbarMain)

        // 해당 메인 앱이 실행이 되면, 먼저, 현재 앱의 외부 스토리지에 접근 권한 체크를 하기.
        MyUtil.checkPermission(this)

        // 플로팅 액션 번튼 클릭시, 글쓰기 뷰로 이동하기 작업.
        binding.addFabtn.setOnClickListener {
            // 글쓰기 전에, 인증 여부 확인 하기.
            if(MyApplication.checkAuth()) {
                // 글쓰기 페이지 이동 -> AddImageShareApp
                //이름 수정 : AddImageShareAppActivity
                startActivity(Intent(this, AddImageShareAppActivity::class.java))
            } else {
                Toast.makeText(this,"인증 후 글쓰기 해주세요",Toast.LENGTH_SHORT).show()
            }
        }




    }// onCreate

    fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

        // TODO: Request an ad.
        MobileAds.initialize(this)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()){
            // 리사이클러뷰 작업하기. 결과 데이터 가져오기.
            makeRecyclerView()
            binding.recyclerView.visibility = View.VISIBLE
            binding.logoutTextResult.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.logoutTextResult.visibility = View.VISIBLE

        }
    }
    // 메뉴 붙이기. 작업.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.auth_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 붙힌 메뉴 이벤트 처리하기.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId === R.id.menu_add_auth) {
           // 인증 후, 실행할 로직.
            // AuthActivity 구성해서, 이 화면으로 이동하게끔 설정.
            startActivity(Intent(this, AuthActivity::class.java))

        } else if (item.itemId === R.id.menu_add_donate) {
            startActivity(Intent(this, DonateActivity::class.java))

        }
        // 저장 구성, 인증은 메인으로 옮기기
        return super.onOptionsItemSelected(item)
    }

    // 리사이클러뷰 호출하는 함수 만들기.
    private fun makeRecyclerView() {
        // 스토어에서, 데이터를 모두 가져오기. 참고로
        // 게시글 id이름, 이미지 이름 동일함. docId 임.

        MyApplication.db.collection("AndroidImageShareApp")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()
                for( doc in result) {
                    // toObject -> 데이터 모델링, 바인딩.
                    val item = doc.toObject(ItemData::class.java)
                    item.docId = doc.id
                    itemList.add(item)
                }
                // 리사이클러 뷰에, 1)리니어 매니저, 2)어댑터 등록
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                myAdapter = MyAdapter(this,itemList)
                binding.recyclerView.adapter = myAdapter
//                binding.recyclerView.adapter = MyAdapter(this,itemList)
            }
            .addOnFailureListener {
                Toast.makeText(this,"서버 데이터 결과 조회 실패",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"실패")
            }

    } // makeRecyclerView


}