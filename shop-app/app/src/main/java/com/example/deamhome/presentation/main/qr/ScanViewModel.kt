package com.example.deamhome.presentation.main.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.StoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow();
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    fun camera(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToCamera);
        }
    }

    fun scan(scan: String){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when(val response = storeRepository.mileageUpdate(scan)){
                is ApiResponse.Success -> {
                    _event.emit(Event.MileageUpdate("마일리지가 변경 되었습니다."));
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.MileageUpdate("마일리지 변경에 실패하였습니다."));
                }

                else -> {
                    _event.emit(Event.MileageUpdate("알 수 없는 에러가 발생하였습니다."));
                }
            }
            _isLoading.value = false
        }
    }

    sealed interface Event{
        data object NavigateToCamera: Event

        data class MileageUpdate(val message: String): Event
    }
    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return ScanViewModel(
                    DeamHomeApplication.container.storeRepository,
                ) as T
            }
        }
    }
}