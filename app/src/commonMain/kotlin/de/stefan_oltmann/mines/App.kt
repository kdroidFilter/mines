/*
 * 💣 Mines 💣
 * Copyright (C) 2025 Stefan Oltmann
 * https://github.com/StefanOltmann/mines
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.stefan_oltmann.mines

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import de.stefan_oltmann.mines.model.GameConfig
import de.stefan_oltmann.mines.model.GameDifficulty
import de.stefan_oltmann.mines.model.GameState
import de.stefan_oltmann.mines.ui.AppFooter
import de.stefan_oltmann.mines.ui.GameOverOverlay
import de.stefan_oltmann.mines.ui.MinefieldCanvas
import de.stefan_oltmann.mines.ui.SettingsDialog
import de.stefan_oltmann.mines.ui.Toolbar
import de.stefan_oltmann.mines.ui.lottie.ConfettiLottieImage
import de.stefan_oltmann.mines.ui.theme.EconomicaFontFamily
import de.stefan_oltmann.mines.ui.theme.colorBackground
import de.stefan_oltmann.mines.ui.theme.colorCardBackground
import de.stefan_oltmann.mines.ui.theme.colorCardBorder
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameOver
import de.stefan_oltmann.mines.ui.theme.colorCardBorderGameWon
import de.stefan_oltmann.mines.ui.theme.defaultRoundedCornerShape
import de.stefan_oltmann.mines.ui.theme.doublePadding
import de.stefan_oltmann.mines.ui.theme.doubleSpacing
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import mines.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    val fontFamily = EconomicaFontFamily()

    val gameState = remember { GameState() }

    val gameConfig = remember {
        mutableStateOf(
            GameConfig(
                cellSize = settings["cellSize"] ?: DEFAULT_CELL_SIZE,
                mapWidth = settings["mapWidth"] ?: defaultMapWidth,
                mapHeight = settings["mapHeight"] ?: defaultMapHeight,
                difficulty = GameDifficulty.valueOf(settings["difficulty"] ?: GameDifficulty.EASY.name)
            )
        )
    }

    val redrawState = remember { mutableStateOf(0) }

    /*
     * Pre-load lotties to avoid delays in playback.
     */

    val confettiLottieComposition by rememberLottieComposition {

        LottieCompositionSpec.DotLottie(
            archive = Res.readBytes("files/confetti.lottie")
        )
    }

    val explosionLottieComposition by rememberLottieComposition {

        LottieCompositionSpec.DotLottie(
            archive = Res.readBytes("files/explosion.lottie")
        )
    }

    /*
     * Initially start a new game when opened.
     */
    LaunchedEffect(Unit) {
        gameState.restart(gameConfig.value)
    }

    LaunchedEffect(gameConfig.value) {

        val newGameConfig = gameConfig.value

        val oldMapWidth = settings["mines_map_width"] ?: defaultMapWidth
        val oldMapHeight = settings["mines_map_height"] ?: defaultMapHeight
        val oldDifficulty = GameDifficulty.valueOf(settings["mines_difficulty"] ?: GameDifficulty.EASY.name)

        /* Save new settings to config */
        settings["mines_cell_size"] = newGameConfig.cellSize
        settings["mines_map_width"] = newGameConfig.mapWidth
        settings["mines_map_height"] = newGameConfig.mapHeight
        settings["mines_difficulty"] = newGameConfig.difficulty.name

        val mapSettingsChanged =
            oldMapWidth != newGameConfig.mapWidth ||
                oldMapHeight != newGameConfig.mapHeight ||
                oldDifficulty != newGameConfig.difficulty

        /* Launch a new game every time the settings change something that influences the map */
        if (mapSettingsChanged)
            gameState.restart(gameConfig.value)

        // HACK
        redrawState.value += 1
    }

    val elapsedSeconds by gameState.elapsedSeconds.collectAsState()

    val showSettings = remember { mutableStateOf(false) }

    println("REDRAW")

    /*
     * Force redraw if state changes.
     *
     * FIXME This is a hack
     */
    redrawState.value

    Column {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .background(colorBackground)
        ) {

            val borderColor = when {
                gameState.gameOver -> colorCardBorderGameOver
                gameState.gameWon -> colorCardBorderGameWon
                else -> colorCardBorder
            }

            var cardSize by remember { mutableStateOf((IntSize.Zero)) }

            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = colorCardBackground
                ),
                shape = defaultRoundedCornerShape,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .doublePadding()
                    .onGloballyPositioned { coordinates ->
                        cardSize = coordinates.size
                    }
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(
                            start = doubleSpacing,
                            end = doubleSpacing,
                            bottom = doubleSpacing
                        )
                ) {

                    Toolbar(
                        highlightRestartButton = gameState.gameOver || gameState.gameWon,
                        elapsedSeconds = elapsedSeconds,
                        remainingFlagsCount = gameState.minefield?.getRemainingFlagsCount() ?: 0,
                        fontFamily = fontFamily,
                        showSettings = {
                            showSettings.value = true
                        },
                        restartGame = {

                            gameState.restart(gameConfig.value)

                            // HACK
                            redrawState.value += 1
                        }
                    )

                    Box {

                        val verticalScrollState = rememberScrollState()
                        val horizontalScrollState = rememberScrollState()

                        Box(
                            modifier = Modifier
                                .verticalScroll(verticalScrollState)
                                .horizontalScroll(horizontalScrollState)
                        ) {

                            val minefield = gameState.minefield

                            if (minefield != null) {

                                MinefieldCanvas(
                                    minefield,
                                    gameConfig,
                                    redrawState,
                                    fontFamily,
                                    hit = { x, y -> gameState.hit(x, y) },
                                    flag = { x, y -> gameState.flag(x, y) }
                                )
                            }
                        }

                        if (verticalScrollState.canScrollForward || verticalScrollState.canScrollBackward)
                            VerticalScrollbar(verticalScrollState)

                        if (horizontalScrollState.canScrollForward || horizontalScrollState.canScrollBackward)
                            HorizontalScrollbar(horizontalScrollState)
                    }
                }
            }

            when {

                gameState.gameWon ->
                    confettiLottieComposition?.let { lottieComposition ->
                        ConfettiLottieImage(lottieComposition)
                    }

                gameState.gameOver ->
                    explosionLottieComposition?.let { lottieComposition ->
                        GameOverOverlay(
                            explosionLottieComposition = lottieComposition,
                            fontFamily = fontFamily
                        )
                    }
            }

            /*
             * The settings must overlay the "game over" text.
             */
            if (showSettings.value)
                SettingsDialog(
                    gameConfig = gameConfig.value,
                    fontFamily = fontFamily,
                    onCancel = {
                        showSettings.value = false
                    },
                    onConfirm = { newGameSettings ->

                        showSettings.value = false

                        gameConfig.value = newGameSettings
                    }
                )
        }

        AppFooter(fontFamily)
    }
}
