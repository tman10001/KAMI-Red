package org.kamired.client.module.modules.combat

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketUseEntity
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.manager.managers.FriendManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object AntiFriendHit : Module(
    name = "AntiFriendHit",
    description = "Don't hit your friends",
    category = Category.COMBAT
) {
    init {
        listener<PacketEvent.Send> {
            if (it.packet !is CPacketUseEntity || it.packet.action != CPacketUseEntity.Action.ATTACK) return@listener
            val entity = mc.world?.let { world -> it.packet.getEntityFromWorld(world) } ?: return@listener
            if (entity is EntityPlayer && FriendManager.isFriend(entity.name)) {
                it.cancel()
            }
        }
    }
}