package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.DoubleVector2Range

data class Constraint(
    val equation: Function3d,
    val constant: Double,
    val range: DoubleVector2Range,
    val accuracy: Double
) {
    val points by lazy {
        val correspondingFunction = Function3d { x, y ->
            equation.eval(x, y) - constant
        }
        correspondingFunction.findRootsNewton(range, accuracy)
    }
}
