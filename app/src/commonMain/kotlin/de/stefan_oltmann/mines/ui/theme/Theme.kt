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

package de.stefan_oltmann.mines.ui.theme

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val defaultRoundedCornerShape = RoundedCornerShape(8.dp)

val defaultSpacing = 8.dp
val doubleSpacing = defaultSpacing * 2
val halfSpacing = defaultSpacing / 2

/** Button size as recommended by Material Design */
val buttonSize = 48.dp

fun Modifier.halfPadding() = this.padding(halfSpacing)

fun Modifier.defaultPadding() = this.padding(defaultSpacing)

fun Modifier.doublePadding() = this.padding(doubleSpacing)

@Composable
fun HalfSpacer() = Spacer(Modifier.size(halfSpacing))

@Composable
fun DefaultSpacer() = Spacer(Modifier.size(defaultSpacing))

@Composable
fun DoubleSpacer() = Spacer(Modifier.size(doubleSpacing))

@Composable
fun ColumnScope.FillSpacer() = Spacer(Modifier.weight(1F))

@Composable
fun RowScope.FillSpacer() = Spacer(Modifier.weight(1F))

