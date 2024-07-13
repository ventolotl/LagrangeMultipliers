package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.ui.fragments.GridPane
import de.ventolotl.lagrange.ui.strokeLine
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.pow
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

internal object FunctionRenderer {
    fun renderGraph(
        ctx: GraphicsContext,
        gridPane: GridPane,
        points: List<Vector2d<Double>>,
        color: Color,
        width: Double = 20.0
    ) {
        val windowCoordinates = points
            .map(gridPane::algebraicToWindowCoordinates)
            .toMutableList()
        val first = windowCoordinates.firstOrNull() ?: return
        var lastPoint = first

        val connectDistSq = (gridPane.width / 10.0).pow(2)

        while (windowCoordinates.isNotEmpty()) {
            val nearest = windowCoordinates.minBy { point ->
                point.distSq(lastPoint)
            }

            val connect = nearest.distSq(lastPoint) < connectDistSq
            if (connect) {
                ctx.strokeLine(nearest, lastPoint, color, width)
            }
            lastPoint = nearest
            windowCoordinates.remove(nearest)
        }

        val connect = first.distSq(lastPoint) < connectDistSq
        if (connect) {
            ctx.strokeLine(first, lastPoint, color, width)
        }
    }
}
