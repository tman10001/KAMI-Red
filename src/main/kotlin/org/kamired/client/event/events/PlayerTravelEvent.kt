package org.kamired.client.event.events

import org.kamired.client.event.Cancellable
import org.kamired.client.event.Event
import org.kamired.client.event.ICancellable
import org.kamired.client.event.ProfilerEvent

class PlayerTravelEvent : Event, ICancellable by Cancellable(), ProfilerEvent {
    override val profilerName: String = "kbPlayerTravel"
}