package de.ventolotl.lagrange.utility

import de.ventolotl.lagrange.maths.Vector2d

typealias Point2d = Vector2d<Double>

fun <T> List<Vector2d<T>>.connectPoints(range: Vector2dRange<T>): List<List<Vector2d<T>>>
        where T : Comparable<T>,
              T : Number {
    val result = mutableListOf<List<Vector2d<T>>>()

    val list = this.toMutableList()
    while (true) {
        val start = list.find { point -> point !in range }
            ?: list.firstOrNull()
            ?: return result

        val points = list.connectPointsFrom(range, start)
        result.add(points)
        list.removeAll(points)
    }
}

private fun <T> List<Vector2d<T>>.connectPointsFrom(
    range: Vector2dRange<T>,
    start: Vector2d<T>
): List<Vector2d<T>>
        where T : Comparable<T>,
              T : Number {
    val input = this.toMutableList()
    val points = mutableListOf<Vector2d<T>>()

    var point = start
    points.add(point)
    input.remove(point)

    var lastDist: Double? = null

    while (input.isNotEmpty()) {
        val nearest = input.minBy { it.distSq(point) }
        val dist = nearest.dist(point)

        val distMultiplier = dist / (lastDist ?: dist)
        if (distMultiplier < 10) {
            points.add(nearest)
            point = nearest
            lastDist = dist
        }
        input.remove(nearest)
    }

    if (start in range) {
        points.add(start)
    }

    return points
}
