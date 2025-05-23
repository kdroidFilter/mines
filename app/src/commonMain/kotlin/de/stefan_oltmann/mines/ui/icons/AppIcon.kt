package de.stefan_oltmann.mines.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private val fillColor = SolidColor(Color(0xFF777777))

val AppIcon: ImageVector
    get() {
        if (_AppIcon != null) {
            return _AppIcon!!
        }
        _AppIcon = ImageVector.Builder(
            name = "AppIcon",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(fill = fillColor) {
                moveTo(256f, 256f)
                moveToRelative(-120f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, 240f, 0f)
                arcToRelative(120f, 120f, 0f, isMoreThanHalf = true, isPositiveArc = true, -240f, 0f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 227f)
                lineTo(256f, 51f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(400f, 112f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(253f, 256f)
                lineTo(461f, 256f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(400f, 400f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 253f)
                lineTo(256f, 461f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(112f, 400f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(259f, 256f)
                lineTo(51f, 256f)
            }
            path(
                stroke = fillColor,
                strokeLineWidth = 50f
            ) {
                moveTo(256f, 256f)
                lineTo(112f, 112f)
            }
        }.build()

        return _AppIcon!!
    }

@Suppress("ObjectPropertyName")
private var _AppIcon: ImageVector? = null
