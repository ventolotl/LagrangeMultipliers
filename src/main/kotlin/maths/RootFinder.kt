package maths

import utility.DoubleRange
import kotlin.math.abs

fun Function3d.findRootsNewton(range: DoubleRange, accuracy: Double): List<Vector2d<Double>> {
    val distinctRoots = mutableListOf<Vector2d<Double>>()
    val accuracySq = accuracy * accuracy

    fun trackRoot(root: Vector2d<Double>) {
        val unknownRoot = distinctRoots.none { otherRoot ->
            otherRoot.distSq(root) < accuracySq
        }

        if (unknownRoot) {
            distinctRoots.add(root)
        }
    }

    range.iterate(accuracy) { x ->
        val function2dX = evalX(x)
        val function2dY = evalY(x)
        val function2dXPrime = function2dX.differentiate()
        val function2dYPrime = function2dY.differentiate()

        range.iterate(accuracy) { start ->
            val newtonRange = DoubleRange(start, range.end)

            function2dX.findRootNewton(function2dXPrime, newtonRange, accuracy)?.let { valueY ->
                trackRoot(Vector2d(x, valueY))
            }

            function2dY.findRootNewton(function2dYPrime, newtonRange, accuracy)?.let { valueX ->
                trackRoot(Vector2d(valueX, x))
            }
        }
    }

    return distinctRoots
}

fun Function3d.findRootNewton(range: DoubleRange, accuracy: Double): Vector2d<Double>? {
    range.iterate(accuracy) { x ->
        val function2d = evalX(x)

        function2d.findRootNewton(range, accuracy)?.let { root ->
            return Vector2d(x, root)
        }
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
