package ui

import maths.Function3d
import maths.Vector2d
import maths.distSq
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

open class IntractableContour(
    function3d: Function3d,
    contourLines: List<ContourLineColored>,
    scalingFactor: Int
) : ContourRenderer(contourLines, scalingFactor) {
    private val gradient = function3d.gradient()
    private var dragged = false
    private var gradientData: GradientData? = null

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

        val contourClosestPoints = contourLines
            .filter { line -> line.points.isNotEmpty() }
            .associateWith { line ->
                line.points.minBy { it.distSq(algebraicMousePoint) }
            }
        val (contour, nearestPoint) = contourClosestPoints.minBy { (_, point) ->
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
            contour.color
        )

        SwingUtilities.invokeLater { repaint() }
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)

        val gradientData = this.gradientData ?: return
        val point = gradientData.point
        val vector = gradientData.vector

        graphics.color = gradientData.color

        val radius = 15
        graphics.fillOval(point.x - radius / 2, point.y - radius / 2, radius, radius)
        graphics.drawArrowLine(point, vector, 10, 10)
    }

    private fun evaluateGradient(x: Double, y: Double): Vector2d<Double> {
        return Vector2d(gradient.x.eval(x, y), gradient.y.eval(x, y))
    }
}

data class GradientData(val point: Vector2d<Int>, val vector: Vector2d<Int>, val color: Color)
