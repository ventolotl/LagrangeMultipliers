package de.ventolotl.lagrange.maths.rootfinder

import de.ventolotl.lagrange.maths.Function2d
import de.ventolotl.lagrange.utility.DoubleRange
import kotlin.math.abs

private const val MAX_ITERATIONS = 10

fun Function2d.findRootNewton(range: DoubleRange, accuracy: Double): Double? {
    return findRootNewton(differentiate(), range, accuracy)
}

private fun Function2d.findRootNewton(derivative: Function2d, range: DoubleRange, accuracy: Double): Double? {
    var root = range.start

    repeat(MAX_ITERATIONS) {
        val nextRoot = root - eval(root) / derivative.eval(root)

        val delta = abs(root - nextRoot)
        if (delta < accuracy && range.includes(nextRoot)) {
            return nextRoot
        }

        root = nextRoot
    }

    return null
}

fun Function2d.findRootsNewton(range: DoubleRange, accuracy: Double): List<Double> {
    val distinctRoots = mutableListOf<Double>()
    val accuracySq = accuracy * accuracy

    range.iterate(accuracy) { start ->
        findRootNewton(DoubleRange(start, range.end), accuracy)?.let { root ->
            val unknownRoot = distinctRoots.none { otherRoot ->
                val delta = root - otherRoot
                delta * delta < accuracySq
            }

            if (unknownRoot) {
                distinctRoots.add(root)
            }
        }
    }

    return distinctRoots
}
