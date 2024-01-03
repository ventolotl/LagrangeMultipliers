package de.ventolotl.lagrange.utility

import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.maths.distSq

typealias Point2d = Vector2d<Double>

fun List<Point2d>.connectPoints(connector: (point1: Point2d, point2: Point2d) -> Unit) {
    val pointsCopy = this.toMutableList()
    var point = this.firstOrNull() ?: return

    val iterator = pointsCopy.iterator()
    while (iterator.hasNext()) {
        pointsCopy.remove(point)

        val nearestPoint = if (pointsCopy.isEmpty()) {
            this.minBy { point.distSq(it) }
        } else {
            pointsCopy.minBy { point.distSq(it) }
        }

        connector(point, nearestPoint)

        point = nearestPoint
    }
}
