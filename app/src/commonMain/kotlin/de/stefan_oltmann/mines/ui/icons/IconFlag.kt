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

val IconFlag: ImageVector
    get() {
        if (_IconFlag != null) {
            return _IconFlag!!
        }
        _IconFlag = ImageVector.Builder(
            name = "IconFlag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFF5F6368))) {
                moveTo(200f, 880f)
                verticalLineToRelative(-760f)
                horizontalLineToRelative(640f)
                lineToRelative(-80f, 200f)
                lineToRelative(80f, 200f)
                lineTo(280f, 520f)
                verticalLineToRelative(360f)
                horizontalLineToRelative(-80f)
                close()
                moveTo(280f, 440f)
                horizontalLineToRelative(442f)
                lineToRelative(-48f, -120f)
                lineToRelative(48f, -120f)
                lineTo(280f, 200f)
                verticalLineToRelative(240f)
                close()
                moveTo(280f, 440f)
                verticalLineToRelative(-240f)
                verticalLineToRelative(240f)
                close()
            }
        }.build()

        return _IconFlag!!
    }

@Suppress("ObjectPropertyName")
private var _IconFlag: ImageVector? = null
