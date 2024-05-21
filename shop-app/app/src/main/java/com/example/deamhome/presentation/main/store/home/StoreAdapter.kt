package com.example.deamhome.presentation.main.store.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deamhome.R
import com.example.deamhome.data.model.response.StoreResponse

//RecyclerView를 list_view_item.xml을 연결해주기 위한 adapter이다
class StoreAdapter(private var items: List<StoreResponse>, val onClick: (StoreResponse)->Unit): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store_list, parent,false)
        return StoreViewHolder(view)
    }

    //list_view_item.xml과 연동된 TextView의 값을 리스트 마다 지정해줌
    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.storeName.text = items[position].storeName
        holder.storeType.text = trashTypeTransform(items[position].trashType)
        holder.itemView.setOnClickListener{
            onClick(items[position])
        }
    }

    fun updateList(list: ArrayList<StoreResponse>){
        items = list
        notifyDataSetChanged()
    }

    fun trashTypeTransform(trashCode: String): String{
        var trashType:String = ""
        trashType += if(trashCode[0]==('1')) "일반쓰레기  " else ""
        trashType += if(trashCode[1]==('1')) "병  " else ""
        trashType += if(trashCode[2]==('1')) "플라스틱  " else ""
        trashType += if(trashCode[3]==('1')) "종이  " else ""
        trashType += if(trashCode[4]==('1')) "캔" else ""
        return trashType
    }

    //items의 갯수를 반환하여 RecyclerView에 나타나는 총 갯수를 보여줌
    override fun getItemCount(): Int {
        return items.size
    }

    //list_view_item.xml의 TextView를 연동시켜줌
    inner class StoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val storeName = itemView.findViewById<TextView>(R.id.store_name)
        val storeType = itemView.findViewById<TextView>(R.id.store_type)
    }
}