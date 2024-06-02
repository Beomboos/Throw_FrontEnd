package com.example.deamhome.presentation.main.qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivityScanBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class ScanActivity : BindingActivity<ActivityScanBinding>(R.layout.activity_scan) {
    private val viewModel: ScanViewModel by viewModels { ScanViewModel.Factory }

    private val cameraLauncher = registerForActivityResult(
        ScanContract()
    ){
            result: ScanIntentResult ->
        if(result.contents == null){
            Toaster.showShort(this@ScanActivity, "값을 받지 못했습니다.")
        }
        else{
            Toaster.showShort(this@ScanActivity, "적립 시작");
            viewModel.scan(result.contents)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            this@ScanActivity,
            android.Manifest.permission.CAMERA
        )
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                1000
            )
        }

        viewModel.event.observe(this){handleEvent(it)}
    }

    private fun handleEvent(event: ScanViewModel.Event){
        when(event){
            is ScanViewModel.Event.MileageUpdate -> Toaster.showShort(this@ScanActivity, event.message);
            ScanViewModel.Event.NavigateToCamera ->
                openCamera()
        }
    }

    private fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(cameraIntent.resolveActivity(packageManager) != null){
            val options = ScanOptions()
            options.setOrientationLocked(false)
            cameraLauncher.launch(options)
        }
    }

    companion object{
        private val MOVE_MAIN_AFTER_QR = "move_main_after_qr"
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, ScanActivity::class.java).apply {
                putExtra(MOVE_MAIN_AFTER_QR, moveToMain)
            }
        }
    }
}