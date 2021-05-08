package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.event.ProfilerEvent

sealed class RunGameLoopEvent(override val profilerName: String) : Event, ProfilerEvent {
    class Start : RunGameLoopEvent("start")
    class Tick : RunGameLoopEvent("tick")
    class Render : RunGameLoopEvent("render")
    class End : RunGameLoopEvent("end")
}