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

class GameDifficultyTest {

    @Test
    fun testCalcMineCount() {
        /* Test EASY difficulty (10%) */
        assertEquals(1, GameDifficulty.EASY.calcMineCount(3, 3)) /* 9 cells * 10% = 0.9, rounded to 1 */
        assertEquals(4, GameDifficulty.EASY.calcMineCount(10, 4)) /* 40 cells * 10% = 4 */

        /* Test MEDIUM difficulty (15%) */
        assertEquals(1, GameDifficulty.MEDIUM.calcMineCount(3, 3)) /* 9 cells * 15% = 1.35, rounded to 1 */
        assertEquals(6, GameDifficulty.MEDIUM.calcMineCount(10, 4)) /* 40 cells * 15% = 6 */

        /* Test HARD difficulty (20%) */
        assertEquals(1, GameDifficulty.HARD.calcMineCount(3, 3)) /* 9 cells * 20% = 1.8, rounded to 1 */
        assertEquals(8, GameDifficulty.HARD.calcMineCount(10, 4)) /* 40 cells * 20% = 8 */
    }
}
