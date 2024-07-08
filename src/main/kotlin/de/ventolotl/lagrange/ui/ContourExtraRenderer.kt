package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.maths.Vector2d
import de.ventolotl.lagrange.maths.distSq
import de.ventolotl.lagrange.utility.Vector2dRange
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities
import kotlin.math.roundToInt

private const val LEGEND_X_OFFSET = 40
private const val LEGEND_Y_OFFSET = 40
private const val LEGEND_BORDER_SIZE = 10
private const val LEGEND_RECT_SPACING_SIZE = 30
private const val LEGEND_RECT_SIZE_PCT = 0.8
private const val LEGEND_LENGTH = 4

class ContourExtraRenderer(
    contourLines: List<ContourLineColored>,
    constraint: Constraint,
    scalingFactor: Int,
    function3d: Function3d
) : ContourRenderer(contourLines, constraint, scalingFactor, function3d) {
    private val gradientOptimizeFunc = function3d.gradient()
    private val gradientConstraintFunc = constraint.equation.gradient()

    private var dragged = false
    private var renderData: RenderData? = null

    private var relevantContours: List<ContourLineColored>? = null

    init {
        val mouseAdapter = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                dragged = !dragged
            }

            override fun mouseMoved(event: MouseEvent) {
                if (dragged) {
                    createGradients(event.x, event.y)
                }
            }
        }
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
    }

    override fun render(graphics: Graphics) {
        super.render(graphics)

        graphics.font = contourFont

        paintRenderData(graphics)
        paintLegend(graphics)
    }

    override fun resize() {
        super.resize()

        relevantContours = null
    }

    private fun paintRenderData(graphics: Graphics) {
        renderData?.let { (cursorData, optimizeGradient, constraintGradient) ->
            // Render gradients
            renderGradient(graphics, optimizeGradient)
            renderGradient(graphics, constraintGradient)

            // Render cursor z
            val text = "z=%.3f".format(cursorData.z)
            graphics.drawText(text, cursorData.pos, Color.WHITE, contourFont)
        }
    }

    private fun renderGradient(graphics: Graphics, gradientData: GradientData) {
        val point = gradientData.point
        val vector = gradientData.vector

        graphics.drawCircle(point, radius = 15, gradientData.color)
        graphics.drawArrowLine(point, vector, 10, 10)
    }

    private fun createGradients(mouseX: Int, mouseY: Int) {
        val mousePoint = Vector2d(mouseX, mouseY)
        val algebraicMousePoint = windowToAlgebraicCoordinates(mousePoint)

        val cursorZ = function3d.eval(algebraicMousePoint.x, algebraicMousePoint.y)
        val cursorData = CursorData(cursorZ, Vector2d(mouseX, mouseY - 30))

        val optimizeFunctionGradient = createGradientOptimizeFunction(algebraicMousePoint)
        val constraintFunctionGradient = createGradientConstraintFunction(algebraicMousePoint)

        renderData = RenderData(cursorData, optimizeFunctionGradient, constraintFunctionGradient)

        SwingUtilities.invokeLater { repaint() }
    }

    private fun createGradientOptimizeFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val contourClosestPoints = contourLines
            .filter { line -> line.points.isNotEmpty() }
            .associateWith { line ->
                line.points.minBy { it.distSq(algebraicMousePoint) }
            }
        val (contour, nearestPoint) = contourClosestPoints
            .minBy { (_, point) ->
                point.distSq(algebraicMousePoint)
            }

        return createGradientFunction(nearestPoint, gradientOptimizeFunc, contour.color.darker())
    }

    private fun createGradientConstraintFunction(algebraicMousePoint: Vector2d<Double>): GradientData {
        val nearestPoint = constraint.points.minBy { point ->
            point.distSq(algebraicMousePoint)
        }
        return createGradientFunction(nearestPoint, gradientConstraintFunc, constraint.color)
    }

    private fun createGradientFunction(
        nearestPoint: Vector2d<Double>,
        gradientFunc: Vector2d<Function3d>,
        color: Color
    ): GradientData {
        val nearestX = nearestPoint.x
        val nearestY = nearestPoint.y

        val gradient = Vector2d(
            gradientFunc.x.eval(nearestX, nearestY),
            gradientFunc.y.eval(nearestX, nearestY)
        )
        val adjustedGradient = Vector2d(gradient.x + nearestX, gradient.y + nearestY)

        return GradientData(
            algebraicToWindowCoordinates(nearestPoint),
            algebraicToWindowCoordinates(adjustedGradient),
            color
        )
    }

    private fun paintLegend(graphics: Graphics) {
        var relevantContours = this.relevantContours ?: mutableListOf()

        if (this.relevantContours == null) {
            val windowRange = Vector2dRange(
                0..width,
                0..height
            )
            relevantContours = this.contourLines.filter { contourLineColored ->
                val points = contourLineColored.points
                points.map(::algebraicToWindowCoordinates)
                    .any { point -> point in windowRange }
            }
            this.relevantContours = relevantContours
        }

        val elements = relevantContours.indices
            .filter { it % (contourLines.indices.last / LEGEND_LENGTH) == 0 }
            .map { contourLines[it].z.toInt() to contourLines[it].color }

        val fontMetrics = graphics.fontMetrics
        val textHeight = fontMetrics.height
        val rectSize = (textHeight * LEGEND_RECT_SIZE_PCT).roundToInt()

        // Create the texts
        val texts = elements.map { (zValue, _) -> "z=$zValue" }

        // Calculate the maximum width
        val elementWidth = texts.maxOf { text -> fontMetrics.stringWidth(text) }.toInt()

        val rectX = LEGEND_X_OFFSET + LEGEND_RECT_SPACING_SIZE + elementWidth - rectSize

        val width = 2 * LEGEND_BORDER_SIZE + LEGEND_RECT_SPACING_SIZE + elementWidth
        val height = LEGEND_Y_OFFSET + textHeight * (elements.size - 1) + LEGEND_BORDER_SIZE

        // Draw rect
        graphics.color = Color(30, 30, 30)
        graphics.fillRect(
            LEGEND_X_OFFSET - LEGEND_BORDER_SIZE,
            LEGEND_Y_OFFSET - LEGEND_BORDER_SIZE,
            width,
            height
        )

        // Draw heights
        elements.withIndex().forEach { (index, element) ->
            val textY = LEGEND_Y_OFFSET + textHeight + textHeight * index

            graphics.color = Color.WHITE
            graphics.drawString(texts[index], LEGEND_X_OFFSET, textY)

            graphics.color = element.second
            graphics.fillRect(rectX, textY - fontMetrics.ascent, rectSize, rectSize)
        }
    }
}

private data class RenderData(
    val cursor: CursorData,
    val optimizeGradient: GradientData,
    val constraintGradient: GradientData
)

private data class CursorData(
    val z: Double,
    val pos: Vector2d<Int>
)

private data class GradientData(
    val point: Vector2d<Int>,
    val vector: Vector2d<Int>,
    val color: Color
)
