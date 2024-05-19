package com.example.deamhome.presentation.main.mypage.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.util.log
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.ProductRepository
import com.example.deamhome.presentation.main.mypage.MyPageViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val productRepository: ProductRepository
):ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _userProfile: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile.EMPTY)

    val name: MutableStateFlow<String> = MutableStateFlow("")
    val phone: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            name.collect {
                log(tag = "profile", message = "name: ${name.value}")
            }
        }

        viewModelScope.launch {
            phone.collect {
                log(tag = "profile", message = "phone: ${phone.value}")
            }
        }

        viewModelScope.launch {
            email.collect {
                log(tag = "profile", message = "email: ${email.value}")
            }
        }
    }
    fun userInfo(user: UserProfile){
        viewModelScope.launch {
            _userProfile.update {
                it.copy(
                    inputId = user.inputId,
                    role = user.role,
                    userName = user.userName,
                    userPhoneNumber = user.userPhoneNumber,
                    email = user.email,
                    mileage = user.mileage,
                )
            }
            name.value = _userProfile.value.userName
            phone.value = _userProfile.value.userPhoneNumber
            email.value = _userProfile.value.email
        }
    }

    fun userModify(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val name = name.value.trim()
        val phone = phone.value.trim()
        val email = email.value.trim()
        val user = _userProfile.value
        viewModelScope.launch {
            if(name.isBlank() || phone.isBlank() || email.isBlank()){
                _event.emit(Event.ModifyFailed("빈칸이 있어 수정 할 수 없습니다."))
                _isLoading.value = false
                return@launch
            }
            if(name.equals(user.userName) && phone.equals(user.userPhoneNumber) && email.equals(user.email)){
                _event.emit(Event.ModifyFailed("유저 정보가 같아 수정 할 수 없습니다."))
                _isLoading.value = false
                return@launch
            }

            when(val response = productRepository.modify(name, phone, email)){
                is ApiResponse.Success -> {
                    _event.emit(Event.ModifySuccess("회원 정보 변경 완료"))
                }

                is ApiResponse.Failure -> {
                    _event.emit(
                        Event.ModifyFailed(
                            response.error?.firstOrNull().toString()
                        )
                    )
                }

                else -> {
                    _event.emit(
                        Event.ModifyFailed("유저 정보를 수정 할 수 없습니다.")
                    )
                }
            }
            _isLoading.value = false
        }
    }

    fun backBtn(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToProfile)
        }
    }

    sealed interface Event{
        data class ModifySuccess(val message: String): Event
        data class ModifyFailed(val message: String): Event
        data object NavigateToProfile : Event
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return ProfileViewModel(
                    DeamHomeApplication.container.productRepository,
                ) as T
            }
        }
    }
}