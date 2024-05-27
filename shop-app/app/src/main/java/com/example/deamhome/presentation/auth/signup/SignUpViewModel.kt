package com.example.deamhome.presentation.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.SignUpInfo
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.domain.repository.MailRepository
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val mailRepository: MailRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val id: MutableStateFlow<String> = MutableStateFlow("")
    val pw: MutableStateFlow<String> = MutableStateFlow("")
    val pwCheck: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> = MutableStateFlow("")
    val emailConfirm: MutableStateFlow<String> = MutableStateFlow("")
    val name: MutableStateFlow<String> = MutableStateFlow("")
    val phone: MutableStateFlow<String> = MutableStateFlow("")
    val genderMale: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val genderFemale: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _isTimer: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTimer: StateFlow<Boolean>
        get() = _isTimer

    fun mailSend(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val email = email.value.trim()
        viewModelScope.launch {
            if(email.isBlank()) {
                _event.emit(Event.MailErrorEvent("입력된 메일이 없습니다."))
                _isLoading.value = false
                return@launch
            }
            when(val response = mailRepository.mailSend(email)){
                is ApiResponse.Success -> {
                    _event.emit(Event.MailSendSuccess)
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.MailErrorEvent(response.error.toString()))
                }

                else -> {
                    _event.emit(Event.MailErrorEvent("알 수 없는 에러입니다."))
                }
            }
            _isLoading.value = false
        }
    }

    fun mailConfirm(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val email = email.value.trim()
        val authCode = emailConfirm.value.trim()
        viewModelScope.launch{
            if(authCode.isBlank()){
                _event.emit(Event.MailErrorEvent("메일 인증을 하지 못했습니다."))
                _isLoading.value = false
                return@launch
            }
            when(val response = mailRepository.mailConfirm(authCode,email)){
                is ApiResponse.Success -> {
                    _event.emit(Event.MailConfirmSuccess)
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.MailErrorEvent(response.error.toString()))
                }

                else -> {
                    _event.emit(Event.MailErrorEvent("알 수 없는 에러입니다."))
                }
            }
        }
    }

    fun signUp(){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val id = id.value.trim()
        val pwd = pw.value.trim()
        val pwdCheck = pwCheck.value.trim()
        val userName = name.value.trim()
        val phone = phone.value.trim()
        val email = email.value.trim()
        viewModelScope.launch {
            if(id.isBlank()){
                _event.emit(Event.SignUpErrorEvent(ErrorType.ID, "아이디를 ${ERROR_MSG}"))
                _isLoading.value = false
                return@launch
            }
            else if(pwd.isBlank()){
                _event.emit(Event.SignUpErrorEvent(ErrorType.PWD,"비밀번호를 ${ERROR_MSG}"))
                _isLoading.value = false
                return@launch
            }
            else if(userName.isBlank()){
                _event.emit(Event.SignUpErrorEvent(ErrorType.NAME,"이름을 ${ERROR_MSG}"))
                _isLoading.value = false
                return@launch
            }
            else if(phone.isBlank()){
                _event.emit(Event.SignUpErrorEvent(ErrorType.PHONE,"전화번호를 ${ERROR_MSG}"))
                _isLoading.value = false
                return@launch
            }
            else if(email.isBlank()){
                _event.emit(Event.SignUpErrorEvent(ErrorType.EMAIL,"이메일을 ${ERROR_MSG}"))
                _isLoading.value = false
                return@launch
            }

            when(val response = authRepository.signUp(id, pwd, pwdCheck, userName, phone, email)){
                is ApiResponse.Success -> {
                    _event.emit(Event.SignUpSuccessEvent)
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.SignUpErrorEvent(ErrorType.ANY, response.error.toString()))
                }

                else -> {
                    _event.emit(Event.SignUpErrorEvent(ErrorType.ANY, "알 수 없는 에러 입니다."))
                }
            }

            _isLoading.value = false
        }
    }

    sealed interface Event {
        data class MailErrorEvent(val message: String): Event
        data object MailSendSuccess: Event
        data object MailConfirmSuccess: Event
        data object NetworkErrorEvent : Event
        data class SignUpErrorEvent(val type: ErrorType, val message: String="") : Event
        data object SignUpSuccessEvent: Event
    }

    companion object {

        private val ERROR_MSG = "입력 하지 않았습니다."

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return SignUpViewModel(
                    DeamHomeApplication.container.mailRepository,
                    DeamHomeApplication.container.authRepository
                ) as T
            }
        }
    }
}
