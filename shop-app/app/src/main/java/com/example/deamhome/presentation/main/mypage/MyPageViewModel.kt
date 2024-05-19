package com.example.deamhome.presentation.main.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val mileage: MutableStateFlow<String> = MutableStateFlow("")

    // 유저 정보 조회에 대한 UiState
    private val _profileUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
        ProfileUiState(
            isLoading = false,
            isError = false,
            data = UserProfile.EMPTY,
        ),
    )
    val profileUiState: StateFlow<ProfileUiState>
        get() = _profileUiState

    fun profileModify(){
    }

    //가게관리 버튼이벤트
    fun store(){
    }

    //마일리지/상품 구매 버튼이벤트
    fun mileage(){
    }


    init {
        inquiry()
    }

    //회원정보 조회
    fun inquiry(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when(val response = productRepository.user()){
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        _profileUiState.update {
                            it.copy(isLoading = false, isError = false, data = data)
                        }
                    }
                }

                is ApiResponse.Failure -> {

                }

                else -> {}
            }

            _isLoading.value = false;
        }
    }

    data class ProfileUiState(
        val isLoading: Boolean,
        val isError: Boolean,
        val data: UserProfile,
    )

    data class ListUiState(
        val isLoading: Boolean,
        val isError: Boolean,
        val data: List<Any>,
    )

    sealed interface Event {
        data object NetworkErrorEvent : Event
        data object NavigateToLogin : Event
        data object NavigateToProfile : Event
        data object NavigateToStore : Event
        data object NavigateToMileage : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MyPageViewModel(
                    DeamHomeApplication.container.productRepository,
                ) as T
            }
        }
    }
}
