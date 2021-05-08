package org.kamired.client.gui.hudgui.elements.misc

import org.kamired.client.event.SafeClientEvent
import org.kamired.client.gui.hudgui.LabelHud
import org.kamired.client.util.InfoCalculator

internal object Ping : LabelHud(
    name = "Ping",
    category = Category.MISC,
    description = "Delay between client and server"
) {

    override fun SafeClientEvent.updateText() {
        displayText.add(InfoCalculator.ping().toString(), primaryColor)
        displayText.add("ms", secondaryColor)
    }

}