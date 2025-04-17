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

import de.stefan_oltmann.mines.MIN_LONG_SIDE

data class GameConfig(
    val cellSize: Int,
    val mapWidth: Int,
    val mapHeight: Int,
    val difficulty: GameDifficulty
) {

    init {
        check(mapWidth >= MIN_LONG_SIDE) { "Map width must be greater than $MIN_LONG_SIDE." }
        check(mapHeight >= MIN_LONG_SIDE) { "Map height must be greater than $MIN_LONG_SIDE." }
    }
}
