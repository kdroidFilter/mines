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

class MinefieldAsciiTest {

    @Test
    fun testSmallMinefieldToAscii() {

        assertEquals(
            expected = smallTestMinefieldAscii,
            actual = MinefieldAscii.toAscii(smallTestMinefield)
        )
    }

    @Test
    fun testMediumMinefieldToAscii() {

        assertEquals(
            expected = mediumTestMinefieldAscii,
            actual = MinefieldAscii.toAscii(mediumTestMinefield)
        )
    }

    @Test
    fun testLargeMinefieldToAscii() {

        assertEquals(
            expected = largeTestMinefieldAscii,
            actual = MinefieldAscii.toAscii(largeTestMinefield)
        )
    }

    @Test
    fun testSmallMinefieldFromAscii() {

        val parsedMinefield = MinefieldAscii.fromAscii(smallTestMinefieldAscii)

        /* Verify config properties */
        assertEquals(smallTestMinefield.config.difficulty, parsedMinefield.config.difficulty)
        assertEquals(smallTestMinefield.config.mapWidth, parsedMinefield.config.mapWidth)
        assertEquals(smallTestMinefield.config.mapHeight, parsedMinefield.config.mapHeight)
        assertEquals(smallTestMinefield.config.mineCount, parsedMinefield.config.mineCount)

        /* Verify seed */
        assertEquals(smallTestMinefield.seed, parsedMinefield.seed)

        /* Verify matrix */
        for (x in 0 until smallTestMinefield.width) {
            for (y in 0 until smallTestMinefield.height) {
                assertEquals(
                    smallTestMinefield.getCellType(x, y),
                    parsedMinefield.getCellType(x, y),
                    "Cell at ($x, $y) should match"
                )
            }
        }
    }

    @Test
    fun testMediumMinefieldFromAscii() {

        val parsedMinefield = MinefieldAscii.fromAscii(mediumTestMinefieldAscii)

        /* Verify config properties */
        assertEquals(mediumTestMinefield.config.difficulty, parsedMinefield.config.difficulty)
        assertEquals(mediumTestMinefield.config.mapWidth, parsedMinefield.config.mapWidth)
        assertEquals(mediumTestMinefield.config.mapHeight, parsedMinefield.config.mapHeight)
        assertEquals(mediumTestMinefield.config.mineCount, parsedMinefield.config.mineCount)

        /* Verify seed */
        assertEquals(mediumTestMinefield.seed, parsedMinefield.seed)

        /* Verify matrix */
        for (x in 0 until mediumTestMinefield.width) {
            for (y in 0 until mediumTestMinefield.height) {
                assertEquals(
                    mediumTestMinefield.getCellType(x, y),
                    parsedMinefield.getCellType(x, y),
                    "Cell at ($x, $y) should match"
                )
            }
        }
    }

    @Test
    fun testLargeMinefieldFromAscii() {

        val parsedMinefield = MinefieldAscii.fromAscii(largeTestMinefieldAscii)

        /* Verify config properties */
        assertEquals(largeTestMinefield.config.difficulty, parsedMinefield.config.difficulty)
        assertEquals(largeTestMinefield.config.mapWidth, parsedMinefield.config.mapWidth)
        assertEquals(largeTestMinefield.config.mapHeight, parsedMinefield.config.mapHeight)
        assertEquals(largeTestMinefield.config.mineCount, parsedMinefield.config.mineCount)

        /* Verify seed */
        assertEquals(largeTestMinefield.seed, parsedMinefield.seed)

        /* Verify matrix */
        for (x in 0 until largeTestMinefield.width) {
            for (y in 0 until largeTestMinefield.height) {
                assertEquals(
                    largeTestMinefield.getCellType(x, y),
                    parsedMinefield.getCellType(x, y),
                    "Cell at ($x, $y) should match"
                )
            }
        }
    }

    @Test
    fun testRoundTrip() {

        /* Test that converting to ASCII and back produces the same minefield */

        val ascii = MinefieldAscii.toAscii(smallTestMinefield)

        val parsedMinefield = MinefieldAscii.fromAscii(ascii)

        /* Verify matrix */
        for (x in 0 until smallTestMinefield.width) {
            for (y in 0 until smallTestMinefield.height) {
                assertEquals(
                    smallTestMinefield.getCellType(x, y),
                    parsedMinefield.getCellType(x, y),
                    "Cell at ($x, $y) should match after round trip"
                )
            }
        }
    }
}
