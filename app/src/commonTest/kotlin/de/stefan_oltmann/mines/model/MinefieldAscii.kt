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
 * Generates an ASCII representation of a minefield.
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
     * Returns the ASCII character representation of a cell type.
     *
     * @param cellType The cell type to convert
     * @return The ASCII character representation
     */
    private fun getCellTypeChar(cellType: CellType): Char {

        return when (cellType) {
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
    }
}
