package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Vector2dRange
import java.awt.Color

data class Constraint(
    val equation: Function3d,
    val constant: Double,
    val range: Vector2dRange<Double>,
    val accuracy: Int,
    val color: Color = Color.RED
) {
    val rootFunction = Function3d { x, y ->
        equation.eval(x, y) - constant
    }
    val points = calculatePoints()

    private fun calculatePoints(): List<Vector2d<Double>> {
        return rootFunction.findRootsNewton(range, accuracy)
    }
}
