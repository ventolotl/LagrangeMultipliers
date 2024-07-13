package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.ui.strokeLine
import de.ventolotl.lagrange.utility.Vector2d
import javafx.scene.paint.Color
import kotlin.math.roundToInt

class GridPane(private val scalingFactor: Double, private val showGrid: Boolean) : UIFragment() {
    private val backgroundColor: Color = Color.rgb(20, 20, 20)

    private val canvasWidth get() = canvas.width
    private val canvasHeight get() = canvas.height
    private val canvasHalfWidth get() = canvasWidth * 0.5
    private val canvasHalfHeight get() = canvasHeight * 0.5

    override fun paint() {
        ctx.fill = backgroundColor

        if (showGrid) {
            drawAxis()
        }
    }

    private fun drawAxis() {
        drawGrid()

        ctx.strokeLine(
            Vector2d(0.0, canvasHalfHeight),
            Vector2d(canvasWidth, canvasHalfHeight),
            color = Color.BLACK,
            lineWidth = 1.0
        )
        ctx.strokeLine(
            Vector2d(canvasHalfWidth, 0.0),
            Vector2d(canvasHalfWidth, canvasHeight),
            color = Color.BLACK,
            lineWidth = 1.0
        )
    }

    private fun drawGrid() {
        iterateAlgebraicValues { algebraicValue ->
            // Draw x-axis
            val windowX = algebraicToWindowCoordinates(Vector2d(algebraicValue, 0.0)).x
            ctx.strokeLine(
                Vector2d(windowX, 0.0), Vector2d(windowX, canvasHeight), color = Color.BLACK, lineWidth = 0.1
            )

            // Draw y-axis
            val windowY = algebraicToWindowCoordinates(Vector2d(0.0, algebraicValue)).y
            ctx.strokeLine(
                Vector2d(0.0, windowY), Vector2d(canvasWidth, windowY), color = Color.BLACK, lineWidth = 0.1
            )
        }
    }

    private inline fun iterateAlgebraicValues(iterator: (iteration: Double) -> Unit) {
        repeat(2 * scalingFactor.roundToInt() + 1) { iterator(it.toDouble() - scalingFactor) }
    }

    fun windowToAlgebraicCoordinates(windowCoordinates: Vector2d<Double>): Vector2d<Double> {
        val algebraicX = (windowCoordinates.x - canvasHalfWidth) * scalingFactor / canvasHalfWidth
        val algebraicY = (windowCoordinates.y - canvasHalfHeight) * scalingFactor / canvasHalfHeight
        return Vector2d(algebraicX, -algebraicY)
    }

    fun algebraicToWindowCoordinates(algebraicCoordinates: Vector2d<Double>): Vector2d<Double> {
        val windowX = (algebraicCoordinates.x * canvasHalfWidth) / scalingFactor + canvasHalfWidth
        val windowY = (-algebraicCoordinates.y * canvasHalfHeight) / scalingFactor + canvasHalfHeight
        return Vector2d(windowX, windowY)
    }
}