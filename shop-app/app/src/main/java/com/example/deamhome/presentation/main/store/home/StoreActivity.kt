package com.example.deamhome.presentation.main.store.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.address.DocumentResponse
import com.example.deamhome.databinding.ActivityStoreBinding
import com.example.deamhome.presentation.main.store.register.RegisterActivity

class StoreActivity : BindingActivity<ActivityStoreBinding>(R.layout.activity_store) {
    private val viewModel: StoreViewModel by viewModels { StoreViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.event.observe(this){ handleEvnet(it) }
        setupObserve()
    }

    fun setupObserve(){
        viewModel.myStoreResponse.observe(this){ updateUi(it) }
    }

    fun updateUi(stores: List<StoreResponse>){
        val itemList = binding.storeList;

        val adapter = StoreAdapter(stores, viewModel::modifyService)
        adapter.notifyDataSetChanged()

        itemList.adapter = adapter
        itemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun handleEvnet(event: StoreViewModel.Event){
        when(event){
            StoreViewModel.Event.NavigateToRegister -> {
                startActivityForResult(RegisterActivity.getIntent(this@StoreActivity), REQUEST_CODE)
            }

            is StoreViewModel.Event.NavigateToModify -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val receiveData = data?.getStringExtra("data")
            if (receiveData != null) {
                viewModel.stores()
            }
        }
    }

    companion object{
        private const val MOVE_PROFILE_AFTER_STORE = "move_profile_after_store_tag"
        private const val REQUEST_CODE: Int = 1
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, StoreActivity::class.java).apply {
                putExtra(MOVE_PROFILE_AFTER_STORE, moveToMain)
            }
        }
    }
}