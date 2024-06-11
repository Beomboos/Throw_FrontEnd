package com.example.deamhome.presentation.main.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingFragment
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.databinding.FragmentMyPageBinding
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.presentation.auth.login.LoginActivity
import com.example.deamhome.presentation.main.mypage.profile.ProfileActivity
import com.example.deamhome.presentation.main.rank.RankActivity
import com.example.deamhome.presentation.main.store.home.StoreActivity

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels { MyPageViewModel.Factory};

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.profileUiState.observe(this) {
            uiState(it)
        }

        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun uiState(profile: MyPageViewModel.ProfileUiState){
        binding.profileName.text = profile.data.userName
        binding.profileEmail.text = profile.data.email
        binding.totalMileage.text = "${profile.data.mileage}M"
        binding.useMileage.text = "${profile.data.mileage}M"
    }

    private fun handleEvent(event: MyPageViewModel.Event) {
        when (event) {
            MyPageViewModel.Event.NavigateToLogin -> {
                startActivity(
                    LoginActivity.getIntent(requireContext()),
                )
                Toaster.showShort(requireContext(),"로그아웃 되었습니다.")
                requireActivity().finish()
            }

            is MyPageViewModel.Event.FailedMessage -> {
                Toaster.showShort(
                    requireContext(),
                    event.message,
                )
            }

            MyPageViewModel.Event.NetworkErrorEvent -> {}

            is MyPageViewModel.Event.NavigateToProfile -> {
                startActivityForResult(ProfileActivity.getIntent(requireContext(), event.user),REQUEST_CODE)
            }

            is MyPageViewModel.Event.NavigateToMileage -> {
                startActivityForResult(RankActivity.getIntent(requireContext(), event.user),REQUEST_CODE)
            }

            MyPageViewModel.Event.NavigateToStore -> {
                startActivity(
                    StoreActivity.getIntent(requireContext()),
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val receiveData = data?.getStringExtra("data")
            if (receiveData != null) {
                viewModel.inquiry()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}
