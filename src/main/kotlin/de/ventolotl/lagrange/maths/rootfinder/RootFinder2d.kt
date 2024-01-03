package de.ventolotl.lagrange.maths.rootfinder

import de.ventolotl.lagrange.maths.Function2d
import de.ventolotl.lagrange.utility.DoubleRange
import kotlin.math.abs

private const val MAX_ITERATIONS = 10
private const val PRECISION = 1e-9

fun Function2d.findRootNewton(range: DoubleRange, accuracy: Double): Double? {
    return findRootNewton(differentiate(), range, accuracy)
}

private fun Function2d.findRootNewton(
    derivative: Function2d,
    range: DoubleRange,
    precision: Double = PRECISION
): Double? {
    var root = range.start

    repeat(MAX_ITERATIONS) {
        val nextRoot = root - eval(root) / derivative.eval(root)

        val delta = abs(root - nextRoot)
        if (delta < precision && range.includes(nextRoot)) {
            return nextRoot
        }

        root = nextRoot
    }

    return null
}

fun Function2d.findRootsNewton(range: DoubleRange, precision: Double = PRECISION): List<Double> {
    val roots = mutableListOf<Double>()

    range.iterate(precision) { start ->
        findRootNewton(DoubleRange(start, range.end), precision)?.let { root ->
            roots.add(root)
        }
    }

    return roots
}
