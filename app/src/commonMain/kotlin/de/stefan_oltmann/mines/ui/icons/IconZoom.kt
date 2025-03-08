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

val IconZoom: ImageVector
    get() {
        if (_IconZoom != null) {
            return _IconZoom!!
        }
        _IconZoom = ImageVector.Builder(
            name = "IconZoom",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF5F6368))) {
                moveTo(120f, 840f)
                verticalLineToRelative(-240f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(104f)
                lineToRelative(124f, -124f)
                lineToRelative(56f, 56f)
                lineToRelative(-124f, 124f)
                horizontalLineToRelative(104f)
                verticalLineToRelative(80f)
                lineTo(120f, 840f)
                close()
                moveTo(636f, 380f)
                lineTo(580f, 324f)
                lineTo(704f, 200f)
                lineTo(600f, 200f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(240f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-104f)
                lineTo(636f, 380f)
                close()
            }
        }.build()

        return _IconZoom!!
    }

@Suppress("ObjectPropertyName")
private var _IconZoom: ImageVector? = null
