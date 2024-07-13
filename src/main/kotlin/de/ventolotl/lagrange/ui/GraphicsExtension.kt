package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.utility.Vector2d
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.hypot

internal fun GraphicsContext.strokeLine(
    start: Vector2d<Double>, end: Vector2d<Double>, color: Color? = null, lineWidth: Double = 10.0
) {
    color?.let { this.stroke = it }
    this.lineWidth = lineWidth
    strokeLine(start.x, start.y, end.x, end.y)
}

internal fun GraphicsContext.fillOval(
    point: Vector2d<Double>, radius: Double, color: Color? = null
) {
    val semiradius = radius / 2.0
    color?.let { this.fill = it }
    this.fillOval(point.x - semiradius, point.y - semiradius, radius, radius)
}

internal fun GraphicsContext.write(
    text: String, point: Vector2d<Double>, color: Color? = null, font: javafx.scene.text.Font? = null
) {
    color?.let { this.fill = it }
    font?.let { this.font = it }
    this.fillText(text, point.x, point.y)
}

// from https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
fun GraphicsContext.drawArrowLine(
    start: Vector2d<Double>, vec: Vector2d<Double>, d: Double, h: Double, color: Color
) {
    val x1 = start.x
    val y1 = start.y
    val x2 = vec.x
    val y2 = vec.y

    val dx = x2 - x1
    val dy = y2 - y1
    val D = hypot(dx, dy)
    var xm = D - d
    var xn = xm
    var ym = h
    var yn = -h
    var x: Double
    val sin = dy / D
    val cos = dx / D

    x = xm * cos - ym * sin + x1
    ym = xm * sin + ym * cos + y1
    xm = x

    x = xn * cos - yn * sin + x1
    yn = xn * sin + yn * cos + y1
    xn = x

    val pointsX = doubleArrayOf(x2, xm, xn)
    val pointsY = doubleArrayOf(y2, ym, yn)

    stroke = color
    strokeLine(x1, y1, x2, y2)
    fillPolygon(pointsX, pointsY, 3)
}