package com.example.firebasetest.lsy

import android.os.Bundle
import android.util.Log
import android.view.View
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
            // 인증 되면, mode에 따라 보여주는 함수를 동작 시키기
            chageVisi("login")
        } else {
            Log.d("lsy","로그인 인증이 안됨")
            // 인증 되면, mode에 따라 보여주는 함수를 동작 시키기
            chageVisi("loout")
        }

     // onCreate
    }

    //임의의 함수를 만들기.
    // 인증의 상태에 따라서, 로그인 화면을 표시 여부.
    // 예) 로그인이 되면, 로그아웃 버튼을 보이고,
    // 예) 로그인이 안되면, 로그아웃 버튼을 사라지게 만들기. 등
    fun chageVisi(mode: String) {
        if (mode === "로그인") {
            // 로그인이 되었다면, 인증된 이메일도 이미 등록이 되어서, 가져와서 사용하기.
            binding.authMainText.text = "${MyApplication.email} 님 반가워요."
            binding.logoutBtn.visibility = View.VISIBLE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.authEmailEdit.visibility = View.GONE
            binding.authPasswordEdit.visibility = View.GONE
            binding.logInBtn.visibility = View.GONE
        } else if( mode === "logout") {
            binding.authMainText.text ="로그인 하거나 회원가입 해주세요."
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.VISIBLE
            binding.signInBtn2.visibility = View.VISIBLE
            binding.googleAuthInBtn.visibility = View.VISIBLE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.VISIBLE

        } else if( mode === "signIn") {
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.GONE

        }
    }


}







