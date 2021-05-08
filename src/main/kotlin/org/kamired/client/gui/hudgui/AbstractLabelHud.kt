package org.kamired.client.gui.hudgui

import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.event.SafeClientEvent
import org.kamired.client.setting.configs.AbstractConfig
import org.kamired.client.util.graphics.VertexHelper
import org.kamired.client.util.graphics.font.TextComponent
import org.kamired.client.util.math.Vec2d
import org.kamired.client.util.threads.safeAsyncListener
import org.kamired.commons.interfaces.Nameable

abstract class AbstractLabelHud(
    name: String,
    alias: Array<String>,
    category: Category,
    description: String,
    alwaysListening: Boolean,
    enabledByDefault: Boolean,
    config: AbstractConfig<out Nameable>,
) : AbstractHudElement(name, alias, category, description, alwaysListening, enabledByDefault, config) {

    override val hudWidth: Float get() = displayText.getWidth() + 2.0f
    override val hudHeight: Float get() = displayText.getHeight(2)

    protected val displayText = TextComponent()

    init {
        safeAsyncListener<TickEvent.ClientTickEvent> {
            if (it.phase != TickEvent.Phase.END) return@safeAsyncListener
            displayText.clear()
            updateText()
        }
    }

    abstract fun SafeClientEvent.updateText()

    override fun renderHud(vertexHelper: VertexHelper) {
        super.renderHud(vertexHelper)

        val textPosX = width * dockingH.multiplier / scale - dockingH.offset
        val textPosY = height * dockingV.multiplier / scale

        displayText.draw(
            Vec2d(textPosX.toDouble(), textPosY.toDouble()),
            horizontalAlign = dockingH,
            verticalAlign = dockingV
        )
    }

}