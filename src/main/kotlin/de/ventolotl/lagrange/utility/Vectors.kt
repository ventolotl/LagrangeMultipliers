package de.ventolotl.lagrange.utility

import kotlin.math.sqrt

/// Vector2d
data class Vector2d<T>(var x: T, var y: T)

fun <T : Number> Vector2d<T>.dist(other: Vector2d<T>): Double {
    return sqrt(this.distSq(other))
}

fun <T : Number> Vector2d<T>.distSq(other: Vector2d<T>): Double {
    val deltaX = (this.x - other.x).toDouble()
    val deltaY = (this.y - other.y).toDouble()
    return deltaX * deltaX + deltaY * deltaY
}

fun Vector2d<Double>.lenSq(): Double {
    return x * x + y * y
}

/// Vector3d
data class Vector3d<T>(var x: T, var y: T, var z: T)

fun <T : Number> Vector3d<T>.dist(other: Vector3d<T>): Double {
    return sqrt(this.distSq(other))
}

fun <T : Number> Vector3d<T>.distSq(other: Vector3d<T>): Double {
    val deltaX = (this.x - other.x).toDouble()
    val deltaY = (this.y - other.y).toDouble()
    val deltaZ = (this.z - other.z).toDouble()
    return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ
}

fun Vector3d<Double>.lenSq(): Double {
    return x * x + y * y + z * z
}