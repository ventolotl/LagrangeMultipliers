package de.ventolotl.lagrange.maths

import de.ventolotl.lagrange.utility.Vector2d
import de.ventolotl.lagrange.utility.Vector3d

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

fun interface Function3d {
    fun eval(x: Double, y: Double): Double

    fun eval(vector2d: Vector2d<Double>): Double {
        return eval(vector2d.x, vector2d.y)
    }

    fun evalX(x: Double): Function2d {
        return Function2d { y -> eval(x, y) }
    }

    fun evalY(y: Double): Function2d {
        return Function2d { x -> eval(x, y) }
    }

    fun partialX(): Function3d {
        return Function3d { x, y ->
            val df = eval(x + EPSILON, y) - eval(x, y)
            val dx = EPSILON
            df / dx
        }
    }

    fun partialY(): Function3d {
        return Function3d { x, y ->
            val df = eval(x, y + EPSILON) - eval(x, y)
            val dy = EPSILON
            df / dy
        }
    }

    fun gradient(): Vector2d<Function3d> {
        return Vector2d(partialX(), partialY())
    }
}

fun interface Function4d {
    fun eval(x: Double, y: Double, z: Double): Double

    fun eval(vector3d: Vector3d<Double>): Double {
        return eval(vector3d.x, vector3d.y, vector3d.z)
    }

    fun evalX(x: Double): Function3d {
        return Function3d { y, z -> eval(x, y, z) }
    }

    fun evalY(y: Double): Function3d {
        return Function3d { x, z -> eval(x, y, z) }
    }

    fun evalZ(z: Double): Function3d {
        return Function3d { x, y -> eval(x, y, z) }
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
            val dx = EPSILON
            df / dx
        }
    }

    fun partialZ(): Function4d {
        return Function4d { x, y, z ->
            val df = eval(x, y, z + EPSILON) - eval(x, y, z)
            val dx = EPSILON
            df / dx
        }
    }

    fun gradient(): Vector3d<Function4d> {
        return Vector3d(partialX(), partialY(), partialZ())
    }
}
