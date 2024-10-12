package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector3
import de.ventolotl.lagrange.utility.plus
import de.ventolotl.lagrange.utility.times
import javafx.scene.paint.Color
import kotlin.math.abs

object ColorInterpolator {
    inline fun <T> linearGradient(
        interpolations: List<T>,
        value: (T) -> Double,
        color: (T) -> Color,
        i: Double
    ): Color {
        val below = interpolations.lastOrNull { element ->
            value(element) < i
        }
        val above = interpolations.firstOrNull { element ->
            value(element) >= i
        }

        if (below == null && above == null) {
            error("There should be at least one below or above")
        }

        return when {
            below == null -> color(above!!)
            above == null -> color(below)
            else -> {
                val vAbove = value(above)
                val vBelow = value(below)

                val contourAboveColor = color(above).toVec()
                val contourBelowColor = color(below).toVec()

                val coefficient = abs(i - vBelow) / abs(vBelow - vAbove)
                (coefficient * contourAboveColor + (1.0 - coefficient) * contourBelowColor).toColor()
            }
        }
    }

    fun Vector3<Double>.toColor(): Color {
        return Color.rgb(x.toInt(), y.toInt(), z.toInt())
    }

    fun Color.toVec(): Vector3<Double> {
        return 255.0 * Vector.of(red, green, blue)
    }
}