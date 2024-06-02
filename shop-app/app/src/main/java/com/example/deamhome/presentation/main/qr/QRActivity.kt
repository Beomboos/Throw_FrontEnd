package com.example.deamhome.presentation.main.qr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityQractivityBinding

class QRActivity : BindingActivity<ActivityQractivityBinding>(R.layout.activity_qractivity) {
    private val viewModel: QRViewModel by viewModels { QRViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    companion object{
        private val MOVE_Store_AFTER_QR = "move_store_after_qr"
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, QRActivity::class.java).apply {
                putExtra(MOVE_Store_AFTER_QR, moveToMain)
            }
        }
    }
}