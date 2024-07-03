package de.ventolotl.lagrange.utility

import de.ventolotl.lagrange.maths.Vector2d

data class Range<T : Comparable<T>>(private val start0: T, private val end0: T) {
    val start = if (start0 < end0) start0 else end0
    val end = if (start0 > end0) start0 else end0

    constructor(range: ClosedRange<T>) : this(range.start, range.endInclusive)

    operator fun contains(value: T): Boolean {
        return value in start..end
    }
}

infix fun <T : Comparable<T>> T.range(other: T): Range<T> {
    return Range(start0 = this, end0 = other)
}

inline fun <reified T> Range<T>.iterate(step: T, iteration: (value: T) -> Unit)
        where T : Comparable<T>,
              T : Number {
    var value = start

    while (value <= end) {
        iteration(value)
        value = value.plus(step)
    }
}

data class Vector2dRange<T : Comparable<T>>(private val start0: Vector2d<T>, private val end0: Vector2d<T>) {
    val startX = if (start0.x < end0.x) start0.x else end0.x
    val endX = if (start0.x > end0.x) start0.x else end0.x
    val rangeX = startX range endX

    val startY = if (start0.y < end0.y) start0.y else end0.y
    val endY = if (start0.y > end0.y) start0.y else end0.y
    val rangeY = startY range endY

    constructor(range1: ClosedRange<T>, range2: ClosedRange<T>) : this(
        Vector2d(range1.start, range2.start),
        Vector2d(range1.endInclusive, range2.endInclusive)
    )

    operator fun contains(point: Vector2d<T>): Boolean {
        return point.x in startX..endX && point.y in startY..endY
    }
}

infix fun <T : Comparable<T>> Vector2d<T>.range(other: Vector2d<T>): Vector2dRange<T> {
    return Vector2dRange(start0 = this, end0 = other)
}

inline fun <reified T> Vector2dRange<T>.iterate(step: T, iteration: (x: T, y: T) -> Unit)
        where T : Comparable<T>,
              T : Number {
    rangeX.iterate(step) { x ->
        rangeY.iterate(step) { y ->
            iteration(x, y)
        }
    }
}
