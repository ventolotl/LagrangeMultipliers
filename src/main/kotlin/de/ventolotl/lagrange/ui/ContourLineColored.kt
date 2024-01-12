package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.ContourLine
import de.ventolotl.lagrange.maths.Vector2d
import java.awt.Color

fun List<ContourLine>.mapToColors(color: Array<Color>): List<ContourLineColored> {
    return this.withIndex().map { (index, line) ->
        val lineColor = color[index % color.size]
        ContourLineColored(line.z, line.points, lineColor)
    }
}

class ContourLineColored(z: Double, points: List<Vector2d<Double>>, val color: Color) : ContourLine(z, points)
