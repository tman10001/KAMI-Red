package org.kamired.client.event.events

import net.minecraft.client.gui.GuiScreen
import org.kamired.client.event.Event

abstract class GuiEvent(var screen: GuiScreen?) : Event {
    class Displayed(screen: GuiScreen?) : GuiEvent(screen)
    class Closed(screen: GuiScreen?) : GuiEvent(screen)
}