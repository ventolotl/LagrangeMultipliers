package utility

data class DoubleRange(val start: Double, val end: Double) {
    inline fun iterate(accuracy: Double, iteration: (value: Double) -> Unit) {
        var value = start

        while (value <= end) {
            iteration(value)
            value += accuracy
        }
    }

    fun includes(value: Double): Boolean {
        return value in start..end
    }
}

infix fun Double.range(other: Double): DoubleRange {
    return DoubleRange(this, other)
}
