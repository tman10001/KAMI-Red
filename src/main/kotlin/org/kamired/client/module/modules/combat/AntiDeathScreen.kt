package org.kamired.client.module.modules.combat

import net.minecraft.client.gui.GuiGameOver
import org.kamired.client.event.events.GuiEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object AntiDeathScreen : Module(
    name = "AntiDeathScreen",
    description = "Fixes random death screen glitches",
    category = Category.COMBAT
) {
    init {
        listener<GuiEvent.Displayed> {
            if (it.screen !is GuiGameOver) return@listener
            if (mc.player.health > 0) {
                mc.player.respawnPlayer()
                mc.displayGuiScreen(null)
            }
        }
    }
}