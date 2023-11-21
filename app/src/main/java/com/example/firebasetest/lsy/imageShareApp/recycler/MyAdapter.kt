package com.example.firebasetest.lsy.imageShareApp.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.lsy.databinding.ItemMainBinding
import com.example.firebasetest.lsy.imageShareApp.model.ItemData

//뷰홀더 설정.
// 아이템 요소의 뷰를 설정하기. -> 목록요소 뷰 -> 메뉴 아이템 뷰 구성. 백
class MyViewHolder(val binding : ItemMainBinding ) : RecyclerView.ViewHolder(binding.root)

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

        // 스토리지에서 이미지 불러와서, Glide 로 출력하기.
    }

}