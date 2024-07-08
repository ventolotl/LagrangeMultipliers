package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Vector2d
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JPanel
import kotlin.math.roundToInt

open class GridRenderer(private val scalingFactor: Int) : JPanel() {
    private val darkGray = Color(20, 20, 20)

    private val halfWidth: Int get() = width / 2
    private val halfHeight: Int get() = height / 2

    init {
        val componentListener = object : ComponentListener {
            override fun componentResized(e: ComponentEvent?) {
                resize()
            }

            override fun componentMoved(e: ComponentEvent?) = Unit
            override fun componentShown(e: ComponentEvent?) = Unit
            override fun componentHidden(e: ComponentEvent?) = Unit
        }
        addComponentListener(componentListener)
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)

        val hints = RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        )
        (graphics as Graphics2D).setRenderingHints(hints)
        render(graphics)
        graphics.dispose()
    }

    open fun resize() {}

    open fun render(graphics: Graphics) {
        graphics.color = Color.BLACK
        graphics.fillRect(0, 0, width, height)
        drawAxis(graphics)
    }

    private fun drawAxis(graphics: Graphics) {
        drawGrid(graphics)
        graphics.drawLine(Vector2d(0, halfHeight), Vector2d(width, halfHeight), color = Color.BLACK)
        graphics.drawLine(Vector2d(halfWidth, 0), Vector2d(halfWidth, height), color = Color.BLACK)
    }

    private fun drawGrid(graphics: Graphics) {
        iterateAlgebraicValues { algebraicValue ->
            // Draw x-axis
            val windowX = algebraicToWindowCoordinates(Vector2d(algebraicValue, 0.0)).x
            if (windowX != halfWidth) {
                graphics.drawLine(
                    Vector2d(windowX, 0),
                    Vector2d(windowX, height),
                    color = darkGray,
                    lineWidth = 1f
                )
            }

            // Draw y-axis
            val windowY = algebraicToWindowCoordinates(Vector2d(0.0, algebraicValue)).y
            if (windowY != halfHeight) {
                graphics.drawLine(
                    Vector2d(0, windowY),
                    Vector2d(width, windowY),
                    color = darkGray,
                    lineWidth = 1f
                )
            }
        }
    }


    private inline fun iterateAlgebraicValues(iterator: (iteration: Double) -> Unit) {
        repeat(2 * scalingFactor + 1) { iterator(it.toDouble() - scalingFactor) }
    }

    fun windowToAlgebraicCoordinates(windowCoordinates: Vector2d<Int>): Vector2d<Double> {
        val algebraicX = (windowCoordinates.x - halfWidth) * scalingFactor / halfWidth.toDouble()
        val algebraicY = (windowCoordinates.y - halfHeight) * scalingFactor / halfHeight.toDouble()
        return Vector2d(algebraicX, -algebraicY)
    }

    fun algebraicToWindowCoordinates(algebraicCoordinates: Vector2d<Double>): Vector2d<Int> {
        val windowX = (algebraicCoordinates.x * halfWidth) / scalingFactor.toDouble() + halfWidth
        val windowY = (-algebraicCoordinates.y * halfHeight) / scalingFactor.toDouble() + halfHeight
        return Vector2d(windowX.roundToInt(), windowY.roundToInt())
    }
}