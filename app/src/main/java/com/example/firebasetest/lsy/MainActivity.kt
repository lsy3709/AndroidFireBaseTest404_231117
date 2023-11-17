package com.example.firebasetest.lsy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 일단, 이메일 인증 또는 구글인증이 인증이 되어야
    // 2번째 화면으로 갈수 있게 간단히 설정.
        // 순서
    // 1 MyApplication 설정 파일을 만들어서, 매니페스트에 등록
        // 2 인증 기능을 하는 액티비티 만들기.
        // 3 메뉴에서 인증 클릭시 -> 인증 액티비티 화면으로 이동하게끔.
        // 3-2 메뉴 하위에 필요한 아이템 구성.
        // 4 메인에서 동작하기.


    }
}