package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.Function2d
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.utility.Range
import de.ventolotl.lagrange.utility.iterate
import de.ventolotl.lagrange.utility.range
import org.junit.Test
import kotlin.math.absoluteValue

class RootFindingTest {
    @Test
    fun testRootFinding2d() {
        val radius = 4
        val function = Function3d { x, y ->
            x * x + y * y - radius * radius
        }

        println("start")

        // Roots of function give circle with radius 4

        val projX = function.evalX(0.0)
        val range = -10.0 range 10.0

        rootsFinder2d(projX, range).forEach { root ->
            println("rootY at $root")
        }
    }

    private fun rootsFinder2d(function: Function2d, range: Range<Double>): List<Double> {
        val roots = mutableListOf<Double>()
        val approximatedRoots = approximatedRoots(function, range)

        approximatedRoots.forEach { initialGuess ->
            rootFinder2dExact(function, initialGuess)?.let { root ->
                roots.add(root)
            }
        }

        return roots
    }

    private fun rootFinder2dExact(
        function: Function2d,
        guess: Double,
        iterations: Int = 50
    ): Double? {
        var currentX = guess

        val derivative = function.differentiate()

        repeat(iterations) {
            val yValue = function.eval(currentX)

            if (yValue.absoluteValue < 1e-5) {
                return currentX
            }

            currentX -= yValue / derivative.eval(currentX)
        }

        return null
    }

    private fun approximatedRoots(
        function: Function2d,
        range: Range<Double>,
        accuracy: Int = 100
    ): List<Double> {
        // We apply the intermediate value theorem to isolate possible roots of our single-valued function

        val approximatedRoots = mutableListOf<Double>()

        val totalRange = range.end - range.start
        val stepSize = totalRange / accuracy.toDouble()

        var lastSign = function.eval(range.start) > 0

        range.iterate(stepSize) { x ->
            val currentSign = function.eval(x) > 0

            if (currentSign != lastSign) {
                val average = x - stepSize * 0.5
                approximatedRoots.add(average)
            }

            lastSign = currentSign
        }

        return approximatedRoots
    }
}
