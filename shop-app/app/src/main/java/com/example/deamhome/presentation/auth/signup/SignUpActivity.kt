package com.example.deamhome.presentation.auth.signup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.util.log
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivitySignUpBinding

class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.Factory };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setupObserve()
        binding.etvId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: return
                if (text.length == 20) {
                    binding.tilId.error = "최대 길이 도달"
                } else {
                    binding.tilId.error = null
                }
            }
        })


        binding.etvPwdCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: return
                if (binding.etvPwd.text.toString() != text && text.isNotBlank()) {
                    binding.tilPwdCheck.error = "비밀번호를 다시 한 번 확인해주세요"
                } else {
                    binding.tilPwdCheck.error = null
                }
            }
        })
    }

    private fun setupObserve() {
        viewModel.event.observe(this@SignUpActivity) { handleEvent(it) }
    }

    private fun handleEvent(event: SignUpViewModel.Event) {
        when (event) {
            SignUpViewModel.Event.NetworkErrorEvent -> {
                Toaster.showShort(this@SignUpActivity, R.string.all_network_check_please_message)
            }

            is SignUpViewModel.Event.MailErrorEvent -> {
                Toaster.showShort(this@SignUpActivity, event.message)
                if(event.message.contains("인증"))
                    binding.tilEmailCheck.error = event.message
                else
                    binding.tilEmail.error = event.message
            }

            is SignUpViewModel.Event.SignUpErrorEvent -> {
                failed(event.type, event.message)
            }

            SignUpViewModel.Event.MailSendSuccess -> {
                Toaster.showShort(this@SignUpActivity, "메일을 전송했습니다.\n메일을 확인해주세요.")
            }

            SignUpViewModel.Event.MailConfirmSuccess -> {
                binding.btnEmailCheck.isEnabled = false
                binding.btnEmailCheck.text = "인증완료"
            }

            SignUpViewModel.Event.SignUpSuccessEvent -> {
                Toaster.showShort(this@SignUpActivity,"정상적으로 회원가입이 완료되었습니다.")
                finish()
            }
        }
    }

    private fun failed(type: ErrorType, message: String){
        when(type){
            ErrorType.ID -> {
                binding.tilId.error = message
            }
            ErrorType.PWD -> {
                binding.tilPwd.error = message
            }
            ErrorType.NAME -> {
                binding.tilName.error = message
            }
            ErrorType.PHONE -> {
                binding.tilBirthday.error = message
            }
            ErrorType.EMAIL -> {
                binding.tilEmail.error = message
            }
            ErrorType.ANY -> {
                Toaster.showShort(this@SignUpActivity, message)
            }
        }
    }
}
