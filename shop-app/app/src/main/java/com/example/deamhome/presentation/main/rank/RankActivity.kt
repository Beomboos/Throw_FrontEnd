package com.example.deamhome.presentation.main.rank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.databinding.ActivityRankBinding

class RankActivity : BindingActivity<ActivityRankBinding>(R.layout.activity_rank) {
    private val viewModel: RankViewModel by viewModels();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setObserve()
    }

    private fun setObserve(){
        viewModel.event.observe(this){ handleEvent(it) }
    }

    private fun handleEvent(event: RankViewModel.Event){

    }
}