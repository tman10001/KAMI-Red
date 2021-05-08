package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.event.ProfilerEvent

class RenderOverlayEvent : Event, ProfilerEvent {
    override val profilerName: String = "kbRender2D"
}