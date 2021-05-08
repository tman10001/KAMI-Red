package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.event.KamiEventBus
import org.kamired.client.event.SingletonEvent

object ShutdownEvent : Event, SingletonEvent(KamiEventBus)