/*
 * Material Design Icon under Apache 2 License
 * taken from https://fonts.google.com/icons
 */

package de.stefan_oltmann.mines.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconHeight: ImageVector
    get() {
        if (_IconHeight != null) {
            return _IconHeight!!
        }
        _IconHeight = ImageVector.Builder(
            name = "IconHeight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF5F6368))) {
                moveTo(240f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(160f, 800f)
                verticalLineToRelative(-640f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(240f, 80f)
                horizontalLineToRelative(480f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(800f, 160f)
                verticalLineToRelative(640f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(720f, 880f)
                lineTo(240f, 880f)
                close()
                moveTo(720f, 800f)
                verticalLineToRelative(-640f)
                lineTo(240f, 160f)
                verticalLineToRelative(640f)
                horizontalLineToRelative(480f)
                close()
                moveTo(720f, 160f)
                lineTo(240f, 160f)
                horizontalLineToRelative(480f)
                close()
                moveTo(360f, 360f)
                horizontalLineToRelative(240f)
                lineTo(480f, 240f)
                lineTo(360f, 360f)
                close()
                moveTo(480f, 720f)
                lineTo(600f, 600f)
                lineTo(360f, 600f)
                lineToRelative(120f, 120f)
                close()
            }
        }.build()

        return _IconHeight!!
    }

@Suppress("ObjectPropertyName")
private var _IconHeight: ImageVector? = null
