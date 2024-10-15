package de.ventolotl.lagrange.ui.utility

import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector3
import de.ventolotl.lagrange.utility.plus
import de.ventolotl.lagrange.utility.times
import javafx.scene.paint.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object ColorInterpolator {
    inline fun <T> linearGradient(
        interpolations: List<T>,
        value: (T) -> Double,
        color: (T) -> Vector3<Double>,
        i: Double
    ): Color {
        val belowIndex = interpolations.binarySearchForBounds(i) { value(it) }
        val below = interpolations.getOrNull(belowIndex)
        val above = interpolations.getOrNull(belowIndex + 1)

        if (below == null && above == null) {
            error("There should be at least one below or above")
        }

        return when {
            below == null -> color(above!!)
            above == null -> color(below)
            else -> {
                val vAbove = value(above)
                val vBelow = value(below)
                val vBounds = max(vBelow, min(vAbove, i))

                val contourAboveColor = color(above)
                val contourBelowColor = color(below)

                val coefficient = abs(vBounds - vBelow) / abs(vBelow - vAbove)
                (coefficient * contourAboveColor + (1.0 - coefficient) * contourBelowColor)
            }
        }.toColor()
    }

    inline fun <T> List<T>.binarySearchForBounds(key: Double, selector: (T) -> Double): Int {
        var pivotIndex = (size - 1) / 2

        var lowerBoundIndex = 0
        var upperBoundIndex = size

        while (!(selector(this[pivotIndex]) < key && selector(this[pivotIndex + 1]) > key)) {
            pivotIndex = (lowerBoundIndex + upperBoundIndex) / 2

            val valueAtPivot = selector(this[pivotIndex])

            when {
                valueAtPivot == key -> return pivotIndex
                valueAtPivot < key -> lowerBoundIndex = pivotIndex
                valueAtPivot > key -> upperBoundIndex = pivotIndex
            }

            if (lowerBoundIndex >= size - 1) {
                return size - 1
            }

            if (upperBoundIndex == 0) {
                return 0
            }
        }

        return pivotIndex
    }

    fun Vector3<Double>.toColor(): Color {
        return Color.rgb(x.toInt(), y.toInt(), z.toInt())
    }

    fun Color.toVec(): Vector3<Double> {
        return 255.0 * Vector.of(red, green, blue)
    }
}