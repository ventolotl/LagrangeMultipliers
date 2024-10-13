package de.ventolotl.lagrange

import de.ventolotl.lagrange.ui.utility.ColorInterpolator
import de.ventolotl.lagrange.ui.utility.ColorInterpolator.toVec
import javafx.scene.paint.Color

object ColorSet {
    private val interpolations = listOf(
        0.0 to Color.hsb(244.0, 0.92, 0.53),
        0.25 to Color.hsb(182.0, 0.86, 0.57),
        0.5 to Color.hsb(103.0, 0.74, 0.54),
        0.75 to Color.hsb(64.0, 1.0, 0.55),
        1.0 to Color.hsb(0.0, 0.83, 0.58)
    )

    fun createColorSet(size: Int): List<Color> {
        return (0..<size).map { iteration ->
            ColorInterpolator.linearGradient(
                interpolations,
                value = { (v, _) -> v },
                color = { (_, c) -> c.toVec() },
                i = iteration.toDouble() / (size - 1)
            )
        }
    }
}