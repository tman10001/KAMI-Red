package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.manager.managers.WaypointManager.Waypoint

class WaypointUpdateEvent(val type: Type, val waypoint: Waypoint?) : Event {
    enum class Type {
        GET, ADD, REMOVE, CLEAR, RELOAD
    }
}