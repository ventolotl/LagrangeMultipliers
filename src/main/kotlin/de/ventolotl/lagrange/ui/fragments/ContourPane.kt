package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.utility.FunctionRenderer
import de.ventolotl.lagrange.ui.utility.fillOval
import de.ventolotl.lagrange.ui.utility.write
import de.ventolotl.lagrange.utility.Vector2
import de.ventolotl.lagrange.utility.distSq
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.max

private const val DIST_TO_CONNECT = 0.5

class ContourPane(lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourFont = Font.font("Arial", 15.0)

    private val function3d = lagrangePane.function3
    private val constraint = lagrangePane.constraint

    private val solutions = function3d.optimize(constraint.rootFunction, constraint.range, 0.05)
    private val connectedSolutions = FunctionRenderer.computeAlgebraicConnections(solutions, DIST_TO_CONNECT)

    private val contourConnections = lagrangePane.contourLines.associateWith {
        FunctionRenderer.computeAlgebraicConnections(it.points, DIST_TO_CONNECT)
    }
    private val constraintConnections = FunctionRenderer.computeAlgebraicConnections(constraint.points, DIST_TO_CONNECT)

    override fun paint() {
        val width = max(canvas.width, canvas.height) * 0.02

        contourConnections.forEach { (contour, connections) ->
            FunctionRenderer.renderGraph(grid, ctx, connections, contour.color, width)
        }
        FunctionRenderer.renderGraph(grid, ctx, constraintConnections, constraint.color, 4.0)

        paintSolutions()
    }

    private fun paintSolutions() {
        val width = 10.0

        // Draw a circle
        solutions.forEach { solution ->
            val windowPoint = grid.algebraicToWindowCoordinates(solution)
            ctx.fillOval(windowPoint, width, Color.WHITE)
        }

        // Write the coordinates of the solution
        val visualizedSolutions = mutableListOf<Vector2<Double>>()
        solutions.forEach { solution ->
            val solutionNearby = visualizedSolutions.any { other ->
                other.distSq(solution) < 0.01
            }
            if (solutionNearby) {
                return@forEach
            }

            visualizedSolutions.add(solution)
            val windowPoint = grid.algebraicToWindowCoordinates(solution)
            val text = "(%.3f, %.3f) at z=%.3f".format(
                solution.x, solution.y, function3d.eval(solution.x, solution.y)
            )
            ctx.write(text, windowPoint, Color.WHITE, contourFont)
        }

        // Connect nearby solutions
        FunctionRenderer.renderGraph(grid, ctx, connectedSolutions, Color.WHITE, width)
    }
}