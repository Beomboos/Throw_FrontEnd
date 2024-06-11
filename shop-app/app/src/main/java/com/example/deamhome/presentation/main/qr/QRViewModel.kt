package com.example.deamhome.presentation.main.qr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.StoreRepository
import com.example.deamhome.presentation.main.store.home.StoreViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QRViewModel(
    private val storeRepository: StoreRepository
): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow();
    val event: SharedFlow<Event>
        get() = _event

    private val _bitMap: MutableStateFlow<Bitmap> = MutableStateFlow(Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888));
    val bitMap: StateFlow<Bitmap>
        get() = _bitMap

    init{
    }

    fun qr(extStoreId: String){
        viewModelScope.launch{
            when(val response = storeRepository.qr(extStoreId)){
                is ApiResponse.Success -> {
                    response.body.let {responseBody ->
                        val inputStream = responseBody.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        _bitMap.update {
                            bitmap
                        }
                    }
                }

                is ApiResponse.Failure -> {

                }

                else -> {

                }
            }
        }
    }

    sealed interface Event{
        data object NavigateToCamera: Event
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return QRViewModel(
                    DeamHomeApplication.container.storeRepository,
                ) as T
            }
        }
    }
}