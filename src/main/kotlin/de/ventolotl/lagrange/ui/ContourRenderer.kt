package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.connectPoints
import java.awt.*
import kotlin.math.roundToInt

open class ContourRenderer(
    val contourLines: List<ContourLineColored>,
    scalingFactor: Int
) : GridRenderer(scalingFactor) {
    private val contourFont by lazy {
        Font(graphics.font.name, Font.PLAIN, 20)
    }

    override fun render(graphics: Graphics) {
        super.render(graphics)

        graphics.font = contourFont

        contourLines.forEach { line ->
            renderContourLinePoints(graphics, line)
        }
        //  contourLines.forEach { line -> renderContourLineText(graphics, line) }
    }

    private fun renderContourLinePoints(graphics: Graphics, line: ContourLineColored) {
        val points = line.points
        val color = line.color

        val windowPoints = points.map { algebraicToWindowCoordinates(it) }

        val sortedPoints = windowPoints.connectPoints(
            Vector2dRange(0..width, 0..height)
        )

        sortedPoints.forEach { points ->
            var index = 0
            while (index++ < points.size) {
                val point1 = points.getOrNull(index) ?: return@forEach
                val point2 = points.getOrNull(index + 1) ?: return@forEach

                graphics.color = color
                (graphics as Graphics2D).stroke = BasicStroke(2.2f)
                graphics.drawLine(point1.x, point1.y, point2.x, point2.y)
            }
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
