package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector2
import de.ventolotl.lagrange.utility.Vector3

const val EPSILON = 1e-5

fun interface Function2d {
    fun eval(x: Double): Double

    fun differentiate(): Function2d {
        return Function2d { x ->
            val dy = eval(x + EPSILON) - eval(x)
            val dx = EPSILON
            dy / dx
        }
    }
}

fun interface Function3 {
    fun eval(x: Double, y: Double): Double

    fun eval(vector2: Vector2<Double>): Double {
        return eval(vector2.x, vector2.y)
    }

    fun evalX(x: Double): Function2d {
        return Function2d { y -> eval(x, y) }
    }

    fun evalY(y: Double): Function2d {
        return Function2d { x -> eval(x, y) }
    }

    fun partialX(): Function3 {
        return Function3 { x, y ->
            val df = eval(x + EPSILON, y) - eval(x, y)
            val dx = EPSILON
            df / dx
        }
    }

    fun partialY(): Function3 {
        return Function3 { x, y ->
            val df = eval(x, y + EPSILON) - eval(x, y)
            val dy = EPSILON
            df / dy
        }
    }

    fun gradient(): Vector2<Function3> {
        return Vector.of(partialX(), partialY())
    }
}

fun interface Function4d {
    fun eval(x: Double, y: Double, z: Double): Double

    fun eval(vector3: Vector3<Double>): Double {
        return eval(vector3.x, vector3.y, vector3.z)
    }

    fun evalX(x: Double): Function3 {
        return Function3 { y, z -> eval(x, y, z) }
    }

    fun evalY(y: Double): Function3 {
        return Function3 { x, z -> eval(x, y, z) }
    }

    fun evalZ(z: Double): Function3 {
        return Function3 { x, y -> eval(x, y, z) }
    }

    fun partialX(): Function4d {
        return Function4d { x, y, z ->
            val df = eval(x + EPSILON, y, z) - eval(x, y, z)
            val dx = EPSILON
            df / dx
        }
    }

    fun partialY(): Function4d {
        return Function4d { x, y, z ->
            val df = eval(x, y + EPSILON, z) - eval(x, y, z)
            val dy = EPSILON
            df / dy
        }
    }

    fun partialZ(): Function4d {
        return Function4d { x, y, z ->
            val df = eval(x, y, z + EPSILON) - eval(x, y, z)
            val dz = EPSILON
            df / dz
        }
    }

    fun gradient(): Vector3<Function4d> {
        return Vector.of(partialX(), partialY(), partialZ())
    }
}
