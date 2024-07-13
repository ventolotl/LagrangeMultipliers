package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.ContourLine
import de.ventolotl.lagrange.utility.Vector2d
import javafx.scene.paint.Color

fun List<ContourLine>.mapToColors(color: Array<Color>): List<ContourLineColored> {
    return this.withIndex().map { (index, line) ->
        val colorIndex = (index * color.size.toDouble() / this.size.toDouble()).toInt()
        val lineColor = color[colorIndex]
        ContourLineColored(line.z, line.points, lineColor)
    }
}

class ContourLineColored(z: Double, points: List<Vector2d<Double>>, val color: Color) : ContourLine(z, points)