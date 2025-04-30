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
    val seed: Int,
    val matrix: Array<Array<CellType>>
) {

    val width
        get() = config.mapWidth

    val height
        get() = config.mapHeight

    fun getCellType(x: Int, y: Int): CellType =
        matrix[x][y]

    fun isMine(x: Int, y: Int): Boolean =
        matrix[x][y] == CellType.MINE

    companion object {

        fun create(
            config: GameConfig,
            seed: Int
        ): Minefield =
            Minefield(
                config = config,
                seed = seed,
                matrix = createMatrix(
                    width = config.mapWidth,
                    height = config.mapHeight,
                    mineCount = config.mineCount,
                    seed = seed
                )
            )

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

        /* Calculates a centered protected range that scales with board size */
        fun calcProtectedRange(length: Int): IntRange {

            val targetSize = (length * 0.3).toInt().coerceAtLeast(2)

            val protectedSize =
                if (targetSize % 2 == length % 2)
                    targetSize
                else
                    targetSize + 1

            val start = (length - protectedSize) / 2

            return start until (start + protectedSize)
        }

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

            val protectedXRange = calcProtectedRange(width)
            val protectedYRange = calcProtectedRange(height)

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
