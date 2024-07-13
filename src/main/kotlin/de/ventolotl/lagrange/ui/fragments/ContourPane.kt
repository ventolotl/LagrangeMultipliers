package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.fillOval
import de.ventolotl.lagrange.ui.utility.FunctionRenderer
import de.ventolotl.lagrange.ui.write
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.max

class ContourPane(private val lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourFont = Font.font("Arial", 15.0)

    private val function3d = lagrangePane.function3d
    private val constraint = lagrangePane.constraint

    private val solutions = function3d.optimize(constraint)

    override fun paint() {
        val width = max(canvas.width, canvas.height) * 0.02

        lagrangePane.contourLines.forEach { line ->
            FunctionRenderer.renderGraph(ctx, grid, line.points, line.color, width)
        }
        FunctionRenderer.renderGraph(ctx, grid, constraint.points, constraint.color, width = 5.0)

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