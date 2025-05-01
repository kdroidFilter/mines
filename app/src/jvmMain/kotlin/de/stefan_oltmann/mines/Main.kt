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

package de.stefan_oltmann.mines

import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.stefan_oltmann.mines.ui.icons.AppIcon
import io.github.kdroidfilter.platformtools.OperatingSystem
import io.github.kdroidfilter.platformtools.darkmodedetector.windows.setWindowsAdaptiveTitleBar
import io.github.kdroidfilter.platformtools.getOperatingSystem
import java.awt.Dimension

fun main() {
    //Title bar in dark mode on macOS.
    if (getOperatingSystem() == OperatingSystem.MACOS) System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = APP_TITLE,
            icon = rememberVectorPainter(AppIcon)
        ) {
            //Title bar in dark mode on Windows.
            window.setWindowsAdaptiveTitleBar(dark = true)
            /*
             * The layout breaks if we allow too small sizes.
             */
            this.window.minimumSize = Dimension(600, 600)

            App()

        }
    }
}
