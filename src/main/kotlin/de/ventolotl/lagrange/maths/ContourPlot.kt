package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.Range
import de.ventolotl.lagrange.utility.Vector2dRange
import de.ventolotl.lagrange.utility.iterate

open class ContourLine(val z: Double, val points: List<Vector2d<Double>>)

fun Function3d.createContour(
    zRange: Range<Double>,
    zAccuracy: Double,
    pointsRange: Vector2dRange<Double>,
    pointsStep: Double
): List<ContourLine> {
    val contour = mutableListOf<ContourLine>()

    zRange.iterate(zAccuracy) { z ->
        val correspondingFunction = Function3d { x, y -> eval(x, y) - z }

        val points = correspondingFunction.findRootsNewton(pointsRange, pointsStep)
        contour.add(ContourLine(z, points))
    }

    return contour
}

