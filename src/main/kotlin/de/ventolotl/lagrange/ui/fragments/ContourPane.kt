package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.utility.FunctionRenderer
import de.ventolotl.lagrange.ui.utility.fillOval
import de.ventolotl.lagrange.ui.utility.write
import de.ventolotl.lagrange.utility.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

private const val DIST_TO_CONNECT = 0.5

class ContourPane(private val lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourFont = Font.font("Arial", 15.0)

    private val function3d = lagrangePane.function3
    private val constraint = lagrangePane.constraint

    private val solutions = function3d.optimize(constraint.rootFunction, constraint.range, 0.05)
    private val connectedSolutions = FunctionRenderer.computeAlgebraicConnections(solutions, DIST_TO_CONNECT)
    private val contourConnections = lagrangePane.contourLines.associateWith {
        FunctionRenderer.computeAlgebraicConnections(it.line.points, DIST_TO_CONNECT)
    }
    private val constraintConnections = FunctionRenderer.computeAlgebraicConnections(constraint.points, DIST_TO_CONNECT)

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

                // This requires the contourLines to be indexed in ascending order (according to z)
                // The correct ordering is ensured when creating the ContourPlot
                val contourBelow = lagrangePane.contourLines.lastOrNull { otherColoredLevel ->
                    otherColoredLevel.line.z < z
                }
                val contourAbove = lagrangePane.contourLines.firstOrNull { otherColoredLevel ->
                    otherColoredLevel.line.z >= z
                }

                if (contourBelow == null && contourAbove == null) {
                    // Nothing to paint
                    return
                }

                val color = when {
                    contourBelow == null -> contourAbove!!.color
                    contourAbove == null -> contourBelow.color
                    else -> {
                        val zAbove = contourAbove.line.z
                        val zBelow = contourBelow.line.z

                        val contourAboveColor = contourAbove.color.toVec()
                        val contourBelowColor = contourBelow.color.toVec()

                        val coefficient = abs(z - zBelow) / abs(zBelow - zAbove)
                        (coefficient * contourAboveColor + (1.0 - coefficient) * contourBelowColor).toColor()
                    }
                }

                ctx.fill = color
                ctx.fillRect(px.toDouble(), py.toDouble(), step.toDouble(), step.toDouble())
            }
        }

        contourConnections.forEach { (contour, connections) ->
          // FunctionRenderer.renderGraph(grid, ctx, connections, Color.BLACK, width * 0.1)
        }
    }

    private fun Vector3<Double>.toColor(): Color {
        return Color.rgb(x.toInt(), y.toInt(), z.toInt())
    }

    private fun Color.toVec(): Vector3<Double> {
        return 255.0 * Vector.of(red, green, blue)
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