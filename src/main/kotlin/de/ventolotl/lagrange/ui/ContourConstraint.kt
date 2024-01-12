package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.*
import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.connectPoints
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

class ContourConstraint(
    function3d: Function3d,
    val constraint: Constraint,
    contourLines: List<ContourLineColored>,
    scalingFactor: Int
) : IntractableContour(function3d, contourLines, scalingFactor) {
    private val color = Color.RED
    private var dragged = false
    private val gradient = constraint.equation.gradient()
    private var gradientData: GradientData? = null

    private val solutions = function3d.optimize(constraint)

    init {
        val mouseAdapter = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                dragged = !dragged
            }

            override fun mouseMoved(event: MouseEvent) {
                if (dragged) {
                    moveGradient(event.x, event.y)
                }
            }
        }
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
    }

    private fun moveGradient(mouseX: Int, mouseY: Int) {
        val mousePoint = Vector2d(mouseX, mouseY)
        val algebraicMousePoint = windowToAlgebraicCoordinates(mousePoint)

        val nearestPoint = constraint.points.minBy { point ->
            point.distSq(algebraicMousePoint)
        }

        val gradient = evaluateGradient(nearestPoint.x, nearestPoint.y)
        val adjustedGradient = Vector2d(
            gradient.x + nearestPoint.x,
            gradient.y + nearestPoint.y,
        )

        gradientData = GradientData(
            algebraicToWindowCoordinates(nearestPoint),
            algebraicToWindowCoordinates(adjustedGradient),
            color
        )

        SwingUtilities.invokeLater { repaint() }
    }

    override fun render(graphics: Graphics) {
        super.render(graphics)

        renderConstraintEquation(graphics)
        renderSolutions(graphics)

        val gradientData = this.gradientData ?: return
        val point = gradientData.point
        val vector = gradientData.vector

        graphics.color = gradientData.color

        val radius = 15
        graphics.fillOval(point.x - radius / 2, point.y - radius / 2, radius, radius)
        graphics.drawArrowLine(point, vector, 10, 10)
    }

    private fun renderConstraintEquation(graphics: Graphics) {
        val points = constraint.points

        val windowCoordinates = points.map(::algebraicToWindowCoordinates)

        val sortedPoints = windowCoordinates.connectPoints(
            Vector2dRange(0..width, 0..height)
        )

        sortedPoints.forEach { points ->
            var index = 0
            while (index++ < points.size) {
                val point1 = points.getOrNull(index) ?: return@forEach
                val point2 = points.getOrNull(index + 1) ?: return@forEach

                graphics.color = color
                (graphics as Graphics2D).stroke = BasicStroke(2.2f)
                graphics.drawLine(point1.x, point1.y, point2.x, point2.y)
            }
        }
    }

    private fun renderSolutions(graphics: Graphics) {
        graphics.color = Color.WHITE

        solutions.forEach { point ->
            val windowPoint = algebraicToWindowCoordinates(point)

            val radius = 15
            graphics.fillOval(windowPoint.x - radius / 2, windowPoint.y - radius / 2, radius, radius)
        }
    }

    private fun evaluateGradient(x: Double, y: Double): Vector2d<Double> {
        return Vector2d(gradient.x.eval(x, y), gradient.y.eval(x, y))
    }
}