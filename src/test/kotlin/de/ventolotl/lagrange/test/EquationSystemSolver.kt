package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.Vector2d
import org.junit.Test

// This test is meant to provide an implementation for solving a non-linear system of equations
class EquationSystemSolver {
    @Test
    fun test() {
        val f1 = Function3d { x: Double, y: Double ->
            x * x + y * y - 4
        }
        val f2 = Function3d { x: Double, y: Double ->
            x * x * x - y
        }

        // The elements of the Jacobian Matrix
        val j11 = f1.partialX()
        val j12 = f1.partialY()
        val j21 = f2.partialX()
        val j22 = f2.partialY()

        val initialGuess = Vector2d(1.0, 1.0)

        // Compute negative F
        val F1 = -f1.eval(initialGuess)
        val F2 = -f2.eval(initialGuess)

        println("F: <$F1, $F2>")

        // Compute Jacobian
        val J11 = j11.eval(initialGuess)
        val J12 = j12.eval(initialGuess)
        val J21 = j21.eval(initialGuess)
        val J22 = j22.eval(initialGuess)

        println("Jacobian:")
        println("($J11, $J12)")
        println("($J21, $J22)")

        val det = J11 * J22 - J12 * J21
        val detReciprocal = 1.0 / det

        val inverseJ11 = detReciprocal * J22
        val inverseJ12 = -detReciprocal * J12
        val inverseJ21 = -detReciprocal * J21
        val inverseJ22 = detReciprocal * J11

        val deltaX = detReciprocal * (inverseJ11 * F1 - inverseJ12 * F2)
        val deltaY = detReciprocal * (inverseJ21 * F1 - inverseJ22 * F2)

        println("det=$det * 8")
        println("inverse:")
        println("(${inverseJ11} , $inverseJ12)")
        println("($inverseJ21, $inverseJ22)")

        println("$deltaX, $deltaY")
        println("expected ${1.0 / 4.0}, ")
    }
}