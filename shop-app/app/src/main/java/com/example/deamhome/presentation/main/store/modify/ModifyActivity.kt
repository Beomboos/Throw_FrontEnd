package com.example.deamhome.presentation.main.store.modify

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable.Factory
import android.util.Log
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.databinding.ActivityModifyBinding
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.presentation.main.mypage.profile.ProfileActivity
import com.example.deamhome.presentation.main.store.home.StoreActivity

class ModifyActivity : BindingActivity<ActivityModifyBinding>(R.layout.activity_modify) {
    private val viewModel: ModifyViewModel by viewModels { ModifyViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.event.observe(this){ handleEvnet(it) }
        setUpUi()
    }

    fun setUpUi(){
        viewModel.store.observe(this){ viewModel.uiUpdate() }
    }

    fun handleEvnet(event: ModifyViewModel.Event){
        when(event){
            ModifyViewModel.Event.DeleteSuccess -> {

            }
            is ModifyViewModel.Event.Failed -> {

            }
            ModifyViewModel.Event.ModifySuccess -> {}
            ModifyViewModel.Event.NavigateToAddress -> {}
            is ModifyViewModel.Event.Select -> {
                try {
                    val store: StoreResponse? = intent.getParcelableExtra(USER_STORE)
                    if(store!=null) viewModel.storeUpdate(store);
                }catch (e: Exception){
                    Log.d(STORE_TAG,e.toString())
                }
            }
        }
    }

    companion object{

        private const val USER_STORE = "user_store"
        private const val STORE_TAG = "store_tag"
        private const val REQUEST_CODE: Int = 1
        fun getIntent(context: Context, store: StoreResponse): Intent {
            return Intent(context, ModifyActivity::class.java).apply {
                putExtra(USER_STORE, store)
            }
        }
    }
}