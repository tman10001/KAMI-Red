package org.kamired.client.module.modules.misc

import net.minecraft.client.gui.GuiGameOver
import org.kamired.client.event.events.GuiEvent
import org.kamired.client.manager.managers.WaypointManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.InfoCalculator
import org.kamired.client.util.math.CoordinateConverter.asString
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.event.listener.listener

internal object AutoRespawn : Module(
    name = "AutoRespawn",
    description = "Automatically respawn after dying",
    category = Category.MISC
) {
    private val respawn = setting("Respawn", true)
    private val deathCoords = setting("Save Death Coords", true)
    private val antiGlitchScreen = setting("Anti Glitch Screen", true)

    init {
        listener<GuiEvent.Displayed> {
            if (it.screen !is GuiGameOver) return@listener

            if (deathCoords.value && mc.player.health <= 0) {
                WaypointManager.add("Death - " + InfoCalculator.getServerType())
                MessageSendHelper.sendChatMessage("You died at ${mc.player.position.asString()}")
            }

            if (respawn.value || antiGlitchScreen.value && mc.player.health > 0) {
                mc.player.respawnPlayer()
                mc.displayGuiScreen(null)
            }
        }
    }
}