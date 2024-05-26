package com.example.deamhome.presentation.main.store.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.data.model.response.address.DocumentResponse
import com.example.deamhome.databinding.ActivityRegisterBinding
import com.example.deamhome.presentation.main.store.address.AddressActivity

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

            RegisterViewModel.Event.NavigateToAddress -> {
                startActivityForResult(AddressActivity.getIntent(this@RegisterActivity),REQUEST_CODE)
            }
        }
    }
    //AddressActivity에서 전달받는 값을 처리해준다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val receiveData = data?.getParcelableExtra<DocumentResponse>("data")
            if (receiveData != null) {
                binding.tiAddress.setText(receiveData.address_name.toString())
                binding.etZip.setText(receiveData.road_address?.zone_no.toString())
                viewModel.lat = receiveData.x.toString()
                viewModel.lon = receiveData.y.toString()
            }
        }
    }

    companion object{
        private const val REQUEST_CODE: Int = 1
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}