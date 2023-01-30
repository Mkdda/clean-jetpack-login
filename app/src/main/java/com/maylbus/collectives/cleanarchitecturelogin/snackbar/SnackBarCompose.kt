package com.maylbus.collectives.cleanarchitecturelogin.snackbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal const val ID_SNACK_BAR_HOST_CONTAINER = "snack_bar_host_container"
internal const val ID_SNACK_BAR_CONTAINER = "snack_bar_container"
private val defaultPadding: Dp = 16.dp

@Composable
internal fun LoginSnackBar(
    viewModel: SnackBarViewModel
) {

    val isSnackBarDisplayed: Boolean by viewModel.isSnackBarDisplayed.collectAsState()

    LoginSnackBar(
        isSnackBarDisplayed = isSnackBarDisplayed,
        hostState = viewModel.snackBarHostState
    )
}

@Composable
internal fun LoginSnackBar(
    isSnackBarDisplayed: Boolean,
    hostState: SnackbarHostState
) {

    if (isSnackBarDisplayed) {

        SnackbarHost(
            modifier = Modifier
                .testTag(ID_SNACK_BAR_HOST_CONTAINER)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom),
            hostState = hostState
        ) { data: SnackbarData ->

            Snackbar(
                modifier = Modifier
                    .testTag(ID_SNACK_BAR_CONTAINER)
                    .padding(defaultPadding),
                snackbarData = data,
            )
        }
    }
}