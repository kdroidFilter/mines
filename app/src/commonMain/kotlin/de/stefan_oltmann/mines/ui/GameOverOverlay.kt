package de.stefan_oltmann.mines.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import de.stefan_oltmann.mines.ui.lottie.ExplosionLottieImage
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameOver
import io.github.alexzhirkevich.compottie.LottieComposition

@Composable
fun GameOverOverlay(
    explosionLottieComposition: LottieComposition,
    fontFamily: FontFamily
) {

    Box(
        contentAlignment = Alignment.Center
    ) {

        ExplosionLottieImage(
            explosionLottieComposition = explosionLottieComposition,
        )

        DelayedGameOverText(
            text = "game over",
            color = colorCardBorderGameOver,
            fontFamily = fontFamily
        )
    }
}
