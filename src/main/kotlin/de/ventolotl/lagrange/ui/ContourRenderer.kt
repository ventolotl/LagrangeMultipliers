package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.optimize
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage

open class ContourRenderer(
    val contourLines: List<ContourLineColored>,
    val constraint: Constraint,
    scalingFactor: Int,
    val function3d: Function3d
) : GridRenderer(scalingFactor) {
    val contourFont by lazy {
        Font(graphics.font.name, Font.PLAIN, 20)
    }

    private val solutions = function3d.optimize(constraint)
    private var computedContour: BufferedImage? = null

    override fun render(graphics: Graphics) {
        super.render(graphics)

        val contourImage = this.computedContour
            ?: BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        graphics.font = contourFont

        if (this.computedContour == null) {
            val imageGraphics = contourImage.createGraphics()
            paintContours(imageGraphics)
            this.computedContour = contourImage
        }

        graphics.drawImage(contourImage, 0, 0, this)
    }

    override fun resize() {
        computedContour = null
    }

    private fun paintContours(graphics: Graphics) {
        contourLines.forEach { line ->
            FunctionRenderer.renderGraph(graphics, this, line.points, line.color)
        }
        FunctionRenderer.renderGraph(graphics, this, constraint.points, constraint.color, width = 5f)

        solutions.forEach { solution ->
            val windowPoint = algebraicToWindowCoordinates(solution)
            graphics.drawCircle(windowPoint, radius = 15, Color.WHITE)

            val text = "(%.3f, %.3f) at z=%.3f".format(
                solution.x, solution.y, function3d.eval(solution.x, solution.y)
            )
            graphics.drawText(text, windowPoint, Color.WHITE, contourFont)
        }
    }
}