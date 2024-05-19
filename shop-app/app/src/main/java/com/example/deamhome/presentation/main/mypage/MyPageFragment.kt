package com.example.deamhome.presentation.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.deamhome.R
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.common.base.BindingFragment
import com.example.deamhome.common.util.log
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.common.view.showSnackbar
import com.example.deamhome.databinding.FragmentMyPageBinding
import com.example.deamhome.presentation.auth.login.LoginActivity
import kotlinx.coroutines.launch

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

            MyPageViewModel.Event.NavigateToProfile -> {}

            MyPageViewModel.Event.NavigateToMileage -> {}

            MyPageViewModel.Event.NavigateToStore -> {}
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyPageFragment()
    }
}
