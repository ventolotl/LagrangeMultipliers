package de.ventolotl.lagrange.utility

import kotlin.math.sqrt

data class Vector2d<T>(var x: T, var y: T)

fun Vector2d<Int>.toDoubleVec(): Vector2d<Double> {
    return Vector2d(x.toDouble(), y.toDouble())
}

fun Vector2d<Double>.dist(other: Vector2d<Double>): Double {
    return sqrt(distSq(other))
}

fun Vector2d<Double>.distSq(other: Vector2d<Double>): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return dx * dx + dy * dy
}

data class Vector3d<T>(var x: T, var y: T, var z: T)

fun Vector3d<Int>.toDoubleVec(): Vector3d<Double> {
    return Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
}

fun Vector3d<Double>.dist(other: Vector3d<Double>): Double {
    return sqrt(distSq(other))
}

fun Vector3d<Double>.distSq(other: Vector3d<Double>): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

