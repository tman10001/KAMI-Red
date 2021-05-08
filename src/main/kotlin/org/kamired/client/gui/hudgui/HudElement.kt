package org.kamired.client.gui.hudgui

import org.kamired.client.gui.rgui.Component
import org.kamired.client.setting.GuiConfig
import org.kamired.client.setting.settings.SettingRegister

internal abstract class HudElement(
    name: String,
    alias: Array<String> = emptyArray(),
    category: Category,
    description: String,
    alwaysListening: Boolean = false,
    enabledByDefault: Boolean = false,
) : AbstractHudElement(name, alias, category, description, alwaysListening, enabledByDefault, GuiConfig),
    SettingRegister<Component> by GuiConfig