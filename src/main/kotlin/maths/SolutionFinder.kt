package maths

import maths.rootfinder.findRootsNewton
import kotlin.math.abs

fun Function3d.optimize(constraint: Constraint): List<Vector2d<Double>> {
    val solutionFinder = SolutionFinder(this, constraint)
    return solutionFinder.findSolutions()
}

private class SolutionFinder(
    function3d: Function3d,
    private val constraint: Constraint
) {
    private val gradientFunction = function3d.gradient()
    private val gradientConstraint = constraint.equation.gradient()
    private val accuracy = constraint.accuracy

    fun findSolutions(): List<Vector2d<Double>> {
        val points = constraint.points
        val roots = errorFunction().findRootsNewton(constraint.range, accuracy)

        val solutions = mutableListOf<Vector2d<Double>>()

        roots.forEach { root ->
            val nearestPoint = points.minBy { it.distSq(root) }

            if (nearestPoint.dist(root) < accuracy) {
                solutions.add(nearestPoint)
                return@forEach
            }
        }

        return solutions
    }

    private fun errorFunction(): Function3d {
        return Function3d { x, y ->
            val lambda1 = gradientFunction.x.eval(x, y) / gradientConstraint.x.eval(x, y)
            val lambda2 = gradientFunction.y.eval(x, y) / gradientConstraint.y.eval(x, y)
            abs(lambda1 - lambda2)
        }
    }
}
