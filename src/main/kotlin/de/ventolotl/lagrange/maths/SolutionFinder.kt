package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.*
import kotlin.math.min

private const val MAX_ITERATIONS = 500000

fun Function3.optimize(
    constraintEq: Function3,
    range: Vector2dRange<Double>,
    accuracy: Int = 1000,
    maxIterations: Int = MAX_ITERATIONS
): List<Vector2<Double>> {
    val functionToOptimizePartialX = this.partialX()
    val functionToOptimizePartialY = this.partialY()

    val constraintPartialX = constraintEq.partialX()
    val constraintPartialY = constraintEq.partialY()

    val f1 = Function4d { x, y, lambda ->
        val functionPartialXEval = functionToOptimizePartialX.eval(x, y)
        val constraintPartialXEval = constraintPartialX.eval(x, y)
        functionPartialXEval + lambda * constraintPartialXEval
    }
    val f2 = Function4d { x, y, lambda ->
        val functionPartialYEval = functionToOptimizePartialY.eval(x, y)
        val constraintPartialYEval = constraintPartialY.eval(x, y)
        functionPartialYEval + lambda * constraintPartialYEval
    }
    val g = Function4d { x, y, _ ->
        constraintEq.eval(x, y)
    }

    val equationSolver = EquationSolver(f1, f2, g, maxIterations)
    val coordinates = mutableListOf<Vector2<Double>>()

    val stepSize = min(range.rangeX.length, range.rangeY.length) / accuracy.toDouble()
    val distinctSolDist = stepSize / 100.0

    range.iterate(stepSize) { x, y ->
        equationSolver.findSolution(Vector.of(x, y, 1.0))?.let { solution ->
            val coordinate = Vector.of(solution.x, solution.y)

            val unknown = coordinates.none { other ->
                other.distSq(coordinate) < distinctSolDist
            }
            val inRange = coordinate in range

            if (unknown && inRange) {
                coordinates.add(coordinate)
            }
        }
    }

    return coordinates
}

// This equation solver can solve a non-linear system of 3 equations with 3 unknowns
class EquationSolver(
    private val f1: Function4d,
    private val f2: Function4d,
    private val f3: Function4d,
    private var maxIterations: Int
) {
    private val f1PartialX = f1.partialX()
    private val f1PartialY = f1.partialY()
    private val f1PartialZ = f1.partialZ()
    private val f2PartialX = f2.partialX()
    private val f2PartialY = f2.partialY()
    private val f2PartialZ = f2.partialZ()
    private val f3PartialX = f3.partialX()
    private val f3PartialY = f3.partialY()
    private val f3PartialZ = f3.partialZ()

    fun findSolution(initialGuess: Vector3<Double>): Vector3<Double>? {
        var guess = initialGuess

        while (maxIterations-- > 0) {
            val delta = iterate(guess) ?: return null
            val newGuess = guess + delta

            // Solution already close enough
            if (delta.lenSq() < 1e-5) {
                return newGuess
            }

            guess = newGuess
        }

        return null
    }

    private fun iterate(guess: Vector3<Double>): Vector3<Double>? {
        // Compute Jacobian
        val j11 = f1PartialX.eval(guess)
        val j12 = f1PartialY.eval(guess)
        val j13 = f1PartialZ.eval(guess)
        val j21 = f2PartialX.eval(guess)
        val j22 = f2PartialY.eval(guess)
        val j23 = f2PartialZ.eval(guess)
        val j31 = f3PartialX.eval(guess)
        val j32 = f3PartialY.eval(guess)
        val j33 = f3PartialZ.eval(guess)

        // Invert the Jacobian
        // 1) Calculate the determinant
        val det1 = j11 * (j22 * j33 - j23 * j32)
        val det2 = -j12 * (j21 * j33 - j31 * j23)
        val det3 = j13 * (j21 * j32 - j22 * j31)

        val det = det1 + det2 + det3
        if (det == 0.0) {
            return null
        }
        val detReciprocal = 1.0 / det

        // 2) Calculate the elements
        val inverse11 = j22 * j33 - j23 * j32
        val inverse12 = -j12 * j33 + j13 * j32
        val inverse13 = j12 * j23 - j13 * j22
        val inverse21 = -j21 * j33 + j23 * j31
        val inverse22 = j11 * j33 - j13 * j31
        val inverse23 = -j11 * j23 + j13 * j21
        val inverse31 = j21 * j32 - j22 * j31
        val inverse32 = -j11 * j32 + j12 * j31
        val inverse33 = j11 * j22 - j12 * j21

        // Compute negative F
        val f1 = -f1.eval(guess)
        val f2 = -f2.eval(guess)
        val f3 = -f3.eval(guess)

        // Multiply the inverse matrix (det * elements) and the vector -F
        return Vector.of(
            detReciprocal * (inverse11 * f1 + inverse12 * f2 + inverse13 * f3),
            detReciprocal * (inverse21 * f1 + inverse22 * f2 + inverse23 * f3),
            detReciprocal * (inverse31 * f1 + inverse32 * f2 + inverse33 * f3)
        )
    }
}