package org.kamired.client.module.modules.client

import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.KamiMod
import org.kamired.client.event.events.ModuleToggleEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.TickTimer
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.text.format
import org.kamired.event.listener.listener
import org.lwjgl.opengl.Display

internal object CommandConfig : Module(
    name = "CommandConfig",
    category = Category.CLIENT,
    description = "Configures client chat related stuff",
    showOnArray = false,
    alwaysEnabled = true
) {
    var prefix by setting("Prefix", ";", { false })
    val toggleMessages by setting("Toggle Messages", false)
    private val customTitle = setting("Window Title", true)

    private val timer = TickTimer()
    private val prevTitle = Display.getTitle()
    private const val title = "${KamiMod.NAME} ${KamiMod.KAMI_KATAKANA} ${KamiMod.VERSION_SIMPLE}"

    init {
        listener<ModuleToggleEvent> {
            if (!toggleMessages) return@listener

            MessageSendHelper.sendChatMessage(it.module.name +
                if (it.module.isEnabled) TextFormatting.RED format " disabled"
                else TextFormatting.GREEN format " enabled"
            )
        }

        listener<TickEvent.ClientTickEvent> {
            if (timer.tick(10000L)) {
                if (customTitle.value) Display.setTitle(title)
                else Display.setTitle(prevTitle)
            }
        }

        customTitle.listeners.add {
            timer.reset(-0xCAFEBABE)
        }
    }
}