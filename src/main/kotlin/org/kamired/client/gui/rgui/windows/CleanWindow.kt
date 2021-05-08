package org.kamired.client.gui.rgui.windows

import org.kamired.client.gui.rgui.WindowComponent
import org.kamired.client.setting.GuiConfig
import org.kamired.client.setting.configs.AbstractConfig
import org.kamired.commons.interfaces.Nameable

/**
 * Window with no rendering
 */
open class CleanWindow(
    name: String,
    posX: Float,
    posY: Float,
    width: Float,
    height: Float,
    settingGroup: SettingGroup,
    config: AbstractConfig<out Nameable> = GuiConfig
) : WindowComponent(name, posX, posY, width, height, settingGroup, config)