package com.example.deamhome.presentation.main.store.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.domain.repository.StoreRepository
import com.example.deamhome.presentation.main.store.register.RegisterViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.StringTokenizer

class ModifyViewModel(
    private val storeRepository: StoreRepository,
): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _isPopup: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isPopup: StateFlow<Boolean>
        get() = _isPopup

    val crn: MutableStateFlow<String> = MutableStateFlow("")
    val phone: MutableStateFlow<String> = MutableStateFlow("")
    val fullAddress: MutableStateFlow<String> = MutableStateFlow("")
    val subAddress: MutableStateFlow<String> = MutableStateFlow("")
    val zoneNo: MutableStateFlow<String> = MutableStateFlow("");
    //일반쓰레기
    val general: MutableStateFlow<Boolean> = MutableStateFlow(false)
    //병
    val bottle: MutableStateFlow<Boolean> = MutableStateFlow(false)
    //플라스틱
    val plastic: MutableStateFlow<Boolean> = MutableStateFlow(false)
    //종이
    val paper: MutableStateFlow<Boolean> = MutableStateFlow(false)
    //캔
    val can: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _trashType: MutableStateFlow<RegisterViewModel.TrashType> = MutableStateFlow(
        RegisterViewModel.TrashType(
            general = "0",
            bottle = "0",
            plastic = "0",
            paper = "0",
            can = "0"
        )
    )

    private val _store: MutableStateFlow<StoreResponse> = MutableStateFlow(StoreResponse.EMPTY)
    val store: StateFlow<StoreResponse>
        get() = _store

    fun addressSelect(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToAddress)
        }
    }

    fun modify(){

    }

    fun select(type: SelectType){
        viewModelScope.launch {
            when(type){
                SelectType.DELETE -> {
                    _event.emit(Event.Select(type))
                }

                SelectType.MODIFY -> {
                    _isPopup.value = false;
                    _event.emit(Event.Select(type))
                }
            }

        }
    }

    fun delete(){
    }

    fun storeUpdate(store: StoreResponse){
        viewModelScope.launch {
            _store.update {
                it.copy(
                    extStoreId = store.extStoreId,
                    storeName = store.storeName,
                    storePhone = store.storePhone,
                    crn = store.crn,
                    latitude = store.latitude,
                    longitude = store.longitude,
                    zipCode = store.zipCode,
                    fullAddress = store.fullAddress,
                    trashType = store.trashType
                )
            }
        }
    }

    fun uiUpdate(){
        viewModelScope.launch{
            if(!store.value.crn.isNullOrEmpty()) {
                crn.value = crnFormat(store.value.crn)
                phone.value = storePhoneFormat(store.value.storePhone)
                fullAddress.value = store.value.fullAddress
                zoneNo.value = store.value.zipCode
                trashTypeSetting(store.value.trashType)
            }
        }
    }

    fun trashTypeSetting(code: String){
        if(code[0]==('1')) general.value = true
        if(code[1]==('1')) bottle.value = true
        if(code[2]==('1')) plastic.value = true
        if(code[3]==('1')) paper.value = true
        if(code[4]==('1')) can.value = true
    }

    fun crnFormat(crn: String): String{
        var crnFormat:String = crn
        if(!crn.contains("-")) {
            crnFormat =
                crn.substring(0, 3) + "-" + crn.substring(3, 5) + "-" + crn.substring(5, 9)
        }
        return crnFormat
    }

    fun storePhoneFormat(storePhone: String): String{
        var storeFormat:String = storePhone
        if(!storePhone.contains("-")){
            if(storePhone.length==11){
                storeFormat = storePhone.substring(0,3)+"-"+storePhone.substring(3,7)+"-"+storePhone.substring(7,11)
            }
            else if(storePhone.length==10){
                if(storePhone.substring(0,2).equals("02")){
                    storeFormat = storePhone.substring(0,2)+"-"+storePhone.substring(2,6)+"-"+storePhone.substring(6,10)
                }
                storeFormat = storePhone.substring(0,3)+"-"+storePhone.substring(3,6)+"-"+storePhone.substring(6,10)
            }
            else{
                storeFormat = storePhone.substring(0,2)+"-"+storePhone.substring(2,5)+"-"+storePhone.substring(5,9)
            }
        }
        return storeFormat
    }

    fun trashFormat() {
        viewModelScope.launch {
            _trashType.update {
                it.copy(
                    general = if (!general.value) "0" else "1",
                    bottle = if (!bottle.value) "0" else "1",
                    plastic = if (!plastic.value) "0" else "1",
                    paper = if (!paper.value) "0" else "1",
                    can = if (!can.value) "0" else "1"
                )
            }
        }
    }

    sealed interface Event{
        data class Failed(val message: String): Event
        data object DeleteSuccess: Event
        data object ModifySuccess: Event
        data class Select(val type: SelectType): Event
        data object NavigateToAddress: Event
    }

    companion object{

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return ModifyViewModel(
                    DeamHomeApplication.container.storeRepository
                ) as T
            }
        }
    }
}