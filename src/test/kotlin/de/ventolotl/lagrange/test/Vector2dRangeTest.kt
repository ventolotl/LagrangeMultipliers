package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.utility.Vector2dRange
import org.junit.Assert
import kotlin.test.Test

class Vector2dRangeTest {
    @Test
    fun testInside() {
        val field = Vector2dRange(0..10, 0..10)

        (0..10).forEach { x ->
            (0..10).forEach { y ->
                val point = Vector2d(x, y)
                Assert.assertTrue(point in field)
            }
        }
    }

    @Test
    fun testOutside() {
        val field = Vector2dRange(0..10, 0..10)

        val point = Vector2d(11, -3)
        Assert.assertFalse(point in field)
    }
}

