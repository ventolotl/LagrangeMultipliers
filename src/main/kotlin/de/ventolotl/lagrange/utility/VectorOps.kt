package de.ventolotl.lagrange.utility

// Vector2 operations

operator fun <T> Vector2<T>.plus(other: Vector2<T>): Vector2<T> =
    vectorElementsTransform(this, other, OperationalType<T>::add)

operator fun <T> Vector2<T>.minus(other: Vector2<T>): Vector2<T> =
    vectorElementsTransform(this, other, OperationalType<T>::subtract)

operator fun <T> Vector2<T>.times(other: Vector2<T>): Vector2<T> =
    vectorElementsTransform(this, other, OperationalType<T>::multiply)

operator fun <T> T.times(other: Vector2<T>): Vector2<T> {
    return Vector2(this, this, other.operationalType) * other
}

private inline fun <T> vectorElementsTransform(
    a: Vector2<T>,
    b: Vector2<T>,
    transform: OperationalType<T>.(T, T) -> T
): Vector2<T> {
    return Vector2(
        transform(a.operationalType, a.x, b.x),
        transform(a.operationalType, a.y, b.y),
        a.operationalType
    )
}

// Vector3 operations

operator fun <T> Vector3<T>.plus(other: Vector3<T>): Vector3<T> =
    vectorElementsTransform(this, other, OperationalType<T>::add)

operator fun <T> Vector3<T>.minus(other: Vector3<T>): Vector3<T> =
    vectorElementsTransform(this, other, OperationalType<T>::subtract)

operator fun <T> Vector3<T>.times(other: Vector3<T>): Vector3<T> =
    vectorElementsTransform(this, other, OperationalType<T>::multiply)

operator fun <T> T.times(other: Vector3<T>): Vector3<T> {
    return Vector3(this, this, this, other.operationalType) * other
}

private inline fun <T> vectorElementsTransform(
    a: Vector3<T>,
    b: Vector3<T>,
    transform: OperationalType<T>.(T, T) -> T
): Vector3<T> {
    return Vector3(
        transform(a.operationalType, a.x, b.x),
        transform(a.operationalType, a.y, b.y),
        transform(a.operationalType, a.z, b.z),
        a.operationalType
    )
}
