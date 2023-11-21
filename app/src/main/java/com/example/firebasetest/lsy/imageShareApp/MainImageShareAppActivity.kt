package com.example.firebasetest.lsy.imageShareApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lsy.databinding.ActivityMainImageShareAppBinding

// 스토어, 스토리지에서  데이터를 받아서, 리사이클러뷰 로 출력할 예정.
// 인증, 구글인증, 이메일 , 패스워드 인증 재사용.
// 인증이 되어야 글쓰기 가능하게 하고,
// 일단, 삭제한번 도전해보고,
// 수정 도전해보기.
class MainImageShareAppActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainImageShareAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainImageShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }// onCreate


}