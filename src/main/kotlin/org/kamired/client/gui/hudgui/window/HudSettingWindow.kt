package org.kamired.client.gui.hudgui.window

import org.kamired.client.gui.hudgui.AbstractHudElement
import org.kamired.client.gui.rgui.windows.SettingWindow
import org.kamired.client.setting.settings.AbstractSetting

class HudSettingWindow(
    hudElement: AbstractHudElement,
    posX: Float,
    posY: Float
) : SettingWindow<AbstractHudElement>(hudElement.name, hudElement, posX, posY, SettingGroup.NONE) {

    override fun getSettingList(): List<AbstractSetting<*>> {
        return element.settingList
    }

}