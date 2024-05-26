package com.example.deamhome.presentation.main.store.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.BuildConfig
//import com.example.deamhome.BuildConfig
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.data.model.response.address.DocumentResponse
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.KakaoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel(
    private val kakaoRepository: KakaoRepository,
) : ViewModel() {

    private val _evnet: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _evnet

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _addressUIState: MutableStateFlow<List<DocumentResponse>> = MutableStateFlow(
        emptyList()
    )
    val addressUIState: StateFlow<List<DocumentResponse>>
        get() = _addressUIState

    //SearchView에 검색할 데이터를 AddressBindingAdapter를 통해서 content에 전달받는다
    val searchAddress = { content: String? ->
        if (content.isNullOrEmpty().not()) {
            search(content.toString())
        }
        else search("");
    }

    fun search(content: String){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            if(content.equals("")){
                _evnet.emit(Event.AddressFailed("검색할 주소를 입력해주세요."))
                _isLoading.value = false
                return@launch
            }
            when(val response = kakaoRepository.addressSearch(BuildConfig.KAKAO_REST_KEY, content)){
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        _addressUIState.update { data.documents }
                    }
                }
                is ApiResponse.Failure -> {
                    _evnet.emit(Event.AddressFailed("주소를 정확히 입력해 주세요."))
                }
                else ->{
                }
            }
        }
    }

    fun selectAddress(address: DocumentResponse){
        viewModelScope.launch {
            _evnet.emit(Event.AddressSuccess(address))
        }
    }

    sealed interface Event{
        data class AddressSuccess(val address: DocumentResponse): Event
        data class AddressFailed(val message: String): Event
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return AddressViewModel(
                    DeamHomeApplication.container.kakaoRepository,
                ) as T
            }
        }
    }
}