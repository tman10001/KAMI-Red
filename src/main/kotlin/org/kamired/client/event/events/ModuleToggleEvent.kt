package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.module.AbstractModule

class ModuleToggleEvent internal constructor(val module: AbstractModule) : Event