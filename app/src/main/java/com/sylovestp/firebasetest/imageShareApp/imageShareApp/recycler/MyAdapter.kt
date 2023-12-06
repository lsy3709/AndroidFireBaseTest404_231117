package com.sylovestp.firebasetest.imageShareApp.imageShareApp.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sylovestp.firebasetest.imageShareApp.MyApplication
import com.sylovestp.firebasetest.imageShareApp.databinding.ItemMainBinding
import com.sylovestp.firebasetest.imageShareApp.imageShareApp.ItemDetailActivity
import com.sylovestp.firebasetest.imageShareApp.imageShareApp.model.ItemData

//뷰홀더 설정.
// 아이템 요소의 뷰를 설정하기. -> 목록요소 뷰 -> 메뉴 아이템 뷰 구성. 백
class MyViewHolder(val binding : ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val itemList: MutableList<ItemData>)
    :RecyclerView.Adapter<MyViewHolder>() {
    // 목록 요소에, 데이터를 연결 시켜주는 역할.
    // 뷰홀더 있음.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemMainBinding.inflate(layoutInflater))
    }

    // 넘어온 전체 데이터 크기.
    override fun getItemCount(): Int {
        return itemList.size
    }

    // 뷰와 데이터 붙이기 작업(연동)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            emailResultView.text = data.email
            dateResultView.text = data.date
            contentResultView.text = data.content
        }

        //로그인 유저와, 작성자 일치시, 수정 , 삭제 버튼 보이기.
//        if(data.email == MyApplication.email) {
//            holder.binding.deleteBtn.visibility = View.VISIBLE
//            holder.binding.updateBtn.visibility = View.VISIBLE
//        } else {
//            holder.binding.deleteBtn.visibility = View.GONE
//            holder.binding.updateBtn.visibility = View.GONE
//        }

        // 스토리지에서 이미지 불러와서, Glide 로 출력하기.
        val imgRef = MyApplication.storage.reference
            // 사진이 한장이라서, 게시글의 id(자동생성된값)이용하고 있음.
            .child("AndroidImageShareApp/${data.docId}.jpg")
        // 다운로드, 스토리지에서, 이미지의 저장소의 URL 가져오기.
        imgRef.downloadUrl
            .addOnCompleteListener{
                // 성공시 수행할 콜백 함수, task 안에, 이미지의 url 담겨 있음.
                task ->
                if(task.isSuccessful) {
                    // with 동작을 해당 activity, 등 context로 대체 .
                    Glide.with(context)
                        // URL 주소로 이미지 불러오기.
                        .load(task.result)
                        //결과 뷰에 이미지 넣기.
                        .into(holder.binding.imageResultView)
                }

            } // addOnCompleteListener

        // 삭제 기능.
        // 스토어, 이미지 삭제.
//        holder.binding.deleteBtn.setOnClickListener {
//            MyApplication.db.collection("AndroidImageShareApp")
//                .document("${data.docId}")
//                .delete()
//                .addOnSuccessListener {
//                    Log.d("lsy", "스토어 successfully deleted!")
//                    Toast.makeText(context,"스토어 삭제 성공", Toast.LENGTH_SHORT).show()
//
//                    // 스토리지 저장소 이미지도 같이 제거.
//
//                    // Create a storage reference from our app
//                    val storageRef = storage.reference
//
//                    // Create a reference to the file to delete
//                    val desertRef = storageRef.child("AndroidImageShareApp/${data.docId}.jpg")
//
//                    // Delete the file
//                    desertRef.delete().addOnSuccessListener {
//                        // File deleted successfully
//                        Log.d("lsy", "스토리지 successfully deleted!")
//                        Toast.makeText(context,"스토리지 삭제 성공", Toast.LENGTH_SHORT).show()
//                        itemList.removeAt(position)
//                        notifyItemRemoved(position)
////                        val intent = (context as Activity).intent
////                        context.finish() //현재 액티비티 종료 실시
////                        context.overridePendingTransition(0, 0) //효과 없애기
////                        context.startActivity(intent) //현재 액티비티 재실행 실시
////                        context.overridePendingTransition(0, 0) //효과 없애기
//                    }.addOnFailureListener {
//                        // Uh-oh, an error occurred!
//                        Log.d("lsy", "스토리지 failed deleted!")
//                        Toast.makeText(context,"스토리지 삭제 실패", Toast.LENGTH_SHORT).show()
//                    }
//
//
//
//                }
//                .addOnFailureListener { e ->
//                    Log.w("lsy", "Error deleting document", e)
//                    Toast.makeText(context,"삭제 실패", Toast.LENGTH_SHORT).show()
//                }
//        } //  // 삭제 기능.

        // 상세페이지 이동.
//        holder.binding.updateBtn.setOnClickListener {
//            // 인텐트 이동시 특정 값을 넣어서 보내고,
//        // 2번째 화면에서는 인텐트에서 꺼내서 사용하기.
////            val intent = (context as Activity).intent
//            val intent = Intent(context, ItemDetailActivity::class.java)
////            var docId: String? = null
////            var email: String? = null
////            var content: String? = null
////            var date: String? = null
//
//            // 데이터 추가 해보기.
//            intent.putExtra("docId",data.docId)
//            intent.putExtra("email",data.email)
//            intent.putExtra("content",data.content)
//            intent.putExtra("date",data.date)
//            context.startActivity(intent)
//        }
        holder.binding.imageResultView.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
//            var docId: String? = null
//            var email: String? = null
//            var content: String? = null
//            var date: String? = null

            // 데이터 추가 해보기.
            intent.putExtra("docId",data.docId)
            intent.putExtra("email",data.email)
            intent.putExtra("content",data.content)
            intent.putExtra("date",data.date)
            context.startActivity(intent)
        }

    } // onBindViewHolder

} //MyAdapter


