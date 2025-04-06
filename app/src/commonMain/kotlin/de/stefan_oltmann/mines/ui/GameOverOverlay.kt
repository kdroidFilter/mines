package de.stefan_oltmann.mines.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import de.stefan_oltmann.mines.ui.lottie.ExplosionLottie
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameOver

@Composable
fun GameOverOverlay(
    fontFamily: FontFamily,
    animationWidth: Dp
) {

    ExplosionLottie(animationWidth)

    DelayedGameOverlayText(
        text = "game over",
        color = colorCardBorderGameOver,
        fontFamily = fontFamily
    )
}
