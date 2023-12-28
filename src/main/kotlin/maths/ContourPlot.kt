package maths

import maths.rootfinder.findRootsNewton
import utility.DoubleRange
import utility.DoubleVector2Range
import kotlin.math.absoluteValue
import kotlin.system.measureNanoTime

open class ContourLine(val z: Double, val points: List<Vector2d<Double>>)

fun Function3d.createContour(
    zRange: DoubleRange,
    zAccuracy: Double,
    pointsRange: DoubleVector2Range,
    pointsAccuracy: Double
): List<ContourLine> {
    val contour = mutableListOf<ContourLine>()
    var iteration = 0

    measureNanoTime {
        zRange.iterate(zAccuracy) { z ->
            val completedPct = 100.0 * iteration / (zRange.start.absoluteValue + zRange.end.absoluteValue)

            iteration++

            val correspondingFunction = Function3d { x, y -> eval(x, y) - z }

            val points = correspondingFunction.findRootsNewton(pointsRange, pointsAccuracy)
            contour.add(ContourLine(z, points))

            println("$completedPct% at z=$z")
        }
    }.also {
        println("Took: ${it / 1_000_000.0}ms")
    }

    return contour
}
