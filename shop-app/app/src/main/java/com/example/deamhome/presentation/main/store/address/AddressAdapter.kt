package com.example.deamhome.presentation.main.store.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deamhome.R
import com.example.deamhome.data.model.response.address.DocumentResponse

class AddressAdapter(
    private var items: List<DocumentResponse>,
    val onClick: (DocumentResponse)->Unit
): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address_list, parent,false)
        return AddressViewHolder(view)
    }

    //list_view_item.xml과 연동된 TextView의 값을 리스트 마다 지정해줌
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.address.text =
            if(items[position].address!=null && items[position].address?.address_name.isNullOrEmpty().not())
                items[position].address?.address_name
            else "지번 주소"
        holder.roadAddress.text =
            if(items[position].road_address!=null && items[position].road_address?.address_name.isNullOrEmpty().not())
                items[position].road_address?.address_name
            else "도로명 주소"
        holder.zoneNo.text =
            if(items[position].road_address!=null && items[position].road_address?.zone_no.isNullOrEmpty().not())
                items[position].road_address?.zone_no
            else "우편번호"
        holder.itemView.setOnClickListener{
            onClick(items[position])
        }
    }

    fun updateList(list: ArrayList<DocumentResponse>){
        items = list
        notifyDataSetChanged()
    }

    //items의 갯수를 반환하여 RecyclerView에 나타나는 총 갯수를 보여줌
    override fun getItemCount(): Int {
        return items.size
    }

    //list_view_item.xml의 TextView를 연동시켜줌
    inner class AddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val address = itemView.findViewById<TextView>(R.id.address)
        val roadAddress = itemView.findViewById<TextView>(R.id.roadAddress)
        val zoneNo = itemView.findViewById<TextView>(R.id.zoneNo)
    }
}