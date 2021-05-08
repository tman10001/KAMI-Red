package org.kamired.client.module.modules.player

import net.minecraft.network.play.client.CPacketCloseWindow
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.mixin.extension.windowID
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object XCarry : Module(
    name = "XCarry",
    category = Category.PLAYER,
    description = "Store items in crafting slots"
) {
    init {
        listener<PacketEvent.Send> {
            if (it.packet is CPacketCloseWindow && it.packet.windowID == 0) {
                it.cancel()
            }
        }
    }
}