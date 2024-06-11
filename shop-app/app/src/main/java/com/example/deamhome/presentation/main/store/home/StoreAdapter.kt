package com.example.deamhome.presentation.main.store.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deamhome.R
import com.example.deamhome.data.model.response.StoreResponse
import kotlin.coroutines.coroutineContext

//RecyclerView를 list_view_item.xml을 연결해주기 위한 adapter이다
class StoreAdapter(
    private var items: List<StoreResponse>,
    val onManageStoreClick: (StoreResponse) -> Unit,
    val onScanQrClick: (StoreResponse) -> Unit
): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>(){
    private var expandedPosition = -1 // 확장된 아이템의 위치를 저장

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store_list, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val item = items[position]
        holder.storeName.text = item.storeName
        holder.storeType.text = trashTypeTransform(item.trashType)

        Glide.with(holder.storeImg.context)
            .load("https://www.bizhankook.com/upload/bk/article/202002/thumb/19402-44437-sampleM.jpg")
            .into(holder.storeImg)

        val isExpanded = position == expandedPosition
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
        }

        holder.btnManageStore.setOnClickListener {
            onManageStoreClick(item)
        }

        holder.btnScanQr.setOnClickListener {
            onScanQrClick(item)
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
        val storeImg = itemView.findViewById<ImageView>(R.id.iv_image_store)
        val expandedLayout = itemView.findViewById<LinearLayout>(R.id.expanded_layout)
        val btnManageStore = itemView.findViewById<Button>(R.id.btn_manage_store)
        val btnScanQr = itemView.findViewById<Button>(R.id.btn_scan_qr)
    }
}