package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.maths.Function3
import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.utility.ColorInterpolator
import de.ventolotl.lagrange.ui.utility.drawArrowLine
import de.ventolotl.lagrange.ui.utility.fillOval
import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector2
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.plus
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class InteractivePane(lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val function3d = lagrangePane.function3
    private val constraint = lagrangePane.constraint

    private val gradientOptimizeFunc = function3d.gradient()
    private val gradientConstraintFunc = constraint.rootFunction.gradient()

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
        val mousePoint = Vector.of(mouseX, mouseY)
        val algebraicMousePoint = grid.windowToAlgebraicCoordinates(mousePoint)

        val optimizeFunctionGradient = createGradientOptimizeFunction(algebraicMousePoint)
        val constraintFunctionGradient = createGradientConstraintFunction(algebraicMousePoint) ?: return

        renderData = RenderData(optimizeFunctionGradient, constraintFunctionGradient)
    }

    private fun createGradientOptimizeFunction(algebraicMousePoint: Vector2<Double>): GradientData {
        val zValue = function3d.eval(algebraicMousePoint)
        val color = ColorInterpolator.linearGradient(
            contourLines,
            value = { coloredLine -> coloredLine.line.z },
            color = { coloredLine -> coloredLine.colorVec },
            i = zValue
        )
        return createGradientFunction(algebraicMousePoint, gradientOptimizeFunc, color.darker())
    }

    private fun createGradientConstraintFunction(algebraicMousePoint: Vector2<Double>): GradientData? {
        val distFunction = Function3 { x, y -> Vector.of(x, y).distSq(algebraicMousePoint) }
        val closestPoint = distFunction.optimize(
            constraint.rootFunction, constraint.range, 1.0, 4000
        ).minByOrNull { point ->
            point.distSq(algebraicMousePoint)
        } ?: return null
        return createGradientFunction(
            closestPoint,
            gradientConstraintFunc,
            constraint.color
        )
    }

    private fun createGradientFunction(
        nearestPoint: Vector2<Double>, gradientFunc: Vector2<Function3>, color: Color
    ): GradientData {
        val gradient = Vector.of(
            gradientFunc.x.eval(nearestPoint), gradientFunc.y.eval(nearestPoint)
        )
        val adjustedGradient = gradient + nearestPoint

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
    val point: Vector2<Double>, val vector: Vector2<Double>, val color: Color
)