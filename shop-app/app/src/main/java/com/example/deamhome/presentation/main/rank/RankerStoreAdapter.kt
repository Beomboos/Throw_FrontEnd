package com.example.deamhome.presentation.main.rank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deamhome.R
import com.example.deamhome.data.model.response.RankerStore

class RankerStoreAdapter(
    private val items: List<RankerStore>
): RecyclerView.Adapter<RankerStoreAdapter.RankerStoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankerStoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rank_store_list, parent, false)
        return RankerStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankerStoreViewHolder, position: Int) {
        val item = items[position]

        holder.storeName.text = item.storeName
        Glide.with(holder.storeImg.context)
            .load("https://www.bizhankook.com/upload/bk/article/202002/thumb/19402-44437-sampleM.jpg")
            .into(holder.storeImg)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class RankerStoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val storeImg = itemView.findViewById<ImageView>(R.id.iv_image_store1)
        val storeName = itemView.findViewById<TextView>(R.id.store_name1)
    }
}