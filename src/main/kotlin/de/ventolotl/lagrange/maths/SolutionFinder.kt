package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.Vector3d
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.iterate

fun Function3d.optimize(constraint: Constraint): List<Vector2d<Double>> {
    val constraintEq = constraint.rootFunction

    val functionToOptimizePartialX = this.partialX()
    val functionToOptimizePartialY = this.partialY()

    val constraintPartialX = constraintEq.partialX()
    val constraintPartialY = constraintEq.partialY()

    val f1 = Function4d { x, y, lambda ->
        functionToOptimizePartialX.eval(x, y) - lambda * constraintPartialX.eval(x, y)
    }
    val f2 = Function4d { x, y, lambda ->
        functionToOptimizePartialY.eval(x, y) - lambda * constraintPartialY.eval(x, y)
    }
    val g = Function4d { x, y, _ -> constraintEq.eval(x, y) }

    val equationSolver = EquationSolver(f1, f2, g)

    val solutions = mutableListOf<Vector3d<Double>>()

    constraint.range.iterate(0.05) { x, y ->
        equationSolver.findSolution(Vector3d(x, y, 1.0))?.let { solution ->
            val unknown = solutions.none { other ->
                other.distSq(solution) < 0.01
            }

            if (unknown) {
                solutions.add(solution)
            }
        }
    }

    return solutions.map { (x, y, _) -> Vector2d(x, y) }
}
