package org.kamired.client.module.modules.player

import net.minecraft.network.play.server.SPacketPlayerPosLook
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.mixin.extension.rotationPitch
import org.kamired.client.mixin.extension.rotationYaw
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object AntiForceLook : Module(
    name = "AntiForceLook",
    category = Category.PLAYER,
    description = "Stops server packets from turning your head"
) {
    init {
        listener<PacketEvent.Receive> {
            if (it.packet !is SPacketPlayerPosLook || mc.player == null) return@listener
            it.packet.rotationYaw = mc.player.rotationYaw
            it.packet.rotationPitch = mc.player.rotationPitch
        }
    }
}