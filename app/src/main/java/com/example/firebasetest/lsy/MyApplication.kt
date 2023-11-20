package com.example.firebasetest.lsy

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class MyApplication : MultiDexApplication() {
    companion object {
        // 원래는 자바에서, 해당 클래스의 멤버를 사용하는 방법2가지.
        // 1) 인스턴스를 생성해서 접근 2) static 으로 클래스명으로 접근.
        // 자바 : A a = new A(); -> a. 접근.
        // 코틀린 : val a = A(); -> a. 접근.

        // static 과 비슷, 해당 클래스명으로 멤버에 접근이 가능함.
        // 인증 기능에 접근하는 인스턴스 가 필요함.
        // 선언만 하고, 초기화를 안했음.
        // 초기화를 밑에 onCreate 최초1회 실행시 초기화됨.
        lateinit var auth: FirebaseAuth
        // 인증할 이메일
        var email : String? = null

        // 이미지 저장소 , 인스턴스 도구
        lateinit var storage: FirebaseStorage
        // 파이어 스토어, 인스턴스 도구
        lateinit var db : FirebaseFirestore


        // MyApplication.checkAuth() : 이렇게 클래스명. 함수및 특정 변수에 접근이 가능함.
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
        // 초기화를 함.
        auth = Firebase.auth
        storage = Firebase.storage
        db = FirebaseFirestore.getInstance()

    }


}