package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.EquationSolver
import de.ventolotl.lagrange.maths.Function4d
import de.ventolotl.lagrange.utility.Vector
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.test.assertTrue

class EquationSystemSolverTest {
    @Test
    fun test() {
        val f1 = Function4d { x, y, z ->
            x * x + y * y + z * z - 10
        }
        val f2 = Function4d { x, y, z ->
            x - y + z
        }
        val f3 = Function4d { x, y, z ->
            2 * x + 3 * y - 2 * z
        }

        val solver = EquationSolver(f1, f2, f3, 100000)
        val initialGuess = Vector.of(-0.5, 0.5, 0.1)

        val guess = solver.findSolution(initialGuess)

        // Ensures the equations solver found a solution
        assert(guess != null)
        guess!!

        val epsilon = 1e-5
        assertTrue {
            f1.eval(guess).absoluteValue < epsilon
                    && f2.eval(guess).absoluteValue < epsilon
                    && f3.eval(guess).absoluteValue < epsilon
        }
    }
}
