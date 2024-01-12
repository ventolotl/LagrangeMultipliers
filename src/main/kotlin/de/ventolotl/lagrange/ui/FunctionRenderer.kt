package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.connectPoints
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D

internal object FunctionRenderer {
    fun renderGraph(
        graphics: Graphics,
        gridRenderer: GridRenderer,
        points: List<Vector2d<Double>>,
        color: Color,
        width: Float = 2.2F
    ) {
        graphics.color = color
        (graphics as Graphics2D).stroke = BasicStroke(width)

        val windowCoordinates = points.map(gridRenderer::algebraicToWindowCoordinates)
        val connectedPoints = windowCoordinates.connectPoints(
            Vector2dRange(0..gridRenderer.width, 0..gridRenderer.height)
        )
        connectedPoints.forEach { pointsBundle ->
            var index = 0
            while (index++ < pointsBundle.size) {
                val point1 = pointsBundle.getOrNull(index) ?: return@forEach
                val point2 = pointsBundle.getOrNull(index + 1) ?: return@forEach
                graphics.drawLine(point1.x, point1.y, point2.x, point2.y)
            }
        }
    }
}
