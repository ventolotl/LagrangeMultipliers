package de.ventolotl.lagrange

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.maths.createContour
import de.ventolotl.lagrange.ui.ContourConstraint
import de.ventolotl.lagrange.ui.mapToColors
import de.ventolotl.lagrange.utility.range
import java.awt.Color
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants
import kotlin.math.*

private val colors = (255 downTo 0 step 10).map {
    Color(it / 8, 255 - it, it).brighter()
}.toTypedArray()

fun main() {
    val zRange = -15.0 range 1.0
    val zAccuracy = 0.5
    val pointsRange = Vector2d(-3.0, -3.0) range Vector2d(3.0, 3.0)
    val step = 0.05

    val functionToOptimize = Function3d { x, y ->
        1 - x * x - y * y
    }

    val constraintEq = Function3d { x, y ->
        x * y
    }
    val constraintValue = 2.0

    val constraint = Constraint(
        equation = constraintEq,
        constant = constraintValue,
        range = pointsRange,
        step = step
    )
    val contour = functionToOptimize.createContour(
        zRange = zRange,
        zAccuracy = zAccuracy,
        pointsRange = pointsRange,
        pointsStep = step
    ).mapToColors(colors)

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val window = JFrame("Lagrange Multipliers")
    val contourConstraint = ContourConstraint(
        function3d = functionToOptimize,
        constraint = constraint,
        contourLines = contour,
        scalingFactor = max(
            max(pointsRange.startX.absoluteValue, pointsRange.startY.absoluteValue),
            max(pointsRange.endX.absoluteValue, pointsRange.endY.absoluteValue)
        ).nextUp().toInt()
    )

    println("total points: ${contour.sumOf { it.points.size }}")

    window.add(contourConstraint)
    window.apply {
        pack()
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(1000, 1000)
        isVisible = true
    }
}
