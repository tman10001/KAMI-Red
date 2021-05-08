package org.kamired.client.module.modules.movement

import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.TickTimer
import org.kamired.client.util.TimeUnit
import org.kamired.client.util.threads.safeListener

internal object AutoJump : Module(
    name = "AutoJump",
    category = Category.MOVEMENT,
    description = "Automatically jumps if possible"
) {
    private val delay = setting("Tick Delay", 10, 0..40, 1)

    private val timer = TickTimer(TimeUnit.TICKS)

    init {
        safeListener<TickEvent.ClientTickEvent> {
            if (player.isInWater || player.isInLava) player.motionY = 0.1
            else if (player.onGround && timer.tick(delay.value.toLong())) player.jump()
        }
    }
}