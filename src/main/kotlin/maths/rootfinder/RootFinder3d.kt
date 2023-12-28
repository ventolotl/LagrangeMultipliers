package maths.rootfinder

import maths.Function3d
import maths.Vector2d
import maths.distSq
import utility.DoubleVector2Range

private const val MAX_ITERATIONS = 10

fun Function3d.findRootNewton(start: Vector2d<Double>, step: Double): Vector2d<Double>? {
    return findRootNewton(start.x, start.y, step)
}

private fun Function3d.findRootNewton(startX: Double, startY: Double, step: Double): Vector2d<Double>? {
    val stepSq = step * step

    var rootX = startX
    var rootY = startY

    val (partialDerivativeX, partialDerivativeY) = gradient()

    repeat(MAX_ITERATIONS) {
        val z = eval(rootX, rootY)
        val xChange = partialDerivativeX.eval(rootX, rootY)
        val yChange = partialDerivativeY.eval(rootX, rootY)

        val gradientLengthSq = xChange * xChange + yChange * yChange

        val nextRootX = rootX - z * xChange / gradientLengthSq
        val nextRootY = rootY - z * yChange / gradientLengthSq

        val deltaX = rootX - nextRootX
        val deltaY = rootY - nextRootY
        val distSq = deltaX * deltaX + deltaY * deltaY
        if (distSq < stepSq) {
            return Vector2d(rootX, rootY)
        }

        rootX = nextRootX
        rootY = nextRootY
    }

    return null
}

fun Function3d.findRootsNewton(range: DoubleVector2Range, step: Double): List<Vector2d<Double>> {
    val stepSq = step * step
    val distinctRoots = mutableListOf<Vector2d<Double>>()

    range.iterate(step) { x, y ->
        findRootNewton(x, y, step)?.let { root ->
            val unknownRoot = distinctRoots.none { otherRoot ->
                otherRoot.distSq(root) < stepSq
            }

            if (unknownRoot) {
                distinctRoots.add(root)
            }
        }
    }

    return distinctRoots
}
