import maths.Constraint
import maths.Function3d
import maths.Vector2d
import maths.createContour
import ui.ContourConstraint
import ui.mapToColors
import utility.range
import java.awt.Color
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.nextUp

private val colors = (255 downTo 0 step 25).map {
    Color(it / 5, 255 - it, it).brighter()
}.toTypedArray()

/**
 *
 */
fun main() {
    val zRange = -5.0 range 1.0
    val zAccuracy = 1.0
    val pointsRange = Vector2d(-3.0, -3.0) range Vector2d(3.0, 3.0)
    val accuracy = 0.007

    val functionToOptimize = Function3d { x, y ->
        1 - x - y * y
    }
    val constraint = Constraint(
        equation = { x, y -> x * y },
        constant = 2.0,
        range = pointsRange,
        accuracy = accuracy
    )

    val contour = functionToOptimize.createContour(
        zRange = zRange,
        zAccuracy = zAccuracy,
        pointsRange = pointsRange,
        pointsAccuracy = accuracy
    ).mapToColors(colors)

    contour.forEach { contourLine ->
        println("z=${contourLine.z}, points=${contourLine.points.size}")
    }

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
    window.add(contourConstraint)
    window.apply {
        pack()
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        setSize(1000, 1000)
        isVisible = true
    }
}
