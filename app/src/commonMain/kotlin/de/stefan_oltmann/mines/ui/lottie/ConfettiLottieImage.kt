package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun ConfettiLottieImage(
    confettiLottieComposition: LottieComposition
) {

    val painter = rememberLottiePainter(
        composition = confettiLottieComposition,
        speed = 1.3f
    )

    Image(
        painter = painter,
        contentDescription = null
    )
}
