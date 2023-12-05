package com.sylovestp.firebasetest.imageShareApp.Utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
            // 33버전 이상: READ_MEDIA_IMAGES
            // 33버전 미만: READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if(ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) !== PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {

                if(ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) !== PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }


        }
        fun dateToString(date: Date):String {
            val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
            return format.format(date)
        }

    // 갤러리 사진 선택후 filePath 가져오기. : 액티비티용, 프래그먼트,

        // Toast(this(
        fun pickGalleryToFilePath(activity: AppCompatActivity, filePath:String, resultView: ImageView) {
            // 1) 버튼,메뉴등 : 갤러리에서 사진을 선택 후,
            // 2)가져와서 처리하는 , 후처리 함수 만들기.
            var filePath2 = filePath
            val requestLauncher = activity.registerForActivityResult(
                // 갤러리에서, 사진을 선택해서 가져왔을 때, 수행할 함수.
                ActivityResultContracts.StartActivityForResult()
            ) {
                // it 이라는 곳에 사진 이미지가 있음.
                if(it.resultCode === android.app.Activity.RESULT_OK) {
                    // 이미지 불러오는 라이브러리 glide 사용하기, 코루틴이 적용이되어서, 매우 빠름.
                    // OOM(Out Of Memory 해결), gif 움직이는 사진도 가능.
                    // Glide 설치하기. build.gradle
                    // with(this) this 현재 액티비티 가리킴. 대신해서.
                    // 1) applicationContext
                    // 2) getApplicationContext()
                    Glide
                        .with(activity)
                        // 사진을 읽기.
                        .load(it.data?.data)
                        // 크기 지정 , 가로,세로
                        .apply(RequestOptions().override(250,200))
                        // 선택된 사진 크기 자동 조정
                        .centerCrop()
                        // 결과 뷰에 사진 넣기.
                        .into(resultView)

                    // filePath, 갤러리에서 불러온 이미지 파일 정보 가져오기.
                    // 통으로 샘플코드 처러 사용하면 됨.
                    // 커서에 이미지 파일이름이 등록이 되어 있음.
                    val cursor = activity.contentResolver.query(it.data?.data as Uri,
                        arrayOf<String>(MediaStore.Images.Media.DATA),null,
                        null,null);

                    cursor?.moveToFirst().let {
                        filePath2 = cursor?.getString(0) as String
                    }
                    Log.d("lsy","filePath : ${filePath2}")
                    Toast.makeText(activity,"filePath : ${filePath2}", Toast.LENGTH_LONG).show()
//                binding.resultFilepath.text = filePath
                } // 조건문 닫는 블록
            } // 후처리 처리 닫는 블록
        // 1) 갤러리 호출 인텐트
        // 2) 후처리 함수가 필요함.
        // 갤러리 사진 선택후, 결과 이미지 뷰에 , 프리뷰 미리보기 구성.
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        requestLauncher.launch(intent)

        // 갤러리에서, 사진을 선택 후, 후처리하는 로직.
        // 이미 구성을 했어요.
        // AddActivity 에서 가져오기.


    }

    // upload 이미지



    }
}