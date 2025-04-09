package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameOver
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mines.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExplosionLottie(
    animationWidth: Dp
) {

    val composition by rememberLottieComposition {

        LottieCompositionSpec.DotLottie(
            archive = Res.readBytes("files/explosion.lottie")
        )
    }

    val painter = rememberLottiePainter(
        composition = composition,
        speed = 1.5f
    )

    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(colorCardBorderGameOver),
        modifier = Modifier.width(animationWidth)
    )
}
