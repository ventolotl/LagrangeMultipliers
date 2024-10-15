package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.utility.ColorInterpolator
import de.ventolotl.lagrange.ui.utility.FunctionRenderer
import de.ventolotl.lagrange.ui.utility.fillOval
import de.ventolotl.lagrange.ui.utility.write
import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector2
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.length
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class ContourPane(private val lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourFont = Font.font("Arial", 15.0)

    private val function3d = lagrangePane.function3
    private val constraint = lagrangePane.constraint

    private val solutions = function3d.optimize(constraint.rootFunction, constraint.range)

    private val distToConnect = min(constraint.range.rangeX.length, constraint.range.rangeY.length) / 10.0
    private val connectedSolutions = FunctionRenderer.computeAlgebraicConnections(solutions, distToConnect)
    private val constraintConnections = FunctionRenderer.computeAlgebraicConnections(constraint.points, distToConnect)

    override fun paint() {
        drawContour()
        FunctionRenderer.renderGraph(grid, ctx, constraintConnections, constraint.color, 4.0)

        paintSolutions()
    }

    private fun drawContour() {
        val width = max(canvas.width, canvas.height) * 0.002

        val step = max(2, width.toInt())

        for (px in 0..ceil(canvas.width).toInt() step step) {
            for (py in 0..ceil(canvas.height).toInt() step step) {
                val windowCoordinates = Vector.of(px.toDouble(), py.toDouble())
                val algebraicCoords = grid.windowToAlgebraicCoordinates(windowCoordinates)
                val z = function3d.eval(algebraicCoords)

                val color = ColorInterpolator.linearGradient(
                    lagrangePane.contourLines,
                    value = { coloredLine -> coloredLine.line.z },
                    color = { coloredLine -> coloredLine.colorVec },
                    i = z
                )

                ctx.fill = color
                ctx.fillRect(px.toDouble(), py.toDouble(), step.toDouble(), step.toDouble())
            }
        }
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