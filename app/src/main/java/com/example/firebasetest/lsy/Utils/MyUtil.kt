package com.example.firebasetest.lsy.Utils

import java.text.SimpleDateFormat
import java.util.Date

class MyUtil {
    // 자주 쓰는 기능들을 모아뒀다가, 필요시 사용하기.
    // 1) 날짜 변환 부터 여기에 사용.
    // 2) 퍼미션 권한 물어보기.
    companion object {
        fun dateToString(date: Date):String {
            val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
            return format.format(date)
        }
    }
}