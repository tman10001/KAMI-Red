package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.event.ProfilerEvent
import org.kamired.client.mixin.extension.renderPosX
import org.kamired.client.mixin.extension.renderPosY
import org.kamired.client.mixin.extension.renderPosZ
import org.kamired.client.util.Wrapper
import org.kamired.client.util.graphics.KamiTessellator

class RenderWorldEvent : Event, ProfilerEvent {
    override val profilerName: String = "kbRender3D"

    init {
        KamiTessellator.buffer.setTranslation(
            -Wrapper.minecraft.renderManager.renderPosX,
            -Wrapper.minecraft.renderManager.renderPosY,
            -Wrapper.minecraft.renderManager.renderPosZ
        )
    }
}