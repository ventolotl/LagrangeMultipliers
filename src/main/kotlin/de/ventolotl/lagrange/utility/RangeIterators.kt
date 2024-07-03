package de.ventolotl.lagrange.utility

inline fun <reified T> Range<T>.iterate(step: T, iteration: (value: T) -> Unit)
        where T : Comparable<T>,
              T : Number {
    var value = start

    while (value <= end) {
        iteration(value)
        value = value.plus(step)
    }
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
