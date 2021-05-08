package org.kamired.client.module.modules.misc

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.server.SPacketKeepAlive
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.threads.defaultScope
import org.kamired.client.util.threads.onMainThreadSafe
import org.kamired.event.listener.listener

internal object PingSpoof : Module(
    name = "PingSpoof",
    category = Category.MISC,
    description = "Cancels or adds delay to your ping packets"
) {
    private val delay = setting("Delay", 100, 0..2000, 25)

    init {
        listener<PacketEvent.Receive> {
            if (it.packet is SPacketKeepAlive) {
                it.cancel()
                defaultScope.launch {
                    delay(delay.value.toLong())
                    onMainThreadSafe {
                        connection.sendPacket(CPacketKeepAlive(it.packet.id))
                    }
                }
            }
        }
    }
}
