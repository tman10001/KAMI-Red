package org.kamired.client.module.modules.player

import net.minecraft.network.play.client.CPacketAnimation
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.threads.safeListener
import org.kamired.event.listener.listener

internal object NoSwing : Module(
    name = "NoSwing",
    category = Category.PLAYER,
    description = "Cancels server or client swing animation"
) {
    private val mode = setting("Mode", Mode.CLIENT)

    private enum class Mode {
        CLIENT, SERVER
    }

    init {
        listener<PacketEvent.Send> {
            if (mode.value == Mode.SERVER && it.packet is CPacketAnimation) it.cancel()
        }

        safeListener<TickEvent.ClientTickEvent> {
            player.isSwingInProgress = false
            player.swingProgressInt = 0
            player.swingProgress = 0.0f
            player.prevSwingProgress = 0.0f
        }
    }
}