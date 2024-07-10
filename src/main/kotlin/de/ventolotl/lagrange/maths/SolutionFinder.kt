package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.Vector3d
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.iterate

fun Function3d.optimize(constraint: Constraint): List<Vector2d<Double>> {
    val solutionFinder = SolutionFinder(this, constraint)
    return solutionFinder.findSolutions()
}

private class SolutionFinder(
    private val functionToOptimize: Function3d, private val constraint: Constraint
) {
    private val constraintEq = constraint.rootFunction

    fun findSolutions(): List<Vector2d<Double>> {
        val functionToOptimizePartialX = functionToOptimize.partialX()
        val functionToOptimizePartialY = functionToOptimize.partialY()

        val constraintPartialX = constraintEq.partialX()
        val constraintPartialY = constraintEq.partialY()

        val f1 = Function4d { x, y, lambda ->
            functionToOptimizePartialX.eval(x, y) - lambda * constraintPartialX.eval(x, y)
        }
        val f2 = Function4d { x, y, lambda ->
            functionToOptimizePartialY.eval(x, y) - lambda * constraintPartialY.eval(x, y)
        }
        val g = Function4d { x, y, _ -> constraintEq.eval(x, y) }

        val equationSolver = EquationSolver(f1, f2, g, 1000)

        val solutions = mutableListOf<Vector2d<Double>>()

        constraint.range.iterate(0.5) { x, y ->
            val solution = equationSolver.findSolution(Vector3d(x, y, 1.0)) ?: run {
                return@iterate
            }
            val coordinates = Vector2d(solution.x, solution.y)
            val unknown = solutions.none { other ->
                other.distSq(coordinates) < 1e-5
            }

            if (unknown) {
                solutions.add(coordinates)
            }
        }

        return solutions
    }
}
