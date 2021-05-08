package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.event.KamiEventBus
import org.kamired.client.event.SingletonEvent

/**
 * Posted at the return of when Baritone's Settings are initialized.
 */
object BaritoneSettingsInitEvent : Event, SingletonEvent(KamiEventBus)