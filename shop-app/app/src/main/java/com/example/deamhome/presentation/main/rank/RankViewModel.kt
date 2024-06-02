package com.example.deamhome.presentation.main.rank

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class RankViewModel: ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow();
    val event: SharedFlow<Event>
        get() = _event;

    fun backBtn(){

    }

    sealed interface Event{
    }

    companion object{

    }
}