package com.example.deamhome.presentation.main.store.modify

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable.Factory
import android.util.Log
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.address.DocumentResponse
import com.example.deamhome.databinding.ActivityModifyBinding
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.presentation.main.mypage.profile.ProfileActivity
import com.example.deamhome.presentation.main.store.address.AddressActivity
import com.example.deamhome.presentation.main.store.home.StoreActivity
import com.example.deamhome.presentation.main.store.register.RegisterActivity

class ModifyActivity : BindingActivity<ActivityModifyBinding>(R.layout.activity_modify) {
    private val viewModel: ModifyViewModel by viewModels { ModifyViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.event.observe(this) { handleEvnet(it) }
        setUpUi()
    }

    fun setUpUi() {
        viewModel.store.observe(this) { viewModel.uiUpdate() }
    }

    fun handleEvnet(event: ModifyViewModel.Event) {
        when (event) {
            ModifyViewModel.Event.DeleteSuccess -> {
                success("해당 가게를 삭제했습니다.")
            }

            is ModifyViewModel.Event.Failed -> {
                Toaster.showShort(applicationContext, event.message);
            }

            ModifyViewModel.Event.ModifySuccess -> {
                success("해당 가게가 수정되었습니다.")
            }
            ModifyViewModel.Event.NavigateToAddress -> {
                startActivityForResult(
                    AddressActivity.getIntent(this@ModifyActivity), REQUEST_CODE
                )
            }

            is ModifyViewModel.Event.Select -> {
                try {
                    val store: StoreResponse? = intent.getParcelableExtra(USER_STORE)
                    if (store != null){
                        viewModel.storeUpdate(store, event.type)
                        if(event.type==SelectType.DELETE){
                            viewModel.delete()
                        }
                    };
                } catch (e: Exception) {
                    Log.d(STORE_TAG, e.toString())
                }
            }
        }
    }

    fun success(message: String){
        Toaster.showShort(applicationContext, message)
        val intent = Intent()
        intent.putExtra("data", MODIFY_SUCCESS)
        setResult(RESULT_OK, intent)
        finish()
    }

    //AddressActivity에서 전달받는 값을 처리해준다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val receiveData = data?.getParcelableExtra<DocumentResponse>("data")
            if (receiveData != null) {
                binding.tiAddress.setText(receiveData.address_name.toString())
                binding.etZip.setText(receiveData.road_address?.zone_no.toString())
                viewModel.lat = receiveData.y.toString()
                viewModel.lon = receiveData.x.toString()
            }
        }
    }

    companion object {

        private const val USER_STORE = "user_store"
        private const val MODIFY_SUCCESS = "modify_success"
        private const val STORE_TAG = "store_tag"
        private const val REQUEST_CODE: Int = 1
        fun getIntent(context: Context, store: StoreResponse): Intent {
            return Intent(context, ModifyActivity::class.java).apply {
                putExtra(USER_STORE, store)
            }
        }
    }
}