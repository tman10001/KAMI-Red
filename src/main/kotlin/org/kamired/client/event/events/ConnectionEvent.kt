package org.kamired.client.event.events

import org.kamired.client.event.Event

abstract class ConnectionEvent : Event {
    class Connect : ConnectionEvent()
    class Disconnect : ConnectionEvent()
}