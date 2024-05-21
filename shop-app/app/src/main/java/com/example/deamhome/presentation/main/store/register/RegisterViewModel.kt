package com.example.deamhome.presentation.main.store.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.util.log
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.StoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading


    val crn: MutableStateFlow<String> = MutableStateFlow("")
    val phone: MutableStateFlow<String> = MutableStateFlow("")
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

    private val _trashType: MutableStateFlow<TrashType> = MutableStateFlow(
        TrashType(
            general = "0",
            bottle = "0",
            plastic = "0",
            paper = "0",
            can = "0"
        )
    )

    private val _crnBtnUiState: MutableStateFlow<CrnBtnUiState> = MutableStateFlow(
        CrnBtnUiState(
            btn = true,
            text = "인증"
        )
    )
    val crnBtnUiState: StateFlow<CrnBtnUiState>
        get() = _crnBtnUiState

    init {
        viewModelScope.launch {
            log(tag = "crn", message = "crn: ${crn.value}")
        }
        viewModelScope.launch {
            log(tag = "crn", message = "phone: ${phone.value}")
        }
    }

    fun isCrn() {
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val crn = crn.value.trim()
        viewModelScope.launch {
            if(crn.isBlank()){
                _event.emit(Event.CrnFailed("사업자 등록번호를 입력하지 않았습니다."));
                _isLoading.value = false
                return@launch
            }
            when(val response = storeRepository.crn(crn)){
                is ApiResponse.Success -> {
                    _event.emit(Event.CrnFailed("이미 있는 가게 입니다."))
                }

                is ApiResponse.Failure -> {
                    if(response.responseCode.toString().equals("404")){
                        _crnBtnUiState.update {
                            it.copy(
                                btn = false,
                                text = "인증완료"
                            )
                        }
                    }
                    else{
                        try {
                            response.error.toString()
                        }
                        catch (e: Exception){
                            response.error?.firstOrNull().toString()
                        }
                    }
                }

                else -> {

                }
            }
            _isLoading.value = false
        }
    }

    fun trashFormat() {
        viewModelScope.launch {
            _trashType.update {
                it.copy(
                    general = if (general.value) "0" else "1",
                    bottle = if (bottle.value) "0" else "1",
                    plastic = if (plastic.value) "0" else "1",
                    paper = if (paper.value) "0" else "1",
                    can = if (can.value) "0" else "1"
                )
            }
        }
    }

    sealed interface Event {
        data class CrnFailed(val message: String): Event

        data object RegisterSuccess: Event

        data class RegisterFailed(val message: String): Event
    }

    data class TrashType(
        val general: String,
        val bottle: String,
        val plastic: String,
        val paper: String,
        val can: String,
    )

    data class CrnBtnUiState(
        val btn: Boolean,
        val text: String,
    )

    companion object {
        val HTTP_LOG: String = "HTTP_LOG"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return RegisterViewModel(
                    DeamHomeApplication.container.storeRepository
                ) as T
            }
        }
    }
}