package org.kamired.client.module.modules.misc

import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.event.events.BaritoneCommandEvent
import org.kamired.client.event.events.ConnectionEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.module.modules.movement.AutoWalk
import org.kamired.client.util.BaritoneUtils
import org.kamired.client.util.math.RotationUtils
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.threads.safeListener
import org.kamired.event.listener.listener
import kotlin.math.round

internal object AutoTunnel : Module(
    name = "AutoTunnel",
    description = "Automatically tunnels forward, at a given size",
    category = Category.MISC
) {
    private val backFill = setting("Back Fill", false)
    private val height = setting("Height", 2, 1..10, 1)
    private val width = setting("Width", 1, 1..10, 1)
    private val disableOnDisconnect = setting("Disable On Disconnect", true)

    private var lastDirection = EnumFacing.NORTH

    override fun isActive(): Boolean {
        return isEnabled
            && (BaritoneUtils.isPathing
            || BaritoneUtils.primary?.builderProcess?.isActive == true)
    }

    init {
        onDisable {
            BaritoneUtils.cancelEverything()
        }

        safeListener<TickEvent.ClientTickEvent> {
            if (!isActive()) sendTunnel()
        }

        listener<BaritoneCommandEvent> {
            if (it.command.contains("cancel")) {
                disable()
            }
        }

        listener<ConnectionEvent.Disconnect> {
            if (disableOnDisconnect.value) disable()
        }
    }

    private fun sendTunnel() {
        mc.player?.let {
            if (AutoWalk.isEnabled) AutoWalk.disable()
            BaritoneUtils.cancelEverything()
            val normalizedYaw = RotationUtils.normalizeAngle(it.rotationYaw)
            it.rotationYaw = round(normalizedYaw / 90.0f) * 90.0f
            it.rotationPitch = 0.0f
            lastDirection = it.horizontalFacing

            MessageSendHelper.sendBaritoneCommand("tunnel", height.value.toString(), width.value.toString(), "100")
        }
    }

    override fun getHudInfo(): String {
        return lastDirection.name2.capitalize()
    }

    init {
        with({ if (mc.player != null && isEnabled) sendTunnel() }) {
            height.listeners.add(this)
            width.listeners.add(this)
        }

        backFill.listeners.add { BaritoneUtils.settings?.backfill?.value = backFill.value }
    }
}