package com.example.deamhome.presentation.main.map.storeInfo

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.deamhome.domain.model.MapStoreInfo

class MapStoreInfoAdapter(
    private val onStoreCall: (MapStoreInfo) -> Unit,
    private val onNavigatorStart: (MapStoreInfo) -> Unit,
) : ListAdapter<MapStoreInfo, MapStoreInfoViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapStoreInfoViewHolder {
        return MapStoreInfoViewHolder.create(parent, onStoreCall, onNavigatorStart)
    }

    override fun onBindViewHolder(holder: MapStoreInfoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setStores(stores: List<MapStoreInfo>) {
        submitList(stores)
    }

    companion object {
        private val DiffUtil = object : DiffUtil.ItemCallback<MapStoreInfo>() {
            override fun areItemsTheSame(oldItem: MapStoreInfo, newItem: MapStoreInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MapStoreInfo, newItem: MapStoreInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
