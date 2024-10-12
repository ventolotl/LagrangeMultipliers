package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.*
import kotlin.math.min

data class ContourLine(val z: Double, val points: List<Vector2<Double>>)

fun Function3.createContour(
    contourLinesN: Int,
    pointsRange: Vector2dRange<Double>,
    accuracy: Int = 100
): List<ContourLine> {
    val valuesF = mutableListOf<Double>().apply {
        val iterationStep = 0.01 * min(pointsRange.rangeX.length, pointsRange.rangeY.length)
        pointsRange.iterate(iterationStep) { x, y ->
            this.add(this@createContour.eval(x, y))
        }
    }

    val zRange = valuesF.min() range valuesF.max()
    val zAccuracy = zRange.length / contourLinesN

    return createContour(zRange, zAccuracy, pointsRange, accuracy)
}

fun Function3.createContour(
    zRange: Range<Double>,
    zAccuracy: Double,
    pointsRange: Vector2dRange<Double>,
    accuracy: Int
): List<ContourLine> {
    val contour = mutableListOf<ContourLine>()

    if (zAccuracy < 0.0) {
        error("Error creating contour: zAccuracy must be positive ($zAccuracy)")
    }

    zRange.iterate(zAccuracy) { z ->
        val correspondingFunction = Function3 { x, y -> eval(x, y) - z }

        val points = correspondingFunction.findRootsNewton(pointsRange, accuracy)
        val pointsInRange = points.filter { point ->
            val zValue = this.eval(point.x, point.y)
            point in pointsRange && zValue in zRange
        }

        println("z=$z: points=${points.size}, relevant=${pointsInRange.size}")

        if (pointsInRange.isNotEmpty()) {
            contour.add(ContourLine(z, pointsInRange))
        }
    }

    return contour
}