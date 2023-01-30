package com.maylbus.collectives.cleanarchitecturelogin.uimodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UITextModel {

    data class DynamicString(val text: String): UITextModel()

    data class StringResource(val resId: Int): UITextModel()

    @Composable
    internal fun stringValue(): String {

        return when (this) {

            is DynamicString -> this.text

            is StringResource -> stringResource(id = this.resId)
        }
    }
}