package com.example.deamhome.presentation.main.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class QRViewModel(): ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow();
    val event: SharedFlow<Event>
        get() = _event

    fun camera(){
        viewModelScope.launch {
            _event.emit(Event.NavigateToCamera);
        }
    }

    sealed interface Event{
        data object NavigateToCamera: Event
    }
}