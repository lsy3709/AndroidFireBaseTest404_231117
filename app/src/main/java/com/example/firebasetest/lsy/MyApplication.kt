package com.example.firebasetest.lsy

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MyApplication : MultiDexApplication() {
    companion object {
        // static 과 비슷, 해당 클래스명으로 멤버에 접근이 가능함.
        // 인증 기능에 접근하는 인스턴스 가 필요함.
        lateinit var auth: FirebaseAuth
        // 인증할 이메일
        var email : String? = null

        fun checkAuth():Boolean {
            // auth 도구 인증에관련 도구
            // 도구안에 기능중에서, 현재 유저를 확인하는 함수.
            var currentUser = auth.currentUser
            // 현재 유저가 있다면, 해당 유저의 이메일 정보를 가지고 오고
            // 유저 이메일 인증 확인 했는지 유무에 따라서 false
            return  currentUser?.let {
                email = currentUser.email
                // 인증을 했다면, true 결괏값 반환.
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }
    } // companion object

    // 생명주기, 최초 1회 동작을 합니다.
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

    }


}