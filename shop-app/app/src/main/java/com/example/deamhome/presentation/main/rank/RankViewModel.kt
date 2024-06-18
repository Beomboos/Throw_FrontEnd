package com.example.deamhome.presentation.main.rank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.deamhome.app.DeamHomeApplication
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.RankRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.DomainCombiner

class RankViewModel(
    private val rankRepository: RankRepository,
) : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow();
    val event: SharedFlow<Event>
        get() = _event;

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _uiState: MutableStateFlow<List<LeaderboardResponse>> =
        MutableStateFlow(emptyList())
    val uiState: StateFlow<List<LeaderboardResponse>>
        get() = _uiState

    private val _uiStore: MutableStateFlow<List<RankerStore>> = MutableStateFlow(emptyList())
    val uiStore: StateFlow<List<RankerStore>>
        get() = _uiStore

    val mileage: MutableStateFlow<String> = MutableStateFlow("0M")
    val ranking: MutableStateFlow<String> = MutableStateFlow("0위");

    init {
        leaderboader();
    }

    fun userInfo(user: UserProfile){
        viewModelScope.launch {
            mileage.value = user.mileage.toString()+"M";
        }
    }

    fun leaderboader() {
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when (val response = rankRepository.leaderboard()) {
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        _uiState.update { data }
                    }
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.Failed("조회 실패"));
                }

                else -> {
                    _event.emit(Event.Failed("조회 실패"));
                }
            }

            when(val response = rankRepository.ranking()){
                is ApiResponse.Success -> {
                    ranking.value = response.body.ranking.toString()+"위";
                }
                is ApiResponse.Failure -> {
                    _event.emit(Event.Failed("조회 실패"));
                }
                else -> {
                    _event.emit(Event.Failed("조회 실패"));
                }
            }

            _isLoading.value = false
        }
    }

    fun rankStore(inputId: String){
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        viewModelScope.launch {
            when(val response = rankRepository.rankerStore(inputId)){
                is ApiResponse.Success -> {
                    response.body?.let { data ->
                        _uiStore.update { data }
                    }
                }

                is ApiResponse.Failure -> {
                    _event.emit(Event.Failed("조회 실패"));
                }

                else -> {
                    _event.emit(Event.Failed("조회 실패"));
                }
            }
        }

        _isLoading.value = false
    }

    fun backBtn() {
        viewModelScope.launch {
            _event.emit(Event.Back);
        }
    }


    sealed interface Event {
        data class Failed(val msg: String): Event
        data object Back: Event
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return RankViewModel(
                    DeamHomeApplication.container.rankRepository
                ) as T
            }
        }
    }
}