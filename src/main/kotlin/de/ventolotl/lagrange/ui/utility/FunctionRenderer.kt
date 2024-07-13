package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.ui.fragments.GridPane
import de.ventolotl.lagrange.ui.strokeLine
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

internal object FunctionRenderer {
    fun computeAlgebraicConnections(
        algebraicCoordinates: List<Vector2d<Double>>,
        distToConnect: Double
    ): List<Pair<Vector2d<Double>, Vector2d<Double>>> {
        val distToConnectSq = distToConnect * distToConnect
        val remainingAlgebraicCoords = algebraicCoordinates.toMutableList()

        val first = remainingAlgebraicCoords.firstOrNull() ?: return emptyList()
        var lastPoint = first

        val list = mutableListOf<Pair<Vector2d<Double>, Vector2d<Double>>>()

        while (remainingAlgebraicCoords.isNotEmpty()) {
            val nearest = remainingAlgebraicCoords.minBy { point ->
                point.distSq(lastPoint)
            }

            if (nearest.distSq(lastPoint) < distToConnectSq) {
                list.add(nearest to lastPoint)
            }
            lastPoint = nearest
            remainingAlgebraicCoords.remove(nearest)
        }

        if (first.distSq(lastPoint) < distToConnectSq) {
            list.add(first to lastPoint)
        }

        return list
    }

    fun renderGraph(
        grid: GridPane,
        ctx: GraphicsContext,
        algebraicConnections: List<Pair<Vector2d<Double>, Vector2d<Double>>>,
        color: Color,
        width: Double = 20.0
    ) {
        algebraicConnections.forEach { (first, second) ->
            val windowCoordinates1 = grid.algebraicToWindowCoordinates(first)
            val windowCoordinates2 = grid.algebraicToWindowCoordinates(second)
            ctx.strokeLine(windowCoordinates1, windowCoordinates2, color, width)
        }
    }
}
