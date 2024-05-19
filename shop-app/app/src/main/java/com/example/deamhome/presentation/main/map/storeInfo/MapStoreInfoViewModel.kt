package com.example.deamhome.presentation.main.map.storeInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.deamhome.common.livedata.SingleLiveEvent
import com.example.deamhome.domain.model.GeoPoint
import com.example.deamhome.domain.model.MapStoreInfo

class MapStoreInfoViewModel : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    fun onStoreCall(mapStoreInfo: MapStoreInfo) {
        _event.value = Event.NavigateToCall("01012345678")
    }

    fun onNavigatorStart(mapStoreInfo: MapStoreInfo) {
        _event.value = Event.NavigateToDestination(mapStoreInfo.geoPoint)
    }

    sealed class Event {
        object Dismiss : Event()
        data class NavigateToCall(val phoneNumber: String) : Event()
        data class NavigateToDestination(val destination: GeoPoint) : Event()
    }
}
