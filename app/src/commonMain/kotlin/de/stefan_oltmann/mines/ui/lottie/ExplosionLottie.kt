package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun ExplosionLottie(
    composition: LottieComposition,
    animationWidth: Dp
) {

    val painter = rememberLottiePainter(
        composition = composition,
        speed = 1.5f
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.width(animationWidth)
    )
}
