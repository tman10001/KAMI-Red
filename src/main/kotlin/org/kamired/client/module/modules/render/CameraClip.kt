package org.kamired.client.module.modules.render

import org.kamired.client.mixin.client.render.MixinEntityRenderer
import org.kamired.client.module.Category
import org.kamired.client.module.Module

/**
 * @see MixinEntityRenderer.rayTraceBlocks
 */
internal object CameraClip : Module(
    name = "CameraClip",
    category = Category.RENDER,
    description = "Allows your 3rd person camera to pass through blocks",
    showOnArray = false
)
