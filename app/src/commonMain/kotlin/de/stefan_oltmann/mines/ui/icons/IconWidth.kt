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

val IconWidth: ImageVector
    get() {
        if (_IconWidth != null) {
            return _IconWidth!!
        }
        _IconWidth = ImageVector.Builder(
            name = "IconWidth",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF5F6368))) {
                moveTo(160f, 800f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(80f, 720f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(160f, 160f)
                horizontalLineToRelative(640f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(880f, 240f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(800f, 800f)
                lineTo(160f, 800f)
                close()
                moveTo(800f, 240f)
                lineTo(160f, 240f)
                verticalLineToRelative(480f)
                horizontalLineToRelative(640f)
                verticalLineToRelative(-480f)
                close()
                moveTo(160f, 240f)
                verticalLineToRelative(480f)
                verticalLineToRelative(-480f)
                close()
                moveTo(360f, 600f)
                verticalLineToRelative(-240f)
                lineTo(240f, 480f)
                lineToRelative(120f, 120f)
                close()
                moveTo(720f, 480f)
                lineTo(600f, 360f)
                verticalLineToRelative(240f)
                lineToRelative(120f, -120f)
                close()
            }
        }.build()

        return _IconWidth!!
    }

@Suppress("ObjectPropertyName")
private var _IconWidth: ImageVector? = null
