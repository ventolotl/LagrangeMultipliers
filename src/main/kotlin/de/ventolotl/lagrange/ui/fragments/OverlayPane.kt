package de.ventolotl.lagrange.ui.fragments

import com.sun.javafx.tk.Toolkit
import de.ventolotl.lagrange.ui.ContourLineColored
import de.ventolotl.lagrange.ui.LagrangePane
import de.ventolotl.lagrange.ui.utility.write
import de.ventolotl.lagrange.utility.Vector
import de.ventolotl.lagrange.utility.Vector2dRange
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text

private const val LEGEND_X_POS = 40.0
private const val LEGEND_Y_POS = 40.0
private const val LEGEND_BORDER_SIZE = 10.0
private const val LEGEND_RECT_SPACING_SIZE = 20.0
private const val LEGEND_RECT_SIZE_PCT = 0.8
private const val LEGEND_LENGTH = 4
private const val LEGEND_EVAL_OFF_X = 50
private const val LEGEND_EVAL_OFF_Y = 50

class OverlayPane(lagrangePane: LagrangePane, private val grid: GridPane) : UIFragment() {
    private val contourOverlayFont = Font.font("Arial", 20.0)
    private val functionEvalFont = Font.font("Arial", 17.0)

    private val backgroundColor = Color.rgb(30, 30, 30)

    private val contourLines = lagrangePane.contourLines
    private val function3d = lagrangePane.function3

    private var relevantContours: List<ContourLineColored>? = null

    private var mouseWindowPosition = Vector.of(0.0, 0.0)

    init {
        setOnMouseMoved { event ->
            interactivePaint(event.x, event.y)
        }
    }

    override fun paint() {
        paintHeightsOverlay()
        paintFunctionEvalOverlay()
    }

    private fun interactivePaint(mouseX: Double, mouseY: Double) {
        mouseWindowPosition = Vector.of(mouseX, mouseY)

        clear()
        paint()
    }

    private fun paintFunctionEvalOverlay() {
        val algebraic = grid.windowToAlgebraicCoordinates(mouseWindowPosition)
        val zValue = function3d.eval(algebraic)

        val text = "f(%.2f, %.2f) = %.2f".format(algebraic.x, algebraic.y, zValue)
        val (width, height) = computeMaxBounds(functionEvalFont, listOf(text))

        ctx.fill = backgroundColor
        ctx.fillRect(
            mouseWindowPosition.x + LEGEND_EVAL_OFF_X,
            mouseWindowPosition.y - LEGEND_EVAL_OFF_Y,
            width,
            height
        )

        val fontMetrics = Toolkit.getToolkit().fontLoader.getFontMetrics(functionEvalFont)
        val ascent = (height - fontMetrics.lineHeight) * 0.5 + height

        val textPosition = Vector.of(
            mouseWindowPosition.x + LEGEND_EVAL_OFF_X,
            mouseWindowPosition.y + ascent - LEGEND_EVAL_OFF_Y
        )
        ctx.write(text, textPosition, Color.WHITE, functionEvalFont)
    }

    private fun paintHeightsOverlay() {
        var relevantContours = this.relevantContours ?: mutableListOf()

        // Take some of the contours into account, rendering all 1000 contours might not be a good idea...
        if (this.relevantContours == null) {
            val windowRange = Vector2dRange(
                Vector.of(0.0, 0.0),
                Vector.of(canvas.width, canvas.height)
            )
            relevantContours = this.contourLines.filter { contourLineColored ->
                val points = contourLineColored.points
                points.map(grid::algebraicToWindowCoordinates)
                    .any { point -> point in windowRange }
            }
            this.relevantContours = relevantContours
        }

        val elements = relevantContours.indices
            .filter { it % (contourLines.indices.last / LEGEND_LENGTH) == 0 }
            .map { contourLines[it].z.toInt() to contourLines[it].color }

        // Create the texts
        val texts = elements.map { (zValue, _) -> "z=$zValue" }

        // Calculate maximum width and height
        val (maxTextWidth, maxTextHeight) = computeMaxBounds(contourOverlayFont, texts)

        // Calculate rect
        val rectSize = maxTextHeight * LEGEND_RECT_SIZE_PCT
        val rectX = LEGEND_X_POS + maxTextWidth + LEGEND_RECT_SPACING_SIZE

        // Calculate total width and height
        val width = LEGEND_BORDER_SIZE + maxTextWidth + LEGEND_RECT_SPACING_SIZE + rectSize + LEGEND_BORDER_SIZE
        val height = LEGEND_BORDER_SIZE + rectSize + maxTextHeight * (texts.size - 1) + LEGEND_BORDER_SIZE

        // Draw rect
        ctx.fill = backgroundColor
        ctx.fillRect(
            LEGEND_X_POS - LEGEND_BORDER_SIZE,
            LEGEND_Y_POS - LEGEND_BORDER_SIZE,
            width,
            height
        )

        elements.withIndex().forEach { (index, element) ->
            val textY = LEGEND_Y_POS + rectSize + maxTextHeight * index

            ctx.write(texts[index], Vector.of(LEGEND_X_POS, textY), Color.WHITE, contourOverlayFont)

            ctx.fill = element.second
            ctx.fillRect(rectX, textY - rectSize, rectSize, rectSize)
        }
    }

    private fun computeMaxBounds(font: Font, texts: List<String>): TextBounds {
        val fontMetrics = Toolkit.getToolkit().fontLoader.getFontMetrics(font)
        val bounds = texts.map { text ->
            val textFX = Text(text).also {
                it.font = font
            }
            textFX.boundsInLocal
        }
        return TextBounds(
            maxWidth = bounds.maxOf { it.width },
            maxHeight = bounds.maxOf { it.height } - fontMetrics.maxDescent
        )
    }

    private data class TextBounds(val maxWidth: Double, val maxHeight: Double)
}