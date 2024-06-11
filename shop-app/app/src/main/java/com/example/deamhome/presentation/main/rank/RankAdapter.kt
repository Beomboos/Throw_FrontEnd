package com.example.deamhome.presentation.main.rank

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deamhome.R
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore

class RankAdapter(
    private var items: List<LeaderboardResponse>,
    val onClick: (String)->Unit
): RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    var expandedPosition = -1 // 확장된 아이템의 위치를 저장
    private var rankerStoreMap: Map<Int, List<RankerStore>> = emptyMap() // 포지션별 하위 데이터를 저장
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rank_list, parent, false)
        return RankViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val item = items[position]

        holder.rankText.text = item.ranking.toString()
        holder.userText.text = item.userName
        holder.mileageText.text = item.mileage.toString()

        if(item.ranking.toInt()==1) {
            holder.rankNumber.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8BB4F"))
            holder.rankText.setTextColor(Color.parseColor("#F8EBBA"))
        }
        else if(item.ranking.toInt()==2) {
            holder.rankNumber.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#B3D3F0"))
            holder.rankText.setTextColor(Color.parseColor("#E4F2FE"))
        }
        else if(item.ranking.toInt()==3) {
            holder.rankNumber.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E2A179"))
            holder.rankText.setTextColor(Color.parseColor("#EDC3B5"))
        }

        val isExpanded = position == expandedPosition
        holder.expandedRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
            if(!isExpanded) {
                onClick(item.inputId);
            }
        }

        if (rankerStoreMap.containsKey(position)) {
            val rankerStoreAdapter = RankerStoreAdapter(rankerStoreMap[position] ?: emptyList())
            holder.expandedRecyclerView.adapter = rankerStoreAdapter
            holder.expandedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun updateRankerStores(position: Int, rankerStores: List<RankerStore>) {
        rankerStoreMap = rankerStoreMap.toMutableMap().apply { put(position, rankerStores) }
        notifyItemChanged(position)
    }

    inner class RankViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val rankNumber = itemView.findViewById<FrameLayout>(R.id.rank_number)
        val rankText = itemView.findViewById<TextView>(R.id.rank_text)
        val userText = itemView.findViewById<TextView>(R.id.tx_user)
        val mileageText = itemView.findViewById<TextView>(R.id.tx_mileage)
        val expandedRecyclerView = itemView.findViewById<RecyclerView>(R.id.rc_store)
    }
}