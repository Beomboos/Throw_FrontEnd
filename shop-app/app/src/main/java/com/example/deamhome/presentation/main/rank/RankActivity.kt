package com.example.deamhome.presentation.main.rank

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingActivity
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.databinding.ActivityRankBinding
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.presentation.main.mypage.profile.ProfileActivity

class RankActivity : BindingActivity<ActivityRankBinding>(R.layout.activity_rank) {
    private val viewModel: RankViewModel by viewModels { RankViewModel.Factory };
    private var rankAdapter: RankAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        try {
            val user: UserProfile? = intent.getParcelableExtra(USER_PROFILE)
            if(user!=null) viewModel.userInfo(user);
        }catch (e: Exception){
            Log.d(RANK_TAG,e.toString())
        }

        setObserve()
    }

    private fun setObserve(){
        viewModel.event.observe(this){ handleEvent(it) }
        viewModel.uiState.observe(this){ updateUi(it) }
        viewModel.uiStore.observe(this){ updateStore(it) }
    }

    private fun handleEvent(event: RankViewModel.Event){
        when(event){
            is RankViewModel.Event.Failed -> {
                Toaster.showShort(this@RankActivity, event.msg);
            }
            else -> {
                finish();
            }
        }
    }

    private fun updateUi(items: List<LeaderboardResponse>){
        var itemList = binding.rcRank

        rankAdapter = RankAdapter(items = items, onClick = viewModel::rankStore)
        rankAdapter?.notifyDataSetChanged()

        itemList.adapter = rankAdapter
        itemList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun updateStore(items: List<RankerStore>){
        val expandedPosition = rankAdapter?.expandedPosition
        if (expandedPosition != -1 && expandedPosition!=null) {
            rankAdapter?.updateRankerStores(expandedPosition, items)
        }
    }

    companion object{
        private val USER_PROFILE = "USER_PROFILE"
        private val RANK_TAG = "rank_tag"
        fun getIntent(context: Context, user: UserProfile): Intent{
            return Intent(context, RankActivity::class.java).apply {
                putExtra(USER_PROFILE, user)
            }
        }
    }
}