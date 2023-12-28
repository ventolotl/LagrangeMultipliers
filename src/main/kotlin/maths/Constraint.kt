package maths

import maths.rootfinder.findRootsNewton
import utility.DoubleVector2Range

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
