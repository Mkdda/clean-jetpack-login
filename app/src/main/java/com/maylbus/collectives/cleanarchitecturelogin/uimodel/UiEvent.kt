package com.maylbus.collectives.cleanarchitecturelogin.uimodel

internal sealed class UiEvent {

    data class Navigate(val route: String) : UiEvent()

    data class ShowSnackBar(val message: UITextModel): UiEvent()
}