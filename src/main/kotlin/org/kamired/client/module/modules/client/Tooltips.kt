package org.kamired.client.module.modules.client

import org.kamired.client.module.Category
import org.kamired.client.module.Module

internal object Tooltips : Module(
    name = "Tooltips",
    description = "Displays handy module descriptions in the GUI",
    category = Category.CLIENT,
    showOnArray = false,
    enabledByDefault = true
)
