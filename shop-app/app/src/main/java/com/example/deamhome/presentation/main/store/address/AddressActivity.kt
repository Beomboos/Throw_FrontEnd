package com.example.deamhome.presentation.main.store.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.data.model.response.address.DocumentResponse
import com.example.deamhome.databinding.ActivityAddressBinding

class AddressActivity : BindingActivity<ActivityAddressBinding>(R.layout.activity_address) {
    private val viewModel: AddressViewModel by viewModels{ AddressViewModel.Factory };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupObserve();
    }

    fun setupObserve(){
        viewModel.event.observe(this){ handleEvent(it) }
        viewModel.addressUIState.observe(this){ setupUI(it) }
    }

    fun setupUI(addressList: List<DocumentResponse>){
        val itemList = binding.addressList
        val adapter = AddressAdapter(addressList, viewModel::selectAddress)
        adapter.notifyDataSetChanged()

        itemList.adapter = adapter
        itemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun handleEvent(event: AddressViewModel.Event){
        when(event){
            is AddressViewModel.Event.AddressSuccess -> {
                select(event.address)
            }
            is AddressViewModel.Event.AddressFailed -> {
                Toaster.showShort(applicationContext, event.message)
            }
        }
    }

    fun select(item: DocumentResponse){
        val intent = Intent()
        intent.putExtra("data", item);
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, AddressActivity::class.java)
        }
    }
}