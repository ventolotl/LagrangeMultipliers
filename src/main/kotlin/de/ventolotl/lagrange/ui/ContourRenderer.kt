package de.ventolotl.lagrange.ui

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.roundToInt

open class ContourRenderer(
    val contourLines: List<ContourLineColored>,
    scalingFactor: Int
) : GridRenderer(scalingFactor) {
    private val contourFont by lazy {
        Font(graphics.font.name, Font.PLAIN, 20)
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)

        graphics.font = contourFont

        contourLines.forEach { line -> renderContourLinePoints(graphics, line) }
        contourLines.forEach { line -> renderContourLineText(graphics, line) }
    }

    private fun renderContourLinePoints(graphics: Graphics, line: ContourLineColored) {
        val points = line.points
        points.forEach { point ->
            val color = line.color
            graphics.drawLine(algebraicToWindowCoordinates(point), algebraicToWindowCoordinates(point), color)
        }
    }

    private fun renderContourLineText(graphics: Graphics, line: ContourLineColored) {
        val z = line.z
        val points = line.points

        val closestPointToOrigin = points.minByOrNull { it.x * it.x + it.y * it.y } ?: return
        val firstPointWindowCoords = algebraicToWindowCoordinates(closestPointToOrigin)

        val text = "z=$z"
        val rect = contourFont.getStringBounds(text, graphics.fontMetrics.fontRenderContext)

        graphics.color = Color.BLACK
        graphics.fillRect(
            firstPointWindowCoords.x + rect.minX.roundToInt(),
            firstPointWindowCoords.y + rect.minY.roundToInt(),
            (rect.maxX - rect.minX).roundToInt(),
            (rect.maxY - rect.minY).roundToInt()
        )

        graphics.color = line.color
        graphics.drawString(text, firstPointWindowCoords.x, firstPointWindowCoords.y)
    }
}
