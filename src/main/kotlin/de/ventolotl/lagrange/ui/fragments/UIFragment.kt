package de.ventolotl.lagrange.ui.fragments

import de.ventolotl.lagrange.WIN_HEIGHT
import de.ventolotl.lagrange.WIN_WIDTH
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.StackPane

abstract class UIFragment : StackPane() {
    val canvas = Canvas(WIN_WIDTH, WIN_HEIGHT)
    val ctx: GraphicsContext get() = canvas.graphicsContext2D

    fun addCanvas() {
        children.add(canvas)
    }

    fun updateCanvas(width: Double, height: Double) {
        canvas.width = width
        canvas.height = height
    }

    fun update() {
        clear()
        paint()
    }

    fun clear() {
        ctx.clearRect(0.0, 0.0, canvas.width, canvas.height)
    }

    protected open fun paint() {}
}