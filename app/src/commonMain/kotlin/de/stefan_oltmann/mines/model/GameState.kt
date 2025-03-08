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

package de.stefan_oltmann.mines.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

private val gameStateScope = CoroutineScope(Dispatchers.Default)

class GameState {

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds = _elapsedSeconds.asStateFlow()

    private var gameStartTime = Instant.DISTANT_PAST

    private var isTimerRunning = false

    var gameOver = false

    var gameWon = false

    var minefield: Minefield? = null

    private fun generateSeed() =
        (1..Int.MAX_VALUE).random()

    private fun startTimer() {

        if (isTimerRunning)
            return

        isTimerRunning = true

        gameStateScope.launch {

            gameStartTime = Clock.System.now()

            while (isTimerRunning) {

                /*
                 * We try to prevent shifts that occur from delay() not being super-accurate.
                 */

                val result = Clock.System.now() - gameStartTime

                _elapsedSeconds.value = result.inWholeSeconds

                delay(1000)
            }
        }
    }

    fun restart(
        gameConfig: GameConfig
    ) {

        isTimerRunning = false
        _elapsedSeconds.value = 0

        gameOver = false
        gameWon = false

        minefield = Minefield(
            config = gameConfig,
            seed = generateSeed()
        )
    }

    fun hit(x: Int, y: Int) {

        val minefield = minefield ?: return

        /* Ignore further inputs if game ended. */
        if (gameOver || gameWon)
            return

        /* Start timer on first interaction after reset. */
        if (!isTimerRunning)
            startTimer()

        /* Ignore clicks on already revealed or flagged cells. */
        if (minefield.isRevealed(x, y) || minefield.isFlagged(x, y))
            return

        /* Reveal the field in any case */
        minefield.reveal(x, y)

        /* Check game over condition */
        if (minefield.isMine(x, y)) {

            isTimerRunning = false

            gameOver = true

            /* Show the user all mines. */
            minefield.revealAllMines()

            return
        }

        /* Check win condition */
        if (minefield.isAllFieldsRevealed()) {

            isTimerRunning = false

            gameWon = true
        }
    }

    fun flag(x: Int, y: Int) {

        val minefield = minefield ?: return

        /* Ignore further inputs if game ended. */
        if (gameOver || gameWon)
            return

        /* Start timer on first interaction after reset. */
        if (!isTimerRunning)
            startTimer()

        /* Only non-revealed fields can be flagged. */
        if (minefield.isRevealed(x, y))
            return

        minefield.toggleFlag(x, y)
    }
}
