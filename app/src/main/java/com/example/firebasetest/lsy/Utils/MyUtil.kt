package com.example.firebasetest.lsy.Utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date

class MyUtil {
    // 자주 쓰는 기능들을 모아뒀다가, 필요시 사용하기.
    // 1) 날짜 변환 부터 여기에 사용.
    // 2) 퍼미션 권한 물어보기.
    companion object {

        //퍼미션 체크 함수 만들기.
        fun checkPermission (activity: AppCompatActivity) {
            //후처리 함수, 1번 앱 -> 2번 앱으로 가서,
            // 1)사진 선택, 2) 권한을 획득을 하건, 뭔가 2번 앱에서 가져오기.
            // 2번,돌아 왔을 때 처리하는 함수
            // 1번, 해당 권한을 요청하는 인텐트 요청.
            // 2번, 처리하는 함수 구성.
            val requestPermissionLauncher = activity.registerForActivityResult(
                // 권한 요청 후, 확인시 사용.
                ActivityResultContracts.RequestPermission()
            ) {
                if (it) {
                    Toast.makeText(activity,"권한 승인됨",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity,"권한 승인 안됨",Toast.LENGTH_SHORT).show()
                }
            }

            // 1번 요청하기.
            if(ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }
        fun dateToString(date: Date):String {
            val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
            return format.format(date)
        }


    }
}