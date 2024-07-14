package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.optimize
import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.distSq
import de.ventolotl.lagrange.utility.range
import org.junit.Test
import kotlin.math.sqrt

class NearestPointTest {
    @Test
    fun test() {
        val g = Function3d { x, y -> x * y - 2 }
        val p = Vector2d(2.0, 2.0)

        val dFunc = Function3d { x, y ->
            val deltaX = x - p.x
            val deltaY = y - p.y
            deltaX * deltaX + deltaY * deltaY
        }

        val areaLength = 10.0
        val pointsRange = Vector2d(-areaLength, -areaLength) range Vector2d(areaLength, areaLength)

        val shortestDist = dFunc.optimize(g, pointsRange, 1.0).minBy {
            solution -> solution.distSq(p)
        }
        println(sqrt(2.0))
        println(shortestDist)
    }
}

