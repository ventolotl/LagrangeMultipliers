package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.Vector2d
import org.junit.Test

// This test is meant to provide an implementation for solving a non-linear system of equations
class EquationSystemSolver {
    @Test
    fun test() {
        val f1 = Function3d { x: Double, y: Double ->
            x * x + y * y - 2
        }
        val f2 = Function3d { x: Double, y: Double ->
            x + y
        }

        // The elements of the Jacobian Matrix
        val j11 = f1.partialX()
        val j12 = f1.partialY()
        val j21 = f2.partialX()
        val j22 = f2.partialY()

        val initialGuess = Vector2d(0.0, 0.05)
        var guess = initialGuess

        repeat(10) {
            val delta = iterate(f1, f2, j11, j12, j21, j22, guess)
            if (delta == null) {
                println("System of equations is not solvable")
                return
            }

            guess = Vector2d(guess.x + delta.x, guess.y + delta.y)
            println("guess=$guess")
        }

        println(guess)
        println("f1=${f1.eval(guess)}, f2=${f2.eval(guess)}")
    }

    private fun iterate(
        f1: Function3d,
        f2: Function3d,
        f1PartialX: Function3d,
        f1PartialY: Function3d,
        f2PartialX: Function3d,
        f2PartialY: Function3d,
        initialGuess: Vector2d<Double>
    ): Vector2d<Double>? {
        // Compute negative F
        val F1 = -f1.eval(initialGuess)
        val F2 = -f2.eval(initialGuess)

        // Compute Jacobian
        val valueF1PartialX = f1PartialX.eval(initialGuess)
        val valueF1PartialY = f1PartialY.eval(initialGuess)
        val valueF2PartialX = f2PartialX.eval(initialGuess)
        val valueF2PartialY = f2PartialY.eval(initialGuess)

        // Invert the Jacobian
        val det = valueF1PartialX * valueF2PartialY - valueF1PartialY * valueF2PartialX
        if (det == 0.0) {
            return null
        }
        val detReciprocal = 1.0 / det

        return Vector2d(
            detReciprocal * (valueF2PartialY * F1 - valueF1PartialY * F2),
            detReciprocal * (-valueF2PartialX * F1 + valueF1PartialX * F2)
        )
    }
}