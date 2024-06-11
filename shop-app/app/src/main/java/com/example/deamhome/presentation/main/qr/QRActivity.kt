package com.example.deamhome.presentation.main.qr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.databinding.ActivityQractivityBinding
import com.example.deamhome.presentation.main.store.modify.ModifyActivity
import com.example.deamhome.presentation.main.store.modify.SelectType

class QRActivity : BindingActivity<ActivityQractivityBinding>(R.layout.activity_qractivity) {
    private val viewModel: QRViewModel by viewModels { QRViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        try {
            val extStoreId = intent.getStringExtra(USER_STORE)
            if (extStoreId != null){
                viewModel.qr(extStoreId)
            };
        } catch (e: Exception) {
            Log.d(QR_TAG, e.toString())
        }
    }

    companion object{
        private val USER_STORE = "user_store"
        private val QR_TAG = "qr_tag"
        fun getIntent(context: Context, extStoreId: String): Intent {
            return Intent(context, QRActivity::class.java).apply {
                putExtra(USER_STORE, extStoreId)
            }
        }
    }
}