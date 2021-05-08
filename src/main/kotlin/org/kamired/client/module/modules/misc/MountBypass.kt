package org.kamired.client.module.modules.misc

import net.minecraft.entity.passive.AbstractChestHorse
import net.minecraft.network.play.client.CPacketUseEntity
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object MountBypass : Module(
    name = "MountBypass",
    category = Category.MISC,
    description = "Might allow you to mount chested animals on servers that block it"
) {
    init {
        listener<PacketEvent.Send> {
            if (it.packet !is CPacketUseEntity || it.packet.action != CPacketUseEntity.Action.INTERACT_AT) return@listener
            if (it.packet.getEntityFromWorld(mc.world) is AbstractChestHorse) it.cancel()
        }
    }
}