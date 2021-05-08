package org.kamired.client.module.modules.player

import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.event.SafeClientEvent
import org.kamired.client.event.events.ConnectionEvent
import org.kamired.client.event.events.PacketEvent
import org.kamired.client.mixin.extension.x
import org.kamired.client.mixin.extension.y
import org.kamired.client.mixin.extension.z
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.threads.runSafe
import org.kamired.client.util.threads.safeListener
import org.kamired.event.listener.listener
import java.util.*

internal object Blink : Module(
    name = "Blink",
    category = Category.PLAYER,
    description = "Cancels server side packets"
) {
    private val cancelPacket by setting("Cancel Packets", false)
    private val autoReset by setting("Auto Reset", true)
    private val resetThreshold by setting("Reset Threshold", 20, 1..100, 5, { autoReset })

    private const val ENTITY_ID = -114514
    private val packets = ArrayDeque<CPacketPlayer>()
    private var clonedPlayer: EntityOtherPlayerMP? = null
    private var sending = false

    init {
        onEnable {
            runSafe {
                begin()
            }
        }

        onDisable {
            end()
        }

        listener<PacketEvent.Send> {
            if (!sending && it.packet is CPacketPlayer) {
                it.cancel()
                packets.add(it.packet)
            }
        }

        safeListener<TickEvent.ClientTickEvent> {
            if (it.phase != TickEvent.Phase.END) return@safeListener
            if (autoReset && packets.size >= resetThreshold) {
                end()
                begin()
            }
        }

        listener<ConnectionEvent.Disconnect> {
            mc.addScheduledTask {
                packets.clear()
                clonedPlayer = null
            }
        }
    }

    private fun SafeClientEvent.begin() {
        clonedPlayer = EntityOtherPlayerMP(mc.world, mc.session.profile).apply {
            copyLocationAndAnglesFrom(mc.player)
            rotationYawHead = mc.player.rotationYawHead
            inventory.copyInventory(mc.player.inventory)
            noClip = true
        }.also {
            mc.world.addEntityToWorld(ENTITY_ID, it)
        }
    }

    private fun end() {
        mc.addScheduledTask {
            runSafe {
                if (cancelPacket) {
                    packets.peek()?.let { player.setPosition(it.x, it.y, it.z) }
                    packets.clear()
                } else {
                    sending = true
                    while (packets.isNotEmpty()) connection.sendPacket(packets.poll())
                    sending = false
                }

                clonedPlayer?.setDead()
                world.removeEntityFromWorld(ENTITY_ID)
                clonedPlayer = null
            }

            packets.clear()
        }
    }

    override fun getHudInfo(): String {
        return packets.size.toString()
    }
}