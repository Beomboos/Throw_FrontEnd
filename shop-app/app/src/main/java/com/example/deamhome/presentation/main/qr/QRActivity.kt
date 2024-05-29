package com.example.deamhome.presentation.main.qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityQractivityBinding
import com.example.deamhome.presentation.auth.login.LoginActivity

class QRActivity : BindingActivity<ActivityQractivityBinding>(R.layout.activity_qractivity) {
    private val viewModel: QRViewModel by viewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                //카메라 결과 저장이라는 데 몰루?
            }
        }

        val cameraPermissionCheck = ContextCompat.checkSelfPermission(
            this@QRActivity,
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

    private fun handleEvent(event: QRViewModel.Event){
        openCamera()
    }
    private fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(cameraIntent.resolveActivity(packageManager) != null){
            cameraLauncher.launch(cameraIntent)
        }
    }

    companion object{
        private val MOVE_MAIN_AFTER_QR = "move_main_after_qr"
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, QRActivity::class.java).apply {
                putExtra(MOVE_MAIN_AFTER_QR, moveToMain)
            }
        }
    }
}