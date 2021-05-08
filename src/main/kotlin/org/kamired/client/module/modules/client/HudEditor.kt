package org.kamired.client.module.modules.client

import org.kamired.client.event.events.ShutdownEvent
import org.kamired.client.gui.hudgui.KamiHudGui
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.event.listener.listener

internal object HudEditor : Module(
    name = "HudEditor",
    description = "Edits the Hud",
    category = Category.CLIENT,
    showOnArray = false
) {
    init {
        onEnable {
            if (mc.currentScreen !is KamiHudGui) {
                ClickGUI.disable()
                mc.displayGuiScreen(KamiHudGui)
                KamiHudGui.onDisplayed()
            }
        }

        onDisable {
            if (mc.currentScreen is KamiHudGui) {
                mc.displayGuiScreen(null)
            }
        }

        listener<ShutdownEvent> {
            disable()
        }
    }
}
