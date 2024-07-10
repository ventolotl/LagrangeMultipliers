package de.ventolotl.lagrange.maths

const val EPSILON = 1e-5

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
