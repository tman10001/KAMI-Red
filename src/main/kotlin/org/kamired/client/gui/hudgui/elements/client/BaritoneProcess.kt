package org.kamired.client.gui.hudgui.elements.client

import org.kamired.client.event.SafeClientEvent
import org.kamired.client.gui.hudgui.LabelHud
import org.kamired.client.module.modules.movement.AutoWalk
import org.kamired.client.process.PauseProcess
import org.kamired.client.util.BaritoneUtils

internal object BaritoneProcess : LabelHud(
    name = "BaritoneProcess",
    category = Category.CLIENT,
    description = "Shows what Baritone is doing"
) {

    override fun SafeClientEvent.updateText() {
        val process = BaritoneUtils.primary?.pathingControlManager?.mostRecentInControl()?.orElse(null) ?: return

        when {
            process == PauseProcess -> {
                displayText.addLine(process.displayName0())
            }
            AutoWalk.baritoneWalk -> {
                displayText.addLine("AutoWalk (${AutoWalk.direction.displayName})")
            }
            else -> {
                displayText.addLine("Process: ${process.displayName()}")
            }
        }
    }

}