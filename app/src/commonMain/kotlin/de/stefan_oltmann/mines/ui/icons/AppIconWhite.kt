package de.stefan_oltmann.mines.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIconWhite: ImageVector
    get() {
        if (_AppIconWhite != null) {
            return _AppIconWhite!!
        }
        _AppIconWhite = ImageVector.Builder(
            name = "AppIconWhite",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(256f, 256f)
                moveToRelative(-120f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, 240f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, -240f, 0f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 227f)
                lineTo(256f, 51f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(400f, 112f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(253f, 256f)
                lineTo(461f, 256f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(400f, 400f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 253f)
                lineTo(256f, 461f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(112f, 400f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(259f, 256f)
                lineTo(51f, 256f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(112f, 112f)
            }
        }.build()

        return _AppIconWhite!!
    }

@Suppress("ObjectPropertyName")
private var _AppIconWhite: ImageVector? = null
