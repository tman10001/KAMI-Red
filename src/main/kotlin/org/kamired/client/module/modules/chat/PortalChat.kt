package org.kamired.client.module.modules.chat

import org.kamired.client.mixin.client.player.MixinEntityPlayerSP
import org.kamired.client.module.Category
import org.kamired.client.module.Module

/**
 * @see MixinEntityPlayerSP
 */
internal object PortalChat : Module(
    name = "PortalChat",
    category = Category.CHAT,
    description = "Allows you to open GUIs in portals",
    showOnArray = false
)
