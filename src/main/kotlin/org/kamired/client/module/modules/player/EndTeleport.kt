package org.kamired.client.module.modules.player

import net.minecraft.network.play.server.SPacketDisconnect
import net.minecraft.network.play.server.SPacketRespawn
import net.minecraft.util.text.TextComponentString
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.threads.safeListener

internal object EndTeleport : Module(
    name = "EndTeleport",
    category = Category.PLAYER,
    description = "Allows for teleportation when going through end portals"
) {
    private val confirmed by setting("Confirm", false)

    init {
        onEnable {
            if (mc.currentServerData == null) {
                MessageSendHelper.sendWarningMessage("$chatName This module does not work in singleplayer")
                disable()
            } else if (!confirmed) {
                MessageSendHelper.sendWarningMessage("$chatName This module will kick you from the server! It is part of the exploit and cannot be avoided")
            }
        }

        safeListener<PacketEvent.Receive> {
            if (it.packet !is SPacketRespawn) return@safeListener
            if (it.packet.dimensionID == 1 && confirmed) {
                connection.handleDisconnect(SPacketDisconnect(TextComponentString("Attempting teleportation exploit")))
                disable()
            }
        }
    }
}