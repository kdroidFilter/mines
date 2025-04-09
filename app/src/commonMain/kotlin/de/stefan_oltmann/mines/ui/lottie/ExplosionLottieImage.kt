package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameOver
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun ExplosionLottieImage(
    explosionLottieComposition: LottieComposition
) {

    val painter = rememberLottiePainter(
        composition = explosionLottieComposition,
        speed = 1.5f
    )

    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(colorCardBorderGameOver)
    )
}
