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

import de.stefan_oltmann.mines.DEFAULT_CELL_SIZE

/**
 * Generates and parses ASCII representations of a minefield.
 *
 * Uses:
 * - O for empty fields
 * - * for mines
 * - Numbers for number fields
 */
object MinefieldAscii {

    /**
     * Generates an ASCII representation of the given minefield.
     *
     * @param minefield The minefield to generate ASCII representation for
     * @return A string containing the ASCII representation of the minefield
     */
    fun toAscii(minefield: Minefield): String =
        buildString {

            /* Print info */
            append(minefield.config.difficulty)
            append("|")
            append(minefield.config.mapWidth)
            append("|")
            append(minefield.config.mapHeight)
            append("|")
            append(minefield.seed)

            appendLine()

            /* Separator line */
            repeat(minefield.config.mapWidth) {
                append("-")
            }

            appendLine()

            /* Print matrix */
            for (y in 0 until minefield.height) {
                for (x in 0 until minefield.width) {

                    val cellType = minefield.getCellType(x, y)

                    val asciiChar = getCellTypeChar(cellType)

                    append(asciiChar)
                }

                /* Add a new line after each row, except the last one */
                if (y < minefield.height - 1)
                    append('\n')
            }
        }

    /**
     * Parses an ASCII representation of a minefield into a Minefield object.
     *
     * @param ascii The ASCII representation to parse
     * @return The parsed Minefield object
     */
    fun fromAscii(ascii: String): Minefield {

        val lines = ascii.trim().lines()

        /* Parse the first line to get metadata */

        val metadataParts = lines[0].split("|")

        val difficulty = GameDifficulty.valueOf(metadataParts[0])
        val width = metadataParts[1].toInt()
        val height = metadataParts[2].toInt()
        val seed = metadataParts[3].toInt()

        /* Skip the separator line */
        val matrixLines = lines.drop(2)

        /* Create the matrix */
        val matrix = Array(width) { x ->
            Array(height) { y ->
                val asciiChar = matrixLines[y][x]
                getCellTypeFromChar(asciiChar)
            }
        }

        /* Create the config */
        val config = GameConfig(
            cellSize = DEFAULT_CELL_SIZE, /* Default value, not stored in ASCII */
            mapWidth = width,
            mapHeight = height,
            difficulty = difficulty
        )

        return Minefield(config, seed, matrix)
    }

    /**
     * Returns the ASCII character representation of a cell type.
     *
     * @param cellType The cell type to convert
     * @return The ASCII character representation
     */
    private fun getCellTypeChar(cellType: CellType): Char =
        when (cellType) {
            CellType.EMPTY -> 'O'
            CellType.MINE -> '*'
            CellType.ONE -> '1'
            CellType.TWO -> '2'
            CellType.THREE -> '3'
            CellType.FOUR -> '4'
            CellType.FIVE -> '5'
            CellType.SIX -> '6'
            CellType.SEVEN -> '7'
            CellType.EIGHT -> '8'
        }

    /**
     * Returns the cell type for a given ASCII character.
     *
     * @param char The ASCII character to convert
     * @return The corresponding cell type
     */
    private fun getCellTypeFromChar(char: Char): CellType =
        when (char) {
            'O' -> CellType.EMPTY
            '*' -> CellType.MINE
            '1' -> CellType.ONE
            '2' -> CellType.TWO
            '3' -> CellType.THREE
            '4' -> CellType.FOUR
            '5' -> CellType.FIVE
            '6' -> CellType.SIX
            '7' -> CellType.SEVEN
            '8' -> CellType.EIGHT
            else -> throw IllegalArgumentException("Unknown cell type character: $char")
        }
}
