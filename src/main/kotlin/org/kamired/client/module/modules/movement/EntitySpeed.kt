package org.kamired.client.module.modules.movement

import net.minecraft.entity.Entity
import net.minecraft.entity.passive.AbstractHorse
import net.minecraft.entity.passive.EntityHorse
import net.minecraft.entity.passive.EntityPig
import org.kamired.client.event.events.PlayerTravelEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.EntityUtils.steerEntity
import org.kamired.client.util.threads.safeListener

internal object EntitySpeed : Module(
    name = "EntitySpeed",
    category = Category.MOVEMENT,
    description = "Abuse client-sided movement to shape sound barrier breaking rideables"
) {
    private val speed by setting("Speed", 1.0f, 0.1f..25.0f, 0.1f)
    private val antiStuck by setting("Anti Stuck", true)
    private val flight by setting("Flight", false)
    private val glideSpeed by setting("Glide Speed", 0.1f, 0.0f..1.0f, 0.01f, { flight })
    private val upSpeed by setting("Up Speed", 1.0f, 0.0f..5.0f, 0.1f, { flight })

    init {
        safeListener<PlayerTravelEvent> {
            player.ridingEntity?.let { entity ->
                if (entity is EntityPig || entity is AbstractHorse && entity.controllingPassenger == player) {
                    steerEntity(entity, speed, antiStuck)

                    if (entity is EntityHorse) {
                        entity.rotationYaw = player.rotationYaw
                    }

                    if (flight) fly(entity)
                }
            }
        }
    }

    private fun fly(entity: Entity) {
        if (!entity.isInWater) entity.motionY = -glideSpeed.toDouble()
        if (mc.gameSettings.keyBindJump.isKeyDown) entity.motionY += upSpeed / 2.0
    }
}
