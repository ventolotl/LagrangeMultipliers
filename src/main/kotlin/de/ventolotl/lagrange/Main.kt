package de.ventolotl.lagrange

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3
import de.ventolotl.lagrange.maths.createContour
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.mapToColors
import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.range
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.cos

const val WIN_WIDTH = 1000.0
const val WIN_HEIGHT = 1000.0

fun main() {
    Application.launch(LagrangeMultipliersUI::class.java)
}

class LagrangeMultipliersUI : Application() {
    private val colors = (0..<255 step 10).map {
        Color.rgb(it / 8, 255 - it, it)
    }.toTypedArray()

    override fun start(stage: Stage) {
        val zRange = -50.0 range 200.0
        val zAccuracy = 0.1
        val areaLength = 10.0
        val pointsRange = Vector.of(-areaLength, -areaLength) range Vector.of(areaLength, areaLength)
        val accuracy = 100

        val functionToOptimize = Function3 { x, y ->
            x * x + y * y
        }

        val constraintEq = Function3 { x, y ->
            x - cos(y) * y
        }
        val constraintValue = 4.0

        val constraint = Constraint(
            equation = constraintEq,
            constant = constraintValue,
            range = pointsRange,
            accuracy = accuracy
        )
        val contour = functionToOptimize.createContour(
            zRange = zRange,
            zAccuracy = zAccuracy,
            pointsRange = pointsRange,
            accuracy = accuracy
        ).mapToColors(colors)

        val renderer = LagrangePane(
            function3 = functionToOptimize,
            constraint = constraint,
            contourLines = contour,
            scalingFactor = areaLength,
            showGrid = true
        )

        val scene = Scene(renderer, WIN_WIDTH, WIN_HEIGHT)

        stage.title = "Lagrange Multipliers"
        stage.scene = scene

        stage.show()
    }
}
