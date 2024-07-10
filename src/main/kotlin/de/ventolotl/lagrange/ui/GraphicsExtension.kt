package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.utility.Vector2d
import java.awt.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal fun Graphics.drawText(text: String, pos: Vector2d<Int>, textColor: Color, font: Font) {
    this.font = font
    val rect = font.getStringBounds(text, fontMetrics.fontRenderContext)
    this.color = Color.BLACK
    fillRect(
        pos.x + rect.minX.roundToInt(),
        pos.y + rect.minY.roundToInt(),
        (rect.maxX - rect.minX).roundToInt(),
        (rect.maxY - rect.minY).roundToInt()
    )
    this.color = textColor
    drawString(text, pos.x, pos.y)
}

internal fun Graphics.drawCircle(pos: Vector2d<Int>, radius: Int, color: Color) {
    this.color = color
    fillOval(pos.x - radius / 2, pos.y - radius / 2, radius, radius)
}

internal fun Graphics.drawLine(vec1: Vector2d<Int>, vec2: Vector2d<Int>, color: Color, lineWidth: Float = 1f) {
    this.color = color

    if (this is Graphics2D) {
        val strokeSize = (stroke as? BasicStroke)?.lineWidth
        if (strokeSize != lineWidth) {
            stroke = BasicStroke(lineWidth)
        }
    }

    drawLine(vec1.x, vec1.y, vec2.x, vec2.y)
}

// from https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
fun Graphics.drawArrowLine(start: Vector2d<Int>, vec: Vector2d<Int>, d: Int, h: Int) {
    val x1 = start.x
    val y1 = start.y
    val x2 = vec.x
    val y2 = vec.y

    val dx: Int = x2 - x1
    val dy: Int = y2 - y1
    val D = sqrt((dx * dx + dy * dy).toDouble())
    var xm = D - d
    var xn = xm
    var ym = h.toDouble()
    var yn = (-h).toDouble()
    var x: Double
    val sin = dy / D
    val cos = dx / D

    x = xm * cos - ym * sin + x1
    ym = xm * sin + ym * cos + y1
    xm = x

    x = xn * cos - yn * sin + x1
    yn = xn * sin + yn * cos + y1
    xn = x

    val pointsX = intArrayOf(x2, xm.toInt(), xn.toInt())
    val pointsY = intArrayOf(y2, ym.toInt(), yn.toInt())

    drawLine(x1, y1, x2, y2)
    fillPolygon(pointsX, pointsY, 3)
}