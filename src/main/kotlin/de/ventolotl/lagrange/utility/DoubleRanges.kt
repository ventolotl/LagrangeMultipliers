package de.ventolotl.lagrange.utility

import de.ventolotl.lagrange.maths.Vector2d
import kotlin.math.max
import kotlin.math.min

infix fun Double.range(other: Double): DoubleRange {
    return DoubleRange(this, other)
}

data class DoubleRange(private val start0: Double, private val end0: Double) {
    val start = min(start0, end0)
    val end = max(start0, end0)

    inline fun iterate(step: Double, iteration: (value: Double) -> Unit) {
        var value = start

        while (value <= end) {
            iteration(value)
            value += step
        }
    }

    fun includes(value: Double): Boolean {
        return value in start..end
    }
}

infix fun Vector2d<Double>.range(other: Vector2d<Double>): DoubleVector2Range {
    return DoubleVector2Range(this, other)
}

data class DoubleVector2Range(private val start0: Vector2d<Double>, private val end0: Vector2d<Double>) {
    val startX = min(start0.x, end0.x)
    val endX = max(start0.x, end0.x)

    val startY = min(start0.y, end0.y)
    val endY = max(start0.y, end0.y)

    inline fun iterate(step: Double, iteration: (x: Double, y: Double) -> Unit) {
        val xRange = startX range endX
        val yRange = startY range endY

        xRange.iterate(step) { x ->
            yRange.iterate(step) { y ->
                iteration(x, y)
            }
        }
    }

    fun includes(point: Vector2d<Double>): Boolean {
        return point.x in startX..endX && point.y in startY..endY
    }
}
