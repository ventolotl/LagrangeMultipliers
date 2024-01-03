package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.DoubleVector2Range
import de.ventolotl.lagrange.utility.Point2d

data class Constraint(
    val equation: Function3d,
    val constant: Double,
    val range: DoubleVector2Range,
    val step: Double
) {
    val points = calculatePoints()

    private fun calculatePoints(): List<Point2d> {
        val correspondingFunction = Function3d { x, y ->
            equation.eval(x, y) - constant
        }
        println("Finding points")
        return correspondingFunction.findRootsNewton(range, step).also {
            println("Finished")
        }
    }
}
