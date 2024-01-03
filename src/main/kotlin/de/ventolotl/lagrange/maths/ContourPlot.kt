package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.DoubleRange
import de.ventolotl.lagrange.utility.DoubleVector2Range
import de.ventolotl.lagrange.utility.Point2d
import kotlin.math.absoluteValue

open class ContourLine(val z: Double, val points: List<Point2d>)

fun Function3d.createContour(
    zRange: DoubleRange,
    zAccuracy: Double,
    pointsRange: DoubleVector2Range,
    pointsStep: Double
): List<ContourLine> {
    val contour = mutableListOf<ContourLine>()
    var iteration = 0

    zRange.iterate(zAccuracy) { z ->
        val correspondingFunction = Function3d { x, y -> eval(x, y) - z }

        val points = correspondingFunction.findRootsNewton(pointsRange, pointsStep)
        contour.add(ContourLine(z, points))

        val completedPct = 100.0 * ++iteration / (zRange.start.absoluteValue + zRange.end.absoluteValue + 1)
        println("$completedPct% at z=$z")
    }

    return contour
}
