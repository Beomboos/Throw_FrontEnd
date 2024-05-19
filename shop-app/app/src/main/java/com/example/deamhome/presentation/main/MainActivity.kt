package com.example.deamhome.presentation.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.BackKeyHandler
import com.example.deamhome.databinding.ActivityMainBinding
import com.example.deamhome.presentation.main.cart.CartActivity
import com.example.deamhome.presentation.main.category.CategoryBottomSheetFragment
import com.example.deamhome.presentation.main.map.MapFragment
import com.example.deamhome.presentation.main.mypage.MyPageFragment
import com.example.deamhome.presentation.main.search.SearchActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val backKeyHandler = BackKeyHandler(this)

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.curScreen.value != ScreenType.ChangeScreenType.Home) { // 홈으로 이동시켜서 한 번 더 상품들을 노출시키기
                binding.bnvMain.selectedItemId = R.id.menu_item_home
                return
            }
            backKeyHandler.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        onBackPressedDispatcher.addCallback(this, callback)
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.curScreen.observe(this) { changeFragment(it) }

        viewModel.curPopScreenEvent.observe(this) {
            when (it) {
                ScreenType.PopScreenType.Search -> startActivity(SearchActivity.getIntent(this))
                ScreenType.PopScreenType.Category -> {
                    CategoryBottomSheetFragment.newInstance().show(supportFragmentManager, it.tag)
                }

                ScreenType.PopScreenType.Cart -> startActivity(CartActivity.getIntent(this))
            }
        }
    }

    private fun changeFragment(screenType: ScreenType.ChangeScreenType) {
        val fragment = supportFragmentManager.findFragmentByTag(screenType.tag)
        supportFragmentManager.commit {
            supportFragmentManager.fragments.forEach(::hide)
            if (fragment == null) {
                add(
                    binding.fcvMain.id,
                    when (screenType) {
                        ScreenType.ChangeScreenType.Home -> MapFragment.newInstance()
                        ScreenType.ChangeScreenType.MyPage -> MyPageFragment.newInstance()
                    },
                    screenType.tag,
                )
            } else {
                show(fragment)
            }
            setReorderingAllowed(true) // 전환 애니메이션 등 최적화
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
