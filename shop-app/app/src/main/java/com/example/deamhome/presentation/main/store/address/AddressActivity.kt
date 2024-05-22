package com.example.deamhome.presentation.main.store.address

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.response.DocumentResponse
import com.example.deamhome.databinding.ActivityAddressBinding
import com.example.deamhome.presentation.main.store.register.RegisterActivity

class AddressActivity : BindingActivity<ActivityAddressBinding>(R.layout.activity_address) {
    private val viewModel: AddressViewModel by viewModels{ AddressViewModel.Factory };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    fun setupObserve(){
        viewModel.event.observe(this){ handleEvent(it) }
        viewModel.addressUIState.observe(this){ setupUI(it) }
    }

    fun setupUI(addressList: List<DocumentResponse>){

    }

    fun handleEvent(event: AddressViewModel.Event){

    }

    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, AddressActivity::class.java)
        }
    }
}