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

package de.stefan_oltmann.mines.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.stefan_oltmann.mines.addRightClickListener
import de.stefan_oltmann.mines.model.CellType
import de.stefan_oltmann.mines.model.Minefield
import de.stefan_oltmann.mines.ui.theme.colorCardBackground
import de.stefan_oltmann.mines.ui.theme.colorCellBorder
import de.stefan_oltmann.mines.ui.theme.colorCellHidden
import de.stefan_oltmann.mines.ui.theme.colorEightAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorFiveAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorFourAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorMine
import de.stefan_oltmann.mines.ui.theme.colorOneAdjacentMine
import de.stefan_oltmann.mines.ui.theme.colorSevenAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorSixAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorThreeAdjacentMines
import de.stefan_oltmann.mines.ui.theme.colorTwoAdjacentMines
import de.stefan_oltmann.mines.ui.theme.lightGray
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val cellCornerRadius = CornerRadius(4f)

private val cellStroke = Stroke(
    width = 0F
)

@Composable
fun MinefieldCanvas(
    minefield: Minefield,
    cellSize: Float,
    density: Float,
    redrawState: MutableState<Int>,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    hit: (Int, Int) -> Unit,
    flag: (Int, Int) -> Unit
) {

    val cellSizeWithDensity = Size(
        width = cellSize * density,
        height = cellSize * density
    )

    val cellPaddingWithDensity = 2 * density

    val innerCellSizeWithDensity = cellSizeWithDensity.copy(
        width = cellSizeWithDensity.width - cellPaddingWithDensity * 2,
        height = cellSizeWithDensity.height - cellPaddingWithDensity * 2
    )

    Canvas(
        modifier = Modifier
            .size(
                width = (minefield.width * cellSize).dp,
                height = (minefield.height * cellSize).dp
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->

                        hit(
                            (offset.x / cellSizeWithDensity.width).toInt(),
                            (offset.y / cellSizeWithDensity.height).toInt()
                        )

                        redrawState.value += 1
                    },
                    onLongPress = { offset ->

                        flag(
                            (offset.x / cellSizeWithDensity.width).toInt(),
                            (offset.y / cellSizeWithDensity.height).toInt()
                        )

                        redrawState.value += 1
                    }
                )
            }
            .addRightClickListener { offset ->

                flag(
                    (offset.x / cellSizeWithDensity.width).toInt(),
                    (offset.y / cellSizeWithDensity.height).toInt()
                )

                redrawState.value += 1
            }
    ) {

        /*
         * Force redraw if state changes.
         *
         * FIXME This is a hack
         */
        redrawState.value

        for (x in 0 until minefield.width) {
            for (y in 0 until minefield.height) {

                val offset = Offset(
                    x * cellSizeWithDensity.width + cellPaddingWithDensity,
                    y * cellSizeWithDensity.height + cellPaddingWithDensity
                )

                if (minefield.isRevealed(x, y)) {

                    val cellType = minefield.getCellType(x, y)

                    drawRevealedCell(
                        cellType = cellType,
                        textMeasurer = textMeasurer,
                        fontFamily = fontFamily,
                        offset = offset,
                        cellSizeWithDensity = innerCellSizeWithDensity
                    )

                } else {

                    drawRoundRect(
                        color = colorCellHidden,
                        topLeft = offset,
                        size = innerCellSizeWithDensity,
                        cornerRadius = cellCornerRadius,
                        style = Fill
                    )

                    drawRoundRect(
                        color = colorCellBorder,
                        topLeft = offset,
                        size = innerCellSizeWithDensity,
                        cornerRadius = cellCornerRadius,
                        style = cellStroke
                    )

                    if (minefield.isFlagged(x, y)) {

                        drawFlag(
                            topLeft = offset,
                            size = innerCellSizeWithDensity
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawRevealedCell(
    cellType: CellType,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    offset: Offset,
    cellSizeWithDensity: Size,
) {

    drawRoundRect(
        color = colorCardBackground,
        topLeft = offset,
        size = cellSizeWithDensity,
        cornerRadius = cellCornerRadius,
        style = Fill
    )

    drawRoundRect(
        color = colorCellBorder,
        topLeft = offset,
        size = cellSizeWithDensity,
        cornerRadius = cellCornerRadius,
        style = cellStroke
    )

    when (cellType) {

        CellType.MINE ->
            drawMine(
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.ONE ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 1,
                color = colorOneAdjacentMine,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.TWO ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 2,
                color = colorTwoAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.THREE ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 3,
                color = colorThreeAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.FOUR ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 4,
                color = colorFourAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.FIVE ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 5,
                color = colorFiveAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.SIX ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 6,
                color = colorSixAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.SEVEN ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 7,
                color = colorSevenAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.EIGHT ->
            drawNumber(
                textMeasurer = textMeasurer,
                number = 8,
                color = colorEightAdjacentMines,
                fontFamily = fontFamily,
                topLeft = offset,
                size = cellSizeWithDensity
            )

        CellType.EMPTY -> Unit
    }
}

private fun DrawScope.drawNumber(
    textMeasurer: TextMeasurer,
    number: Number,
    color: Color,
    fontFamily: FontFamily,
    topLeft: Offset,
    size: Size,
) {

    val text = number.toString()

    val style = TextStyle.Default.copy(
        color = color,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )

    val textLayout = textMeasurer.measure(text, style)

    val centeredOffset = Offset(
        topLeft.x + (size.width - textLayout.size.width) / 2,
        topLeft.y + (size.height - textLayout.size.height) / 2
    )

    drawText(
        textMeasurer = textMeasurer,
        text = text,
        style = style,
        topLeft = centeredOffset,
        size = size
    )
}

private fun DrawScope.drawMine(
    topLeft: Offset,
    size: Size,
) {

    val center = Offset(
        x = topLeft.x + size.width / 2,
        y = topLeft.y + size.height / 2
    )

    val radius = size.width / 5

    drawCircle(
        color = colorMine,
        radius = radius,
        center = center
    )

    val lineLength = radius * 1.5f

    val angles = listOf(0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f)

    val explosionRaysPath = Path().apply {

        angles.forEach { angle ->

            val radians = angle * (PI.toFloat() / 180f)
            val endX = center.x + lineLength * cos(radians)
            val endY = center.y + lineLength * sin(radians)

            moveTo(center.x, center.y)
            lineTo(endX, endY)
        }
    }

    drawPath(
        path = explosionRaysPath,
        color = colorMine,
        style = Stroke(width = 4f)
    )
}

private fun DrawScope.drawFlag(
    topLeft: Offset,
    size: Size,
) {

    val poleHeight = size.height * 0.5f
    val poleWidth = size.width * 0.1f
    val flagWidth = size.width * 0.25f
    val flagHeight = size.height * 0.25f

    /* Calculate the centered position */
    val centerX = topLeft.x + size.width / 2
    val centerY = topLeft.y + size.height / 2

    /* Adjust so that the pole starts at the bottom center */
    val poleStartX = centerX - (poleWidth + flagWidth) / 2
    val poleStartY = centerY - poleHeight / 2
    val poleEndY = centerY + poleHeight / 2

    val flagStartX = poleStartX + poleWidth

    /* Define the flag shape with two jagged edges */
    val flagPath = Path().apply {

        moveTo(poleStartX, poleStartY)
        lineTo(poleStartX, poleEndY)
        lineTo(poleStartX + poleWidth, poleEndY)
        lineTo(poleStartX + poleWidth, poleStartY)

        /* Start flag slightly to the right of the pole */
        moveTo(flagStartX, poleStartY)

        lineTo(flagStartX + flagWidth, poleStartY)
        lineTo(flagStartX + flagWidth * 0.6f, poleStartY + flagHeight * 0.5f)
        lineTo(flagStartX + flagWidth, poleStartY + flagHeight)
        lineTo(flagStartX, poleStartY + flagHeight)

        close()
    }

    /* Draw the entire shape */
    drawPath(
        path = flagPath,
        color = lightGray
    )
}
