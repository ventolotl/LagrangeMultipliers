package de.ventolotl.lagrange.utility

val operations = mapOf(
    Int::class to IntOps,
    Long::class to LongOps,
    Double::class to DoubleOps,
    Float::class to FloatOps,
)

inline fun <reified T> operationForType(): OperationalType<T> {
    val correspondingOperation = operations[T::class] ?: notImplementedOperations
    return correspondingOperation as OperationalType<T>
}

interface OperationalType<T> {
    fun add(a: T, b: T): T
    fun subtract(a: T, b: T): T
    fun multiply(a: T, b: T): T
    fun divide(a: T, b: T): T
}

val notImplementedOperations = object : OperationalType<Any?> {
    override fun add(a: Any?, b: Any?): Any? {
        error("Operation not implemented for $this")
    }

    override fun subtract(a: Any?, b: Any?): Any? {
        error("Operation not implemented for $this")
    }

    override fun multiply(a: Any?, b: Any?): Any? {
        error("Operation not implemented for $this")
    }

    override fun divide(a: Any?, b: Any?): Any? {
        error("Operation not implemented for $this")
    }
}

private object IntOps : OperationalType<Int> {
    override fun add(a: Int, b: Int): Int = a + b
    override fun subtract(a: Int, b: Int): Int = a - b
    override fun multiply(a: Int, b: Int): Int = a * b
    override fun divide(a: Int, b: Int): Int = a / b
}

private object LongOps : OperationalType<Long> {
    override fun add(a: Long, b: Long): Long = a + b
    override fun subtract(a: Long, b: Long): Long = a - b
    override fun multiply(a: Long, b: Long): Long = a * b
    override fun divide(a: Long, b: Long): Long = a / b
}

private object DoubleOps : OperationalType<Double> {
    override fun add(a: Double, b: Double): Double = a + b
    override fun subtract(a: Double, b: Double): Double = a - b
    override fun multiply(a: Double, b: Double): Double = a * b
    override fun divide(a: Double, b: Double): Double = a / b
}

private object FloatOps : OperationalType<Float> {
    override fun add(a: Float, b: Float): Float = a + b
    override fun subtract(a: Float, b: Float): Float = a - b
    override fun multiply(a: Float, b: Float): Float = a * b
    override fun divide(a: Float, b: Float): Float = a / b
}
