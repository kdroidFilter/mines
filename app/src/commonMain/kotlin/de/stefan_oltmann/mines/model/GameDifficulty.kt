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

enum class GameDifficulty(
    private val minePercentage: Int
) {

    EASY(10),
    MEDIUM(15),
    HARD(20);

    fun calcMineCount(mapWidth: Int, mapHeight: Int): Int {

        val cellCount = mapWidth * mapHeight

        return (cellCount * (minePercentage / 100f)).toInt().coerceAtLeast(1)
    }
}
