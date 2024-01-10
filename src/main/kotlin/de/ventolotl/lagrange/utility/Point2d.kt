package de.ventolotl.lagrange.utility

import de.ventolotl.lagrange.maths.Vector2d

typealias Point2d = Vector2d<Double>

fun <T> List<Vector2d<T>>.connectPoints(range: Vector2dRange<T>): List<Vector2d<T>>
        where T : Comparable<T>,
              T : Number {
    val result = mutableListOf<Vector2d<T>>()

    println("got=${this.size}")

    val points = this.toMutableList()
    var point = this.firstOrNull() ?: return emptyList()

    var index = 0

    while (index < points.size) {
        val currentPoint = points[index]
        points.removeAt(index)

        val nearestPoint = if (points.isEmpty()) {
            this.minBy { currentPoint.distSq(it) }
        } else {
            points.minBy { currentPoint.distSq(it) }
        }

        result.add(point)
        point = nearestPoint

        index++
    }


    println("result=$result")
    return result
}
