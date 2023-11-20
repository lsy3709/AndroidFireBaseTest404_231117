package com.example.firebasetest.lsy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lsy.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다음 시간에, 이메일, 구글 인증, 후
        // 인증 후 2번째 페이지 접근 확인하고,
        // 그리고 나서, 2번째 페이지 부분을 아래의 기능으로 구성.
        // 스토어, 스토리지 에 접근 및 이용하는 부분 보여 주기.

        //MyApplication 인증 함수를 이용해서,
    // 인증이 되면, 특정 뷰로 인증됨을 확인
        if(MyApplication.checkAuth()){
            Log.d("lsy","로그인 인증이 됨")
        } else {
            Log.d("lsy","로그인 인증이 안됨")
        }

     // onCreate
    }


}