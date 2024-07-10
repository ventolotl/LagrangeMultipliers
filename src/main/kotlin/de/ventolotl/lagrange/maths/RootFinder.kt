package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Range
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.iterate
import kotlin.math.absoluteValue

// Error reduces ^2 each iteration, so 50 should be enough
private const val NEWTON_ITERATIONS = 50

fun Function3d.findRootsNewton(range: Vector2dRange<Double>, accuracy: Int = 100): List<Vector2d<Double>> {
    val roots = mutableListOf<Vector2d<Double>>()

    // Fix x in order to project 2-valued function to single-valued function
    val totalRangeX = range.endX - range.startX
    val stepSizeX = totalRangeX / accuracy.toDouble()

    range.rangeX.iterate(stepSizeX) { fixedX ->
        val projectedY = this.evalX(fixedX)
        projectedY.findRootsNewton(range.rangeY, accuracy).forEach { rootY ->
            roots.add(Vector2d(fixedX, rootY))
        }
    }

    // Fix y in order to project 2-valued function to single-valued function
    val totalRangeY = range.endY - range.startY
    val stepSizeY = totalRangeY / accuracy.toDouble()

    range.rangeY.iterate(stepSizeY) { fixedY ->
        val projectedX = this.evalY(fixedY)
        projectedX.findRootsNewton(range.rangeX, accuracy).forEach { rootX ->
            roots.add(Vector2d(rootX, fixedY))
        }
    }

    return roots
}

fun Function2d.findRootsNewton(range: Range<Double>, accuracy: Int): List<Double> {
    val roots = mutableListOf<Double>()
    val approximatedRoots = this.approximatedRoots(range, accuracy)

    approximatedRoots.forEach { initialGuess ->
        this.rootFinder2dExact(initialGuess)?.let { root ->
            roots.add(root)
        }
    }

    return roots
}

fun Function2d.rootFinder2dExact(guess: Double): Double? {
    var currentX = guess
    val derivative = this.differentiate()

    repeat(NEWTON_ITERATIONS) {
        val yValue = this.eval(currentX)

        if (yValue.absoluteValue < 1e-5) {
            return currentX
        }

        currentX -= yValue / derivative.eval(currentX)
    }

    return null
}

// We apply the intermediate value theorem to isolate possible roots of our single-valued function
private fun Function2d.approximatedRoots(range: Range<Double>, accuracy: Int = 100): List<Double> {
    val approximatedRoots = mutableListOf<Double>()

    val totalRange = range.end - range.start
    val stepSize = totalRange / accuracy.toDouble()

    var lastSign = this.eval(range.start) > 0

    range.iterate(stepSize) { x ->
        val currentSign = this.eval(x) > 0

        if (currentSign != lastSign) {
            val midX = x - stepSize * 0.5
            approximatedRoots.add(midX)
        }

        lastSign = currentSign
    }

    return approximatedRoots
}
