package de.stefan_oltmann.mines.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import de.stefan_oltmann.mines.ui.theme.colorBackground
import de.stefan_oltmann.mines.ui.theme.defaultRoundedCornerShape
import de.stefan_oltmann.mines.ui.theme.doublePadding
import kotlinx.coroutines.delay

@Composable
fun DelayedGameOverlayText(
    text: String,
    color: Color,
    fontFamily: FontFamily
) {

    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        delay(1000)

        showText = true
    }

    AnimatedVisibility(
        visible = showText,
        enter = fadeIn(animationSpec = tween(durationMillis = 1500))
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    color = colorBackground,
                    shape = defaultRoundedCornerShape
                )
                .doublePadding()

        ) {

            Text(
                text = text,
                color = color,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
        }
    }
}
