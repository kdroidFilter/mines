package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import de.stefan_oltmann.mines.ui.theme.colorExplosion
import io.github.alexzhirkevich.compottie.*
import mines.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExplosionLottie() {

    val composition by rememberLottieComposition {

        LottieCompositionSpec.DotLottie(
            archive = Res.readBytes("files/explosion.lottie")
        )
    }

    val painter = rememberLottiePainter(
        composition = composition,
        speed = 0.5f
    )

    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            colorExplosion
        ), modifier = Modifier
    )
}
