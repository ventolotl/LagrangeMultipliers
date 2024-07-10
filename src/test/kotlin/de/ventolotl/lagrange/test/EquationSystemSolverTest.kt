package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.EquationSolver
import de.ventolotl.lagrange.maths.Function4d
import de.ventolotl.lagrange.utility.Vector3d
import org.junit.Test

// This test is meant to provide an implementation for solving a non-linear system of equations
class EquationSystemSolverTest {
    @Test
    fun test() {
        val f1 = Function4d { x, y, z ->
            2 * x + y + 3 * z * z
        }
        val f2 = Function4d { x, y, z ->
            -x + 3 * y - z + 5
        }
        val f3 = Function4d { x, y, z ->
            x + y + z
        }

        val solver = EquationSolver(f1, f2, f3, 100000)
        val initialGuess = Vector3d(-0.5, 0.5, 0.1)

        val guess = solver.findSolution(initialGuess)
            ?: run {
                println("System of equations has no solution")
                return
            }

        println(guess)
        println("f1=${f1.eval(guess)}, f2=${f2.eval(guess)}, f3=${f3.eval(guess)}")
    }
}
