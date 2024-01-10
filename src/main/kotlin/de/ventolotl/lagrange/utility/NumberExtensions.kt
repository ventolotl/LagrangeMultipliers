package de.ventolotl.lagrange.utility

import kotlin.math.pow

// Thanks, Kotlin, for not implementing this in the language

infix operator fun <T : Number> T.plus(other: T): T {
    return when (this) {
        is Byte -> this as Byte + other as Byte
        is Short -> this as Short + other as Short
        is Int -> this as Int + other as Int
        is Long -> this as Long + other as Long
        is Double -> this as Double + other as Double
        is Float -> this as Float + other as Float
        else -> error("Unknown type")
    } as T
}

infix operator fun <T : Number> T.minus(other: T): T {
    return when (this) {
        is Byte -> this as Byte - other as Byte
        is Short -> this as Short - other as Short
        is Int -> this as Int - other as Int
        is Long -> this as Long - other as Long
        is Double -> this as Double - other as Double
        is Float -> this as Float - other as Float
        else -> error("Unknown type")
    } as T
}

infix operator fun <T : Number> T.times(other: T): T {
    return when (this) {
        is Byte -> this as Byte * other as Byte
        is Short -> this as Short * other as Short
        is Int -> this as Int * other as Int
        is Long -> this as Long * other as Long
        is Double -> this as Double * other as Double
        is Float -> this as Float * other as Float
        else -> error("Unknown type")
    } as T
}

infix operator fun <T : Number> T.div(other: T): T {
    return when (this) {
        is Byte -> this as Byte / other as Byte
        is Short -> this as Short / other as Short
        is Int -> this as Int / other as Int
        is Long -> this as Long / other as Long
        is Double -> this as Double / other as Double
        is Float -> this as Float / other as Float
        else -> error("Unknown type")
    } as T
}

infix fun <T : Number> T.pow(exponent: T): Double {
    return this.toDouble().pow(exponent.toDouble())
}
