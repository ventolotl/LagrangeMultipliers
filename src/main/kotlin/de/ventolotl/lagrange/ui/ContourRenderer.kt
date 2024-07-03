package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.*
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

class ContourRenderer(
    val contourLines: List<ContourLineColored>,
    val constraint: Constraint,
    scalingFactor: Int,
    private val function3d: Function3d
) : GridRenderer(scalingFactor) {
    private val contourFont by lazy {
        Font(graphics.font.name, Font.PLAIN, 50)
    }

    private val solutions = function3d.optimize(constraint)

    private val gradientOptimizeFunc = function3d.gradient()
    private val gradientConstraintFunc = constraint.equation.gradient()

    private var dragged = false
    private var movingGradientContours = (0..10).map { i ->
        val index = ((contourLines.size - 1) * i / 10.0).toInt()
        contourLines[index]
    }
    private var renderData: RenderData? = null

    init {
        val mouseAdapter = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                dragged = !dragged
            }

            override fun mouseMoved(event: MouseEvent) {
                if (dragged) {
                    createGradients(event.x, event.y)
                }
            }
        }
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
    }

    override fun render(graphics: Graphics) {
        super.render(graphics)

        graphics.font = contourFont

        repaintContours(graphics)
        repaintGradient(graphics)
    }

    private fun repaintContours(graphics: Graphics) {
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
            graphics.drawText(text, windowPoint, Color.WHITE, font)
        }
    }

    private fun repaintGradient(graphics: Graphics) {
        renderData?.let { renderData ->
            renderGradient(graphics, renderData.gradientData1)
            renderGradient(graphics, renderData.gradientData2)
        }
    }

    private fun renderGradient(graphics: Graphics, gradientData: GradientData) {
        val point = gradientData.point
        val vector = gradientData.vector

        graphics.drawCircle(point, radius = 15, gradientData.color)
        graphics.drawArrowLine(point, vector, 10, 10)
    }

    private fun createGradients(mouseX: Int, mouseY: Int) {
        val mousePoint = Vector2d(mouseX, mouseY)
        val algebraicMousePoint = windowToAlgebraicCoordinates(mousePoint)

        val gradient1 = createGradientOptimizeFunction(algebraicMousePoint)
        val gradient2 = createGradientConstraintFunction(algebraicMousePoint)

        renderData = RenderData(gradient1, gradient2)

        SwingUtilities.invokeLater { repaint() }
    }

    private fun createGradientOptimizeFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val contourClosestPoints = movingGradientContours
            .filter { line -> line.points.isNotEmpty() }
            .associateWith { line ->
                line.points.minBy { it.distSq(algebraicMousePoint) }
            }
        val (contour, nearestPoint) = contourClosestPoints.minBy { (_, point) ->
            point.distSq(algebraicMousePoint)
        }

        return createGradientFunction(nearestPoint, gradientOptimizeFunc, contour.color)
    }

    private fun createGradientConstraintFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val nearestPoint = constraint.points.minBy { point ->
            point.distSq(algebraicMousePoint)
        }
        return createGradientFunction(nearestPoint, gradientConstraintFunc, constraint.color)
    }

    private fun createGradientFunction(
        nearestPoint: Vector2d<Double>,
        gradientFunc: Vector2d<Function3d>,
        color: Color
    ): GradientData {
        val nearestX = nearestPoint.x
        val nearestY = nearestPoint.y

        val gradient = Vector2d(
            gradientFunc.x.eval(nearestX, nearestY),
            gradientFunc.y.eval(nearestX, nearestY)
        )
        val adjustedGradient = Vector2d(gradient.x + nearestX, gradient.y + nearestY)

        return GradientData(
            algebraicToWindowCoordinates(nearestPoint),
            algebraicToWindowCoordinates(adjustedGradient),
            color
        )
    }
}

data class RenderData(val gradientData1: GradientData, val gradientData2: GradientData)

data class GradientData(
    val point: Vector2d<Int>,
    val vector: Vector2d<Int>,
    val color: Color
)
