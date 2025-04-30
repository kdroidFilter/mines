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

/**
 * Represents the state of a game, including which cells are revealed and flagged.
 */
class GameState(
    val minefield: Minefield
) {

    private val revealedMatrix: Array<Array<Boolean>> =
        Array(minefield.width) {
            Array(minefield.height) {
                false
            }
        }

    private val flaggedMatrix: Array<Array<Boolean>> =
        Array(minefield.width) {
            Array(minefield.height) {
                false
            }
        }

    fun getRemainingFlagsCount(): Int =
        minefield.config.mineCount - flaggedMatrix.flatten().count { it }

    fun isRevealed(x: Int, y: Int): Boolean =
        revealedMatrix[x][y]

    /**
     * Check if all non-mine fields are revealed now.
     */
    fun isAllRevealed(): Boolean {

        for (x in 0 until minefield.width)
            for (y in 0 until minefield.height)
                if (!minefield.isMine(x, y) && !isRevealed(x, y))
                    return false

        return true
    }

    fun reveal(x: Int, y: Int) {

        /* Ignore call if coordinates are already revealed. */
        if (revealedMatrix[x][y])
            return

        /* Mark the current cell as revealed */
        revealedMatrix[x][y] = true

        /* Remove any flags that may have set on non-minefields. */
        flaggedMatrix[x][y] = false

        /* If the cell is empty, recursively reveal adjacent cells */
        if (minefield.getCellType(x, y) == CellType.EMPTY) {

            performOnAdjacentCells(x, y) { adjX, adjY ->

                if (isRevealed(adjX, adjY))
                    return@performOnAdjacentCells

                reveal(adjX, adjY)
            }
        }
    }

    /**
     * Reveal adjacent cells around a number field.
     *
     * Returns if we hit a mine.
     */
    fun revealAdjacentCells(x: Int, y: Int): Boolean {

        val cellType = minefield.getCellType(x, y)

        /*
         * Ignore non-number cells.
         */
        if (cellType == CellType.EMPTY || cellType == CellType.MINE)
            return false

        var hitMine = false

        if (cellType.adjacentMineCount > 0) {

            val adjacentFlags = countAdjacentFlags(x, y)

            if (cellType.adjacentMineCount == adjacentFlags) {

                performOnAdjacentCells(x, y) { adjX, adjY ->

                    if (isRevealed(adjX, adjY) || isFlagged(adjX, adjY))
                        return@performOnAdjacentCells

                    reveal(adjX, adjY)

                    /*
                     * We want to reveal all adjacent cells,
                     * so we don't immediately return here.
                     */
                    if (minefield.isMine(adjX, adjY))
                        hitMine = true
                }
            }
        }

        return hitMine
    }

    fun isFlagged(x: Int, y: Int): Boolean =
        flaggedMatrix[x][y]

    fun toggleFlag(x: Int, y: Int) {
        flaggedMatrix[x][y] = !flaggedMatrix[x][y]
    }

    fun flagAllMines() {

        for (x in 0 until minefield.width)
            for (y in 0 until minefield.height)
                if (minefield.isMine(x, y))
                    flaggedMatrix[x][y] = true
    }

    private fun countAdjacentFlags(x: Int, y: Int): Int =
        directionsOfAdjacentCells.count { (dx, dy) ->
            val adjX = x + dx
            val adjY = y + dy

            isCellWithinBounds(adjX, adjY) && isFlagged(adjX, adjY)
        }

    private fun isCellWithinBounds(x: Int, y: Int): Boolean =
        x in 0 until minefield.width && y in 0 until minefield.height

    private fun performOnAdjacentCells(
        x: Int,
        y: Int,
        action: (Int, Int) -> Unit
    ) {

        for ((dx, dy) in directionsOfAdjacentCells) {

            val adjX = x + dx
            val adjY = y + dy

            if (isCellWithinBounds(adjX, adjY))
                action(adjX, adjY)
        }
    }
}
