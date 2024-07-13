package de.ventolotl.lagrange.ui

import de.ventolotl.lagrange.maths.Constraint
import de.ventolotl.lagrange.maths.Function3d
import de.ventolotl.lagrange.ui.fragments.*
import javafx.animation.PauseTransition
import javafx.scene.layout.StackPane
import javafx.util.Duration

class LagrangePane(
    val function3d: Function3d,
    val constraint: Constraint,
    val contourLines: List<ContourLineColored>,
    scalingFactor: Double,
    showGrid: Boolean
) : StackPane() {
    private val grid = GridPane(scalingFactor, showGrid)
    private val overlayPane = OverlayPane(this, grid)
    private val interactivePane = InteractivePane(this, grid)
    private val contourPane = ContourPane(this, grid)

    private val fragments = arrayOf(overlayPane, interactivePane, grid, contourPane)

    init {
        addUIFragments()
        forwardEventCalls()
        registerResizeListener()
        render()
    }

    private fun addUIFragments() {
        val firstFragment = fragments.first()
        children.add(firstFragment)

        var parentFragment = firstFragment
        fragments.drop(1).forEach { fragment ->
            parentFragment.children.add(fragment)
            parentFragment = fragment
        }

        fragments.forEach(UIFragment::addCanvas)
    }

    private fun forwardEventCalls() {
        // JavaFX does not fire events in child-panes automatically
        val tailed = fragments.drop(1)

        setEventDispatcher { event, eventDispatchChain ->
            tailed.forEach { fragment ->
                fragment.eventDispatcher.dispatchEvent(
                    event,
                    eventDispatchChain
                )
            }
            event
        }
    }

    private fun registerResizeListener() {
        // We are only interested in repainting the window when the user stopped resizing their screen
        val pause = PauseTransition(Duration.millis(100.0))

        // Add listeners to detect window size changes
        widthProperty().addListener { _, _, _ -> pause.playFromStart() }
        heightProperty().addListener { _, _, _ -> pause.playFromStart() }

        pause.setOnFinished { resizeAll(width, height) }
    }

    private fun render() {
        fragments.forEach(UIFragment::update)
    }

    private fun resizeAll(width: Double, height: Double) {
        fragments.forEach { fragment -> fragment.updateCanvas(width, height) }
        render()
    }
}