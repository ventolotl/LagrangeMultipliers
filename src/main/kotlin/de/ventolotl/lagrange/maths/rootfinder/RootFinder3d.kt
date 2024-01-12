package de.ventolotl.lagrange.maths.rootfinder

import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.maths.distSq
import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.iterate

private const val MAX_ITERATIONS = 100000
private const val PRECISION = 1e-9
private const val ROOT_DIFF_MIN = 0.005

fun Function3d.findRootNewton(start: Vector2d<Double>, precision: Double = PRECISION): Vector2d<Double>? {
    return findRootNewton(start.x, start.y, precision)
}

private fun Function3d.findRootNewton(
    startX: Double,
    startY: Double,
    precision: Double = PRECISION
): Vector2d<Double>? {
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
        if (distSq < precision) {
            return Vector2d(rootX, rootY)
        }

        rootX = nextRootX
        rootY = nextRootY
    }

    return null
}

fun Function3d.findRootsNewton(
    range: Vector2dRange<Double>,
    step: Double,
    precision: Double = PRECISION
): List<Vector2d<Double>> {
    val roots = mutableListOf<Vector2d<Double>>()

    range.iterate(step) { x, y ->
        findRootNewton(x, y, precision)?.let { root ->
            roots.add(root)
        }
    }

    return roots.reduceDuplicates()
}

private fun List<Vector2d<Double>>.reduceDuplicates(): List<Vector2d<Double>> {
    val reducedPoints = this.toMutableList()

    var index = 0
    while (index < reducedPoints.size) {
        val point = reducedPoints[index++]
        reducedPoints.removeIf { other ->
            other != point && other.distSq(point) < ROOT_DIFF_MIN
        }
    }

    return reducedPoints
}
