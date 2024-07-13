package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.ui.fragments.GridPane
import de.ventolotl.lagrange.ui.strokeLine
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

internal object FunctionRenderer {
    private const val DIST_TO_CONNECT = 1.0
    private const val DIST_TO_CONNECT_SQ = DIST_TO_CONNECT * DIST_TO_CONNECT

    fun computeAlgebraicConnections(
        algebraicCoordinates: List<Vector2d<Double>>
    ): List<Pair<Vector2d<Double>, Vector2d<Double>>> {
        val remainingAlgebraicCoords = algebraicCoordinates.toMutableList()

        val first = remainingAlgebraicCoords.firstOrNull() ?: return emptyList()
        var lastPoint = first

        val list = mutableListOf<Pair<Vector2d<Double>, Vector2d<Double>>>()

        while (remainingAlgebraicCoords.isNotEmpty()) {
            val nearest = remainingAlgebraicCoords.minBy { point ->
                point.distSq(lastPoint)
            }

            if (nearest.distSq(lastPoint) < DIST_TO_CONNECT_SQ) {
                list.add(nearest to lastPoint)
            }
            lastPoint = nearest
            remainingAlgebraicCoords.remove(nearest)
        }

        if (first.distSq(lastPoint) < DIST_TO_CONNECT_SQ) {
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
