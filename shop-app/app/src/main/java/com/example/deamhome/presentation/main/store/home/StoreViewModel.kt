package com.example.deamhome.presentation.main.store.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.StoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    private val storeRepository: StoreRepository,
): ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val myStoreResponse: MutableStateFlow<List<StoreResponse>> = MutableStateFlow(emptyList())

    init{
        stores()
    }

    fun stores(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when(val response = storeRepository.store()){
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        myStoreResponse.update { data }
                    }
                }

                is ApiResponse.Failure -> {

                }

                else -> {

                }
            }

            _isLoading.value = false;
        }
    }

    fun registerService(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToRegister)
        }
    }

    fun modifyService(store: StoreResponse){
        viewModelScope.launch {
            _event.emit(Event.NavigateToModify(store))
        }
    }

    sealed interface Event{
        data object NavigateToRegister: Event
        data class NavigateToModify(val store: StoreResponse): Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return StoreViewModel(
                    DeamHomeApplication.container.storeRepository,
                ) as T
            }
        }
    }
}