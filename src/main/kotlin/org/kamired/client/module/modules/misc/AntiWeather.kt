package org.kamired.client.module.modules.misc

import org.kamired.client.mixin.client.world.MixinWorld
import org.kamired.client.module.Category
import org.kamired.client.module.Module

/**
 * @see MixinWorld.getThunderStrengthHead
 * @see MixinWorld.getRainStrengthHead
 */
internal object AntiWeather : Module(
    name = "AntiWeather",
    description = "Removes rain and thunder from your world",
    category = Category.MISC
)
