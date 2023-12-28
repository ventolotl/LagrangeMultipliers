package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Vector2d
import java.awt.Color
import java.awt.Graphics
import kotlin.math.sqrt

internal fun Graphics.drawLine(vec1: Vector2d<Int>, vec2: Vector2d<Int>, color: Color, expansion: Int = 4) {
    val colorBefore = this.color
    this.color = color

    val x1 = vec1.x
    val y1 = vec1.y

    val x2 = vec2.x
    val y2 = vec2.y

    val width = x2 - x1
    val height = y2 - y1

    fillRect(x1 - expansion, y1 - expansion, width + expansion, height + expansion)
    this.color = colorBefore
}

// Stolen from https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
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

    val xpoints = intArrayOf(x2, xm.toInt(), xn.toInt())
    val ypoints = intArrayOf(y2, ym.toInt(), yn.toInt())


    drawLine(x1, y1, x2, y2)
    fillPolygon(xpoints, ypoints, 3)
}