package de.ventolotl.lagrange.utility

object Vector {
    inline fun <reified T> of(x: T, y: T): Vector2<T> {
        return Vector2(x, y, operationForType<T>())
    }

    inline fun <reified T> of(x: T, y: T, z: T): Vector3<T> {
        return Vector3(x, y, z, operationForType<T>())
    }
}

data class Vector2<T>(val x: T, val y: T, val operationalType: OperationalType<T>) {
    override fun toString(): String {
        return "Vector2(x=$x, y=$y)"
    }
}

data class Vector3<T>(val x: T, val y: T, val z: T, val operationalType: OperationalType<T>) {
    override fun toString(): String {
        return "Vector3(x=$x, y=$y, z=$z)"
    }
}

fun Vector2<Double>.distSq(other: Vector2<Double>): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return dx * dx + dy * dy
}

fun <T> Vector3<T>.lenSq(): T {
    return operationalType.add(
        operationalType.multiply(x, x),
        operationalType.add(operationalType.multiply(y, y), operationalType.multiply(z, z))
    )
}
