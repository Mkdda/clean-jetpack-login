package com.maylbus.collectives.cleanarchitecturelogin.snackbar

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maylbus.collectives.cleanarchitecturelogin.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SnackBarViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val snackBarHostState: SnackbarHostState = SnackbarHostState()

    private val _isSnackBarDisplayed: MutableStateFlow<Boolean> by lazy {

        MutableStateFlow(false)
    }

    val isSnackBarDisplayed: StateFlow<Boolean>
        get() = this._isSnackBarDisplayed

    fun onSnackBarShow(
        textContent: String,
        labelText: String = "",
        onLabelActionClick: (() -> Unit?)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {

        this.viewModelScope.launch(this.dispatcher) {

            snackBarHostState.showSnackbar(
                message = textContent,
                actionLabel = labelText,
                duration = duration
            ).let { result: SnackbarResult ->

                if (result == SnackbarResult.ActionPerformed) {

                    onLabelActionClick?.invoke()
                }
            }

            _isSnackBarDisplayed.value = false
        }

        _isSnackBarDisplayed.value = true
    }
}