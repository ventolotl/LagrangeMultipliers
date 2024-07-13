package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.fillOval
import de.ventolotl.lagrange.ui.utility.FunctionRenderer
import de.ventolotl.lagrange.ui.write
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.max

class ContourPane(lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourFont = Font.font("Arial", 15.0)

    private val function3d = lagrangePane.function3d
    private val constraint = lagrangePane.constraint

    private val solutions = function3d.optimize(constraint)

    private val contourConnections = lagrangePane.contourLines.associateWith {
        FunctionRenderer.computeAlgebraicConnections(it.points)
    }
    private val constraintConnections = FunctionRenderer.computeAlgebraicConnections(constraint.points)

    override fun paint() {
        val width = max(canvas.width, canvas.height) * 0.02

        contourConnections.forEach { (contour, connections) ->
            FunctionRenderer.renderGraph(grid, ctx, connections, contour.color, width)
        }
        FunctionRenderer.renderGraph(grid, ctx, constraintConnections, constraint.color, 4.0)

        solutions.forEach { solution ->
            val windowPoint = grid.algebraicToWindowCoordinates(solution)
            ctx.fillOval(windowPoint, radius = 15.0, Color.WHITE)

            val text = "(%.3f, %.3f) at z=%.3f".format(
                solution.x, solution.y, function3d.eval(solution.x, solution.y)
            )
            ctx.write(text, windowPoint, Color.WHITE, contourFont)
        }
    }
}