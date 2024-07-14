package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.drawArrowLine
import de.ventolotl.lagrange.ui.fillOval
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class InteractivePane(lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val function3d = lagrangePane.function3d
    private val constraint = lagrangePane.constraint

    private val gradientOptimizeFunc = function3d.gradient()
    private val gradientConstraintFunc = constraint.equation.gradient()

    private val contourLines = lagrangePane.contourLines

    private var dragged = false
    private var renderData: RenderData? = null

    init {
        setOnMousePressed {
            dragged = !dragged
        }
        setOnMouseMoved { event ->
            if (dragged && constraint.points.isNotEmpty()) {
                createGradients(event.x, event.y)
                renderInteractiveMouse()
            }
        }
    }

    private fun renderInteractiveMouse() {
        clear()
        paintRenderData(ctx)
    }

    private fun paintRenderData(ctx: GraphicsContext) {
        renderData?.let { (optimizeGradient, constraintGradient) ->
            // Render gradients
            renderGradient(ctx, optimizeGradient)
            renderGradient(ctx, constraintGradient)
        }
    }

    private fun renderGradient(ctx: GraphicsContext, gradientData: GradientData) {
        val point = gradientData.point
        val vector = gradientData.vector

        ctx.fillOval(point, 15.0, gradientData.color)
        ctx.drawArrowLine(point, vector, 10.0, 10.0, gradientData.color)
    }

    private fun createGradients(mouseX: Double, mouseY: Double) {
        val mousePoint = Vector2d(mouseX, mouseY)
        val algebraicMousePoint = grid.windowToAlgebraicCoordinates(mousePoint)

        val optimizeFunctionGradient = createGradientOptimizeFunction(algebraicMousePoint)
        val constraintFunctionGradient = createGradientConstraintFunction(algebraicMousePoint)

        renderData = RenderData(optimizeFunctionGradient, constraintFunctionGradient)
    }

    private fun createGradientOptimizeFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val contourClosestPoints = contourLines
            .filter { line -> line.points.isNotEmpty() }
            .associateWith { line ->
                line.points.minBy { it.distSq(algebraicMousePoint) }
            }
        val (contour, nearestPoint) = contourClosestPoints.minBy { (_, point) ->
            point.distSq(algebraicMousePoint)
        }

        return createGradientFunction(nearestPoint, gradientOptimizeFunc, contour.color.darker())
    }

    private fun createGradientConstraintFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val nearestPoint = constraint.points.minBy { point ->
            point.distSq(algebraicMousePoint)
        }
        return createGradientFunction(nearestPoint, gradientConstraintFunc, constraint.color)
    }

    private fun createGradientFunction(
        nearestPoint: Vector2d<Double>, gradientFunc: Vector2d<Function3d>, color: Color
    ): GradientData {
        val nearestX = nearestPoint.x
        val nearestY = nearestPoint.y

        val gradient = Vector2d(
            gradientFunc.x.eval(nearestX, nearestY), gradientFunc.y.eval(nearestX, nearestY)
        )
        val adjustedGradient = Vector2d(gradient.x + nearestX, gradient.y + nearestY)

        return GradientData(
            grid.algebraicToWindowCoordinates(nearestPoint),
            grid.algebraicToWindowCoordinates(adjustedGradient),
            color
        )
    }
}

private data class RenderData(
    val optimizeGradient: GradientData, val constraintGradient: GradientData
)

private data class GradientData(
    val point: Vector2d<Double>, val vector: Vector2d<Double>, val color: Color
)