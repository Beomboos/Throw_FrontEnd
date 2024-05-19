package com.example.deamhome.presentation.main.mypage.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.ActivityProfileBinding
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.presentation.main.mypage.MyPageFragment
import com.example.deamhome.presentation.main.mypage.MyPageViewModel

class ProfileActivity : BindingActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        try {
            val user: UserProfile? = intent.getParcelableExtra(USER_PROFILE)
            if(user!=null) viewModel.userInfo(user);
        }catch (e: Exception){
            Log.d(PROFILE_TAG,e.toString())
        }

        viewModel.event.observe(this){ handleEvent(it)}
    }

    fun handleEvent(event: ProfileViewModel.Event){
        when(event){
            ProfileViewModel.Event.NavigateToProfile -> {
                finish()
            }

            is ProfileViewModel.Event.ModifySuccess -> {
                Toaster.showShort(
                    applicationContext,
                    event.message
                )
                MyPageViewModel(DeamHomeApplication.container.productRepository).inquiry()
                finish()
            }

            is ProfileViewModel.Event.ModifyFailed -> {
                Toaster.showShort(
                    applicationContext,
                    event.message,
                )
            }
        }
    }

    companion object{
        private val USER_PROFILE = "USER_PROFILE"
        private val PROFILE_TAG = "PROFILE_TAG"

        fun getIntent(context: Context, user: UserProfile): Intent {
            return Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_PROFILE, user)
            }
        }
    }
}