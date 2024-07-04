package de.ventolotl.lagrange.maths

import kotlin.math.absoluteValue

fun Function3d.optimize(constraint: Constraint): List<Vector2d<Double>> {
    val solutionFinder = SolutionFinder(this, constraint)
    return solutionFinder.findSolutions()
}

private class SolutionFinder(
    private val functionToOptimize: Function3d,
    private val constraint: Constraint
) {
    private val constraintEq = constraint.equation

    fun findSolutions(): List<Vector2d<Double>> {
        val functionToOptimizePartialX = functionToOptimize.partialX()
        val functionToOptimizePartialY = functionToOptimize.partialY()

        val constraintPartialX = constraintEq.partialX()
        val constraintPartialY = constraintEq.partialY()

        val roots = constraint.rootFunction
            .findRootsNewton(constraint.range, 3000)
            .filter { (x, y) ->
                val lambda1 = functionToOptimizePartialX.eval(x, y) / constraintPartialX.eval(x, y)
                val lambda2 = functionToOptimizePartialY.eval(x, y) / constraintPartialY.eval(x, y)
                val error = lambda1.absoluteValue - lambda2.absoluteValue
                error * error < 1e-3
            }

        return roots
    }
}
