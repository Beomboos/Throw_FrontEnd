package com.example.deamhome.presentation.main.map.storeInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deamhome.databinding.ItemMapStoreInfoBinding
import com.example.deamhome.domain.model.MapStoreInfo

class MapStoreInfoViewHolder private constructor(
    val binding: ItemMapStoreInfoBinding,
    onStoreCall: (MapStoreInfo) -> Unit,
    onNavigatorStart: (MapStoreInfo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onCall = onStoreCall
        binding.onNavigate = onNavigatorStart
    }

    fun bind(mapStoreInfo: MapStoreInfo) {
        binding.item = mapStoreInfo
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onStoreCall: (MapStoreInfo) -> Unit,
            onNavigatorStart: (MapStoreInfo) -> Unit,
        ): MapStoreInfoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMapStoreInfoBinding.inflate(layoutInflater, parent, false)
            return MapStoreInfoViewHolder(binding, onStoreCall, onNavigatorStart)
        }
    }
}
