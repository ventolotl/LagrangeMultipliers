package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.pow
import java.awt.Color
import java.awt.Graphics

internal object FunctionRenderer {
    fun renderGraph(
        graphics: Graphics,
        gridRenderer: GridRenderer,
        points: List<Vector2d<Double>>,
        color: Color,
        width: Float = 20F
    ) {
        val windowCoordinates = points
            .map(gridRenderer::algebraicToWindowCoordinates)
            .toMutableList()
        val first = windowCoordinates.firstOrNull() ?: return
        var lastPoint = first

        val connectDistSq = (gridRenderer.width / 10.0).pow(2)

        while (windowCoordinates.isNotEmpty()) {
            val nearest = windowCoordinates.minBy { point ->
                point.distSq(lastPoint)
            }

            val connect = nearest.distSq(lastPoint) < connectDistSq
            if (connect) {
                graphics.drawLine(nearest, lastPoint, color, width)
            }
            lastPoint = nearest
            windowCoordinates.remove(nearest)
        }

        val connect = first.distSq(lastPoint) < connectDistSq
        if (connect) {
            graphics.drawLine(first, lastPoint, color, width)
        }
    }
}
