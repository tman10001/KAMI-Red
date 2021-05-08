package org.kamired.client.module.modules.movement

import net.minecraft.init.MobEffects
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.threads.safeListener

internal object AntiLevitation : Module(
    name = "AntiLevitation",
    description = "Removes levitation potion effect",
    category = Category.MOVEMENT
) {
    init {
        safeListener<TickEvent.ClientTickEvent> {
            if (player.isPotionActive(MobEffects.LEVITATION)) {
                player.removeActivePotionEffect(MobEffects.LEVITATION)
            }
        }
    }
}