package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.ui.utility.strokeLine
import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector2
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
            Vector.of(0.0, canvasHalfHeight),
            Vector.of(canvasWidth, canvasHalfHeight),
            color = Color.BLACK,
            lineWidth = 1.0
        )
        ctx.strokeLine(
            Vector.of(canvasHalfWidth, 0.0),
            Vector.of(canvasHalfWidth, canvasHeight),
            color = Color.BLACK,
            lineWidth = 1.0
        )
    }

    private fun drawGrid() {
        iterateAlgebraicValues { algebraicValue ->
            // Draw x-axis
            val windowX = algebraicToWindowCoordinates(Vector.of(algebraicValue, 0.0)).x
            ctx.strokeLine(
                Vector.of(windowX, 0.0), Vector.of(windowX, canvasHeight), color = Color.BLACK, lineWidth = 0.1
            )

            // Draw y-axis
            val windowY = algebraicToWindowCoordinates(Vector.of(0.0, algebraicValue)).y
            ctx.strokeLine(
                Vector.of(0.0, windowY), Vector.of(canvasWidth, windowY), color = Color.BLACK, lineWidth = 0.1
            )
        }
    }

    private inline fun iterateAlgebraicValues(iterator: (iteration: Double) -> Unit) {
        repeat(2 * scalingFactor.roundToInt() + 1) { iterator(it.toDouble() - scalingFactor) }
    }

    fun windowToAlgebraicCoordinates(windowCoordinates: Vector2<Double>): Vector2<Double> {
        val algebraicX = (windowCoordinates.x - canvasHalfWidth) * scalingFactor / canvasHalfWidth
        val algebraicY = (windowCoordinates.y - canvasHalfHeight) * scalingFactor / canvasHalfHeight
        return Vector.of(algebraicX, -algebraicY)
    }

    fun algebraicToWindowCoordinates(algebraicCoordinates: Vector2<Double>): Vector2<Double> {
        val windowX = (algebraicCoordinates.x * canvasHalfWidth) / scalingFactor + canvasHalfWidth
        val windowY = (-algebraicCoordinates.y * canvasHalfHeight) / scalingFactor + canvasHalfHeight
        return Vector.of(windowX, windowY)
    }
}