package com.example.deamhome.presentation.main.store.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivityRegisterBinding

class RegisterActivity : BindingActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.event.observe(this){ handleEvnet(it) }

        setupUi()
    }

    fun setupUi(){
        viewModel.crnBtnUiState.observe(this){
            binding.crnBtn.text = it.text
            binding.crnBtn.isEnabled = it.btn
        }
    }

    fun handleEvnet(evnet: RegisterViewModel.Event){
        when(evnet){
            RegisterViewModel.Event.RegisterSuccess -> {

            }

            is RegisterViewModel.Event.RegisterFailed -> {

            }

            is RegisterViewModel.Event.CrnFailed -> {
                binding.crnBtn.text = ""
                Toaster.showShort(applicationContext, evnet.message)
            }
        }
    }

    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}