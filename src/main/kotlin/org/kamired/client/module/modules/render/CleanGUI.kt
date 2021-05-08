package org.kamired.client.module.modules.render

import org.kamired.client.module.Category
import org.kamired.client.module.Module

internal object CleanGUI : Module(
    name = "CleanGUI",
    category = Category.RENDER,
    showOnArray = false,
    description = "Modifies parts of the GUI to be transparent"
) {
    val inventoryGlobal = setting("Inventory", true)
    val chatGlobal = setting("Chat", false)
}