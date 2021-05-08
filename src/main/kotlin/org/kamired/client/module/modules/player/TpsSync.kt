package org.kamired.client.module.modules.player

import org.kamired.client.module.Category
import org.kamired.client.module.Module

internal object TpsSync : Module(
    name = "TpsSync",
    description = "Synchronizes block states with the server TPS",
    category = Category.PLAYER
)
