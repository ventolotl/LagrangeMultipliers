package de.ventolotl.lagrange.maths

fun Function3d.optimize(constraint: Constraint): List<Vector2d<Double>> {
    val solutionFinder = SolutionFinder(this, constraint)
    return solutionFinder.findSolutions()
}

private class SolutionFinder(
    functionToOptimize: Function3d,
    private val constraint: Constraint
) {
    private val constraintEq = constraint.equation

    private val gradientOptimizeFunc = functionToOptimize.gradient()
    private val gradientConstraint = constraint.equation.gradient()

    fun findSolutions(): List<Vector2d<Double>> {
        val roots = gradientErrorFunction().findRootsNewton(constraint.range, 1000)
        return satisfyConstraintCurve(roots)
    }

    private fun satisfyConstraintCurve(roots: List<Vector2d<Double>>): List<Vector2d<Double>> {
        return roots.filter { root ->
            val delta = constraintEq.eval(root.x, root.y) - constraint.constant
            delta * delta < 1e-3
        }
    }

    private fun gradientErrorFunction(): Function3d {
        return Function3d { x, y ->
            val lambda1 = gradientOptimizeFunc.x.eval(x, y) / gradientConstraint.x.eval(x, y)
            val lambda2 = gradientOptimizeFunc.y.eval(x, y) / gradientConstraint.y.eval(x, y)
            val error = lambda1 - lambda2
            error * error
        }
    }
}