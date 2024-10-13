package de.ventolotl.lagrange.test

import de.ventolotl.lagrange.ui.utility.ColorInterpolator.binarySearchForBounds
import kotlin.test.Test
import kotlin.test.assertEquals

class BinarySearchBoundsTest {
    @Test
    fun testBinarySearchBounds() {
        val list = (0..10).toList()

        val i = 25.0
        val result = list.binarySearchForBounds(i) { it * 10.0 }

        assertEquals(2, result)
    }

    @Test
    fun testBinarySearchBoundsBelow() {
        val list = (0..10).toList()

        val i = -1.0
        val result = list.binarySearchForBounds(i) { it * 10.0 }

        assertEquals(0, result)
    }

    @Test
    fun testBinarySearchBoundsAbove() {
        val list = (0..10).toList()

        val i = 200.0
        val result = list.binarySearchForBounds(i) { it * 10.0 }

        assertEquals(10, result)
    }
}