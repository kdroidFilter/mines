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

import kotlin.test.Test
import kotlin.test.assertEquals

class CellTypeTest {

    @Test
    fun testCellTypeValues() {

        /* Test that EMPTY has 0 adjacent mines */
        assertEquals(0, CellType.EMPTY.adjacentMineCount)

        /* Test that MINE has -1 adjacent mines */
        assertEquals(-1, CellType.MINE.adjacentMineCount)

        /* Test that ONE through EIGHT have the correct adjacent mine counts */
        assertEquals(1, CellType.ONE.adjacentMineCount)
        assertEquals(2, CellType.TWO.adjacentMineCount)
        assertEquals(3, CellType.THREE.adjacentMineCount)
        assertEquals(4, CellType.FOUR.adjacentMineCount)
        assertEquals(5, CellType.FIVE.adjacentMineCount)
        assertEquals(6, CellType.SIX.adjacentMineCount)
        assertEquals(7, CellType.SEVEN.adjacentMineCount)
        assertEquals(8, CellType.EIGHT.adjacentMineCount)
    }

    @Test
    fun testOfMineCount() {

        /* Test that ofMineCount returns the correct CellType for each mine count */
        assertEquals(CellType.EMPTY, CellType.ofMineCount(0))
        assertEquals(CellType.ONE, CellType.ofMineCount(1))
        assertEquals(CellType.TWO, CellType.ofMineCount(2))
        assertEquals(CellType.THREE, CellType.ofMineCount(3))
        assertEquals(CellType.FOUR, CellType.ofMineCount(4))
        assertEquals(CellType.FIVE, CellType.ofMineCount(5))
        assertEquals(CellType.SIX, CellType.ofMineCount(6))
        assertEquals(CellType.SEVEN, CellType.ofMineCount(7))
        assertEquals(CellType.EIGHT, CellType.ofMineCount(8))

        /* Test that ofMineCount returns EMPTY for invalid mine counts */
        assertEquals(CellType.EMPTY, CellType.ofMineCount(-1))
        assertEquals(CellType.EMPTY, CellType.ofMineCount(9))
    }
}
