package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.ContourLine
import de.ventolotl.lagrange.ui.utility.ColorInterpolator.toVec
import javafx.scene.paint.Color

fun List<ContourLine>.mapToColors(color: Array<Color>): List<ContourLineColored> {
    return this.withIndex().map { (index, line) ->
        val colorIndex = (index * color.size.toDouble() / this.size.toDouble()).toInt()
        val lineColor = color[colorIndex]
        ContourLineColored(lineColor, line)
    }
}

data class ContourLineColored(val color: Color, val line: ContourLine) {
    val colorVec = color.toVec()
}