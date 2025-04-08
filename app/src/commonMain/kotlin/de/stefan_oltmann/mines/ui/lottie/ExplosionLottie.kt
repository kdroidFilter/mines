package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import de.stefan_oltmann.mines.ui.theme.colorExplosion
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun ExplosionLottie(
    composition: LottieComposition,
    animationWidth: Dp
) {

    val painter = rememberLottiePainter(
        composition = composition,
        speed = 0.5f
    )

    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(colorExplosion),
        modifier = Modifier.width(animationWidth)
    )
}
