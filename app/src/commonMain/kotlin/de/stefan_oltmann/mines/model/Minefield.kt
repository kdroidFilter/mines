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

import kotlin.random.Random

class Minefield(
    val config: GameConfig,
    val seed: Int
) {

    // val id: String = "$width-$height-$mineCount-$seed"

    val width
        get() = config.mapWidth

    val height
        get() = config.mapHeight

    private val mineCount = config.difficulty.calcMineCount(width, height)

    init {

        /*
         * Protection from infinite loop on mine placement.
         */
        check(mineCount <= width * height) {
            "Too many mines. Wanted $mineCount, but must be less than ${width.times(height)}!"
        }
    }

    private val matrix: Array<Array<CellType>> =
        createMatrix(width, height, mineCount, seed)

    private val revealedMatrix: Array<Array<Boolean>> =
        Array(width) {
            Array(height) {
                false
            }
        }

    private val flaggedMatrix: Array<Array<Boolean>> =
        Array(width) {
            Array(height) {
                false
            }
        }

    fun getRemainingFlagsCount(): Int =
        mineCount - flaggedMatrix.flatten().count { it }

    fun isRevealed(x: Int, y: Int): Boolean =
        revealedMatrix[x][y]

    /**
     * Check if all non-mine fields are revealed now.
     */
    fun isAllRevealed(): Boolean {

        for (x in 0 until width)
            for (y in 0 until height)
                if (!isMine(x, y) && !isRevealed(x, y))
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
        if (matrix[x][y] == CellType.EMPTY) {

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

        val cellType = matrix[x][y]

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
                    if (isMine(adjX, adjY))
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

        for (x in 0 until width)
            for (y in 0 until height)
                if (isMine(x, y))
                    flaggedMatrix[x][y] = true
    }

    fun getCellType(x: Int, y: Int): CellType = matrix[x][y]

    fun isMine(x: Int, y: Int): Boolean =
        matrix[x][y] == CellType.MINE

    private fun countAdjacentFlags(x: Int, y: Int): Int =
        directionsOfAdjacentCells.count { (dx, dy) ->

            val adjX = x + dx
            val adjY = y + dy

            isCellWithinBounds(adjX, adjY) && isFlagged(adjX, adjY)
        }

    private fun isCellWithinBounds(x: Int, y: Int): Boolean =
        x in 0 until width && y in 0 until height

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

    companion object {

        private fun createMatrix(
            width: Int,
            height: Int,
            mineCount: Int,
            seed: Int
        ): Array<Array<CellType>> {

            val matrix = createEmptyMatrix(width, height)

            placeMines(matrix, width, height, mineCount, seed)

            placeCounts(matrix, width, height)

            return matrix
        }

        private fun createEmptyMatrix(width: Int, height: Int): Array<Array<CellType>> =
            Array(width) {
                Array(height) {
                    CellType.EMPTY
                }
            }

        fun calcProtectedXRange(width: Int) =
            width / 2 - 1..width / 2 + 1

        fun calcProtectedYRange(height: Int) =
            height / 2 - 1..height / 2 + 1

        private fun placeMines(
            matrix: Array<Array<CellType>>,
            width: Int,
            height: Int,
            mineCount: Int,
            seed: Int
        ) {

            /*
             * Mines are placed according to seed to reproduce results.
             */
            val random = Random(seed)

            val protectedXRange = calcProtectedXRange(width)
            val protectedYRange = calcProtectedYRange(height)

            var placedMinesCount = 0

            while (placedMinesCount < mineCount) {

                val x = random.nextInt(width)
                val y = random.nextInt(height)

                /*
                 * Keep the middle free of mines to give players a starting point.
                 */
                if (x in protectedXRange && y in protectedYRange)
                    continue

                /*
                 * Only place mines into empty cells.
                 *
                 * This guarantees that we have enough mines,
                 * even if the randomizer selects the same cell twice.
                 */
                if (matrix[x][y] == CellType.EMPTY) {

                    matrix[x][y] = CellType.MINE

                    placedMinesCount++
                }
            }
        }

        private fun placeCounts(
            matrix: Array<Array<CellType>>,
            width: Int,
            height: Int
        ) {

            for (x in 0 until width) {
                for (y in 0 until height) {

                    /* Minefields stay as they are. */
                    if (matrix[x][y] == CellType.MINE)
                        continue

                    val mineCount = countMinesInAdjacentCells(matrix, x, y)

                    matrix[x][y] = CellType.ofMineCount(mineCount)
                }
            }
        }

        private fun countMinesInAdjacentCells(
            matrix: Array<Array<CellType>>,
            x: Int,
            y: Int
        ): Int =
            directionsOfAdjacentCells.count { (dx, dy) ->

                hasMine(
                    matrix = matrix,
                    x = x + dx,
                    y = y + dy
                )
            }

        private fun hasMine(
            matrix: Array<Array<CellType>>,
            x: Int,
            y: Int
        ): Boolean {

            /* Return zero for out-of-bounds fields */
            @Suppress("ComplexCondition")
            if (x < 0 || y < 0 || x >= matrix.size || y >= matrix[x].size)
                return false

            return matrix[x][y] == CellType.MINE
        }
    }
}
