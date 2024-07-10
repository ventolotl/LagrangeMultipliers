package de.ventolotl.lagrange.utility

import kotlin.math.sqrt

fun <T : Number> Vector2d<T>.dist(other: Vector2d<T>): Double {
    val deltaX = (this.x - other.x).toDouble()
    val deltaY = (this.y - other.y).toDouble()
    return sqrt(deltaX * deltaX + deltaY * deltaY)
}

fun <T : Number> Vector2d<T>.distSq(other: Vector2d<T>): Double {
    val deltaX = (this.x - other.x).toDouble()
    val deltaY = (this.y - other.y).toDouble()
    return deltaX * deltaX + deltaY * deltaY
}
