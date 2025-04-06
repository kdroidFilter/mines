package de.stefan_oltmann.mines.ui.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import mines.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ConfettiLottie() {

    val composition by rememberLottieComposition {

        LottieCompositionSpec.DotLottie(
            archive = Res.readBytes("files/confetti.lottie")
        )
    }

    val painter = rememberLottiePainter(
        composition = composition,
        speed = 1.3f
    )

    Image(
        painter = painter,
        contentDescription = null
    )
}
