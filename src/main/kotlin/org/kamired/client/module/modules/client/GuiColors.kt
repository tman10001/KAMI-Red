package org.kamired.client.module.modules.client

import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.color.ColorHolder

internal object GuiColors : Module(
    name = "GuiColors",
    description = "Opens the Click GUI",
    showOnArray = false,
    category = Category.CLIENT,
    alwaysEnabled = true
) {
    private val primarySetting = setting("Primary Color", ColorHolder(167, 1, 1, 255))
    private val outlineSetting = setting("Outline Color", ColorHolder(138, 1, 1, 200))
    private val backgroundSetting = setting("Background Color", ColorHolder(30, 36, 48, 200))
    private val textSetting = setting("Text Color", ColorHolder(255, 255, 255, 255))
    private val aHover = setting("Hover Alpha", 32, 0..255, 1)

    val primary get() = primarySetting.value.clone()
    val idle get() = if (primary.averageBrightness < 0.8f) ColorHolder(255, 255, 255, 0) else ColorHolder(0, 0, 0, 0)
    val hover get() = idle.apply { a = aHover.value }
    val click get() = idle.apply { a = aHover.value * 2 }
    val backGround get() = backgroundSetting.value.clone()
    val outline get() = outlineSetting.value.clone()
    val text get() = textSetting.value.clone()
}