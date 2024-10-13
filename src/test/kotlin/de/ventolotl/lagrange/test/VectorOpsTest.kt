package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.minus
import de.ventolotl.lagrange.utility.plus
import de.ventolotl.lagrange.utility.times
import org.junit.Assert
import org.junit.Test

class VectorOpsTest {
    @Test
    fun testVecAdd() {
        val vec1 = Vector.of(1, 1)
        val vec2 = Vector.of(3, 5)

        val expected = Vector.of(4, 6)
        Assert.assertEquals(expected, vec1 + vec2)
    }

    @Test
    fun testVecSub() {
        val vec1 = Vector.of(1, 1)
        val vec2 = Vector.of(3, 5)

        val expected = Vector.of(-2, -4)
        Assert.assertEquals(expected, vec1 - vec2)
    }

    @Test
    fun testVecMul() {
        val vec1 = Vector.of(3, 2)
        val vec2 = Vector.of(3, 5)

        val expected = Vector.of(9, 10)
        Assert.assertEquals(expected, vec1 * vec2)
    }

    @Test
    fun testScalarMul() {
        val scalar = 2
        val vec = Vector.of(10, 5)

        val expected = Vector.of(20, 10)
        Assert.assertEquals(expected, scalar * vec)
    }

    @Test
    fun testOpNotImplemented() {
        val vec1 = Vector.of(TestObj, TestObj)
        val vec2 = Vector.of(TestObj, TestObj)

        Assert.assertThrows(IllegalStateException::class.java) {
            vec1 + vec2
        }
    }

    object TestObj
}