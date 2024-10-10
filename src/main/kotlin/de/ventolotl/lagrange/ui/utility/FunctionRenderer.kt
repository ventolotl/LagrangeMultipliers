package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.ui.fragments.GridPane
import de.ventolotl.lagrange.utility.Vector2
import de.ventolotl.lagrange.utility.distSq
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

internal object FunctionRenderer {
    fun computeAlgebraicConnections(
        algebraicCoordinates: List<Vector2<Double>>, distToConnect: Double
    ): List<Pair<Vector2<Double>, Vector2<Double>>> {
        val distToConnectSq = distToConnect * distToConnect
        val remainingAlgebraicCoords = algebraicCoordinates.toMutableList()

        val first = remainingAlgebraicCoords.firstOrNull() ?: return emptyList()
        var lastPoint = first

        val connections = mutableListOf<Pair<Vector2<Double>, Vector2<Double>>>()
        val jumps = mutableListOf<Vector2<Double>>()

        while (remainingAlgebraicCoords.isNotEmpty()) {
            val nearest = remainingAlgebraicCoords.minBy { point ->
                point.distSq(lastPoint)
            }

            if (nearest.distSq(lastPoint) < distToConnectSq) {
                connections.add(nearest to lastPoint)
            } else {
                // Jump to the next point
                jumps.add(nearest)
                jumps.add(lastPoint)
            }
            lastPoint = nearest
            remainingAlgebraicCoords.remove(nearest)
        }

        jumps.add(first)
        jumps.add(lastPoint)

        // We have to iterate through all jumps to draw the remaining lines
        jumps.forEach { point1 ->
            jumps.forEach { point2 ->
                if (point1 != point2 && point1.distSq(point2) < distToConnectSq) {
                    connections.add(point1 to point2)
                }
            }
        }

        return connections
    }

    fun renderGraph(
        grid: GridPane,
        ctx: GraphicsContext,
        algebraicConnections: List<Pair<Vector2<Double>, Vector2<Double>>>,
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
