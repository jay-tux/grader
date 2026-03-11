package com.jaytux.grader.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SnackVM : ViewModel() {
    private val _snacks = Channel<String>()
    val snacks = _snacks.receiveAsFlow()

    fun show(msg: String): Unit {
        viewModelScope.launch {
            _snacks.send(msg)
        }
    }

    @Composable
    fun Launcher(state: SnackbarHostState) {
        LaunchedEffect(Unit) {
            snacks.collect { snack -> state.showSnackbar(snack) }
        }
    }
}