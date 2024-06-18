package com.example.deamhome.presentation.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

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
        viewModelScope.launch {
            _event.emit(Event.NavigateToProfile(_profileUiState.value.data))
        }
    }

    //가게관리 버튼이벤트
    fun store(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToStore)
        }
    }

    //마일리지/상품 구매 버튼이벤트
    fun mileage(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToMileage(_profileUiState.value.data))
        }
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

    fun logout(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when(val response = productRepository.logout()){
                is ApiResponse.Success -> {
                    _event.emit(Event.NavigateToLogin)
                    authRepository.removeToken()
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.FailedMessage(response.error?.firstOrNull().toString()))
                }

                is ApiResponse.Unexpected -> {
                    _event.emit(Event.NavigateToLogin)
                }

                else -> {
                    _event.emit(Event.FailedMessage("알 수 없는 에러가 발생했습니다."))
                }
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
        data class NavigateToProfile(val user: UserProfile) : Event
        data object NavigateToStore : Event
        data class NavigateToMileage(val user: UserProfile) : Event
        data class FailedMessage(val message: String) : Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MyPageViewModel(
                    DeamHomeApplication.container.authRepository,
                    DeamHomeApplication.container.productRepository,
                ) as T
            }
        }
    }
}
