package de.ventolotl.lagrange

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.createContour
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.mapToColors
import de.ventolotl.lagrange.utility.Vector2d
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
        val zAccuracy = 0.2
        val area = 13.0
        val pointsRange = Vector2d(-area, -area) range Vector2d(area, area)
        val accuracy = 100

        val functionToOptimize = Function3d { x, y ->
            x * x + y * y
        }

        val constraintEq = Function3d { x, y ->
            x * cos(y)
        }
        val constraintValue = 1.0

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
            function3d = functionToOptimize,
            constraint = constraint,
            contourLines = contour,
            scalingFactor = 10.0,
            showGrid = true
        )

        val scene = Scene(renderer, WIN_WIDTH, WIN_HEIGHT)

        stage.title = "Lagrange Multipliers"
        stage.scene = scene

        stage.show()
    }
}
