package com.example.firebasetest.lsy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lsy.MyApplication.Companion.db
import com.example.firebasetest.lsy.Utils.MyUtil
import com.example.firebasetest.lsy.databinding.ActivityAddFireStoreBinding
import java.util.Date

class AddFireStoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddFireStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFireStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FireStore crud 확인.
        // 아이디(기본값), 게시글 내용, 날짜(기본값),
        binding.insertBtn.setOnClickListener {
            val data = mapOf(
//                "email" to MyApplication.email,
                "content" to binding.inputEdt.text.toString(),
                "date" to MyUtil.dateToString(Date())
            )
            // 스토어에 넣기. NoSQL 기반, JSON과 비슷함.
            MyApplication.db.collection("TestBoard")
                // 데이터 추가
                .add(data)
                // 데이터 추가 성공후 실행할 콜백 함수
                .addOnCompleteListener{
                    Log.d("lsy","글쓰기 성공")
                    Toast.makeText(this,"글쓰기 성공",Toast.LENGTH_SHORT).show()
                    binding.inputEdt.text.clear()
                }
                // 데이터 추가 실패후 실행할 콜백 함수
                .addOnFailureListener {
                    Log.d("lsy","글쓰기 실패")
                    Toast.makeText(this,"글쓰기 실패",Toast.LENGTH_SHORT).show()
                }

        }

        //불러오기 ,리사이클러뷰 이용해서, 조금있다가하고
        // 현재 한개만 샘플로 불러오기.
        // 공식문서 :
        // https://firebase.google.com/docs/firestore/query-data/get-data?hl=ko#kotlin+ktx
        val docRef = db.collection("TestBoard").document("5gpqhgSocpBr764zhTeK")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("lsy", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("lsy", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("lsy", "get failed with ", exception)
            }

    } // onCreate
}