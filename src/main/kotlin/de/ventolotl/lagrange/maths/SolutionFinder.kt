package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.maths.rootfinder.findRootsNewton
import de.ventolotl.lagrange.utility.Point2d

fun Function3d.optimize(constraint: Constraint): List<Point2d> {
    val solutionFinder = SolutionFinder(this, constraint)
    return solutionFinder.findSolutions()
}

private class SolutionFinder(
    function3d: Function3d,
    private val constraint: Constraint
) {
    private val gradientFunction = function3d.gradient()
    private val gradientConstraint = constraint.equation.gradient()

    fun findSolutions(): List<Vector2d<Double>> {
        return errorFunction().findRootsNewton(constraint.range, 0.1)
    }

    private fun errorFunction(): Function3d {
        return Function3d { x, y ->
            val lambda1 = gradientFunction.x.eval(x, y) / gradientConstraint.x.eval(x, y)
            val lambda2 = gradientFunction.y.eval(x, y) / gradientConstraint.y.eval(x, y)

            val error = lambda1 - lambda2
            error
        }
    }
}
