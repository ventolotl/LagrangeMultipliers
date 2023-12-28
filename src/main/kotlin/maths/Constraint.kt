package maths

import utility.DoubleRange

data class Constraint(val equation: Function3d, val constant: Double, val range: DoubleRange, val accuracy: Double) {
    val points by lazy {
        val correspondingFunction = Function3d { x, y ->
            equation.eval(x, y) - constant
        }
        correspondingFunction.findRootsNewton(range, accuracy)
    }
}
