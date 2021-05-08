package org.kamired.client.gui.hudgui.elements.player

import org.kamired.client.event.SafeClientEvent
import org.kamired.client.gui.hudgui.LabelHud
import org.kamired.client.util.math.Direction

internal object Direction : LabelHud(
    name = "Direction",
    category = Category.PLAYER,
    description = "Direction of player facing to"
) {

    override fun SafeClientEvent.updateText() {
        val entity = mc.renderViewEntity ?: player
        val direction = Direction.fromEntity(entity)
        displayText.add(direction.displayName, secondaryColor)
        displayText.add("(${direction.displayNameXY})", primaryColor)
    }

}