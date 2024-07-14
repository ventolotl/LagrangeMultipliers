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

    val coordinatesScaling = 0.05

    val f1 = Function4d { x, y, lambda ->
        val functionPartialXEval = functionToOptimizePartialX.eval(
            coordinatesScaling * x,
            coordinatesScaling * y
        )
        val constraintPartialXEval = constraintPartialX.eval(
            coordinatesScaling * x,
            coordinatesScaling * y
        )
        functionPartialXEval - lambda * constraintPartialXEval
    }
    val f2 = Function4d { x, y, lambda ->
        val functionPartialYEval = functionToOptimizePartialY.eval(
            coordinatesScaling * x,
            coordinatesScaling * y
        )
        val constraintPartialYEval = constraintPartialY.eval(
            coordinatesScaling * x,
            coordinatesScaling * y
        )
        functionPartialYEval - lambda * constraintPartialYEval
    }
    val g = Function4d { x, y, _ ->
        constraintEq.eval(coordinatesScaling * x, coordinatesScaling * y)
    }

    val equationSolver = EquationSolver(f1, f2, g)

    val coordinates = mutableListOf<Vector2d<Double>>()

    constraint.range.iterate(0.1) { x, y ->
        equationSolver.findSolution(Vector3d(x, y, 1.0))?.let { solution ->
            val coordinate = Vector2d(
                solution.x * coordinatesScaling,
                solution.y * coordinatesScaling
            )

            val unknown = coordinates.none { other ->
                other.distSq(coordinate) < 0.05
            }
            val inRange = coordinate in constraint.range

            if (unknown && inRange) {
                coordinates.add(coordinate)
            }
        }
    }

    println("solutions=${coordinates.size}")

    return coordinates
}
