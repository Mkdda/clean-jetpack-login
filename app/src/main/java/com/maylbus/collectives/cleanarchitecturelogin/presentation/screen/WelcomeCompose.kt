package com.maylbus.collectives.cleanarchitecturelogin.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.*
import com.maylbus.collectives.cleanarchitecturelogin.R

@Composable
internal fun WelcomeCompose(backgroundColor: Color) {

    val compositionResult: LottieCompositionResult = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.chat)
    )

    val progress by animateLottieCompositionAsState(
        composition = compositionResult.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {


        LottieAnimation(
            composition = compositionResult.value,
            progress = progress
        )
    }
}