package org.kamired.client.gui.hudgui.elements.player

import org.kamired.client.event.SafeClientEvent
import org.kamired.client.gui.hudgui.LabelHud
import org.kamired.client.manager.managers.TimerManager
import org.kamired.commons.utils.MathUtils

internal object TimerSpeed : LabelHud(
    name = "TimerSpeed",
    category = Category.PLAYER,
    description = "Client side timer speed"
) {

    override fun SafeClientEvent.updateText() {
        val timerSpeed = MathUtils.round(50.0f / TimerManager.tickLength, 2)

        displayText.add("%.2f".format(timerSpeed), primaryColor)
        displayText.add("x", secondaryColor)
    }

}