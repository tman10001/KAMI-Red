package org.kamired.client.module.modules.player

import net.minecraft.network.play.client.CPacketConfirmTeleport
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.threads.runSafe
import org.kamired.event.listener.listener

internal object PortalGodMode : Module(
    name = "PortalGodMode",
    category = Category.PLAYER,
    description = "Don't take damage in portals"
) {
    private val instantTeleport by setting("Instant Teleport", true)

    private var packet: CPacketConfirmTeleport? = null

    init {
        onEnable {
            packet = null
        }

        onDisable {
            runSafe {
                if (instantTeleport) packet?.let {
                    connection.sendPacket(it)
                }
            }
        }

        listener<PacketEvent.Send> {
            if (it.packet !is CPacketConfirmTeleport) return@listener
            it.cancel()
            packet = it.packet
        }
    }
}