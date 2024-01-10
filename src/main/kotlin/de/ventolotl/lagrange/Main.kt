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
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.WindowConstants
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.nextUp


private val colors = (255 downTo 0 step 25).map {
    Color(it / 5, 255 - it, it).brighter()
}.toTypedArray()

fun main() {
    val zRange = -15.0 range 1.0
    val zAccuracy = 1.0
    val pointsRange = Vector2d(-3.0, -3.0) range Vector2d(3.0, 3.0)
    val step = 0.09

    val functionToOptimize = Function3d { x, y ->
        1 - x * x - y * y
    }
    val constraint = Constraint(
        equation = { x, y -> x * y },
        constant = 2.0,
        range = pointsRange,
        step= step
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
