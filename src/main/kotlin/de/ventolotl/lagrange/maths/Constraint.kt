package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.Point2d
import de.ventolotl.lagrange.utility.Vector2dRange

data class Constraint(
    val equation: Function3d,
    val constant: Double,
    val range: Vector2dRange<Double>,
    val step: Double
) {
    val points = calculatePoints()

    private fun calculatePoints(): List<Point2d> {
        val correspondingFunction = Function3d { x, y ->
            equation.eval(x, y) - constant
        }
        return correspondingFunction.findRootsNewton(range, step)
    }
}
