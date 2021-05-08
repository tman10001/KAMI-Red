package org.kamired.client.event.events

import net.minecraft.entity.Entity
import org.kamired.client.event.Cancellable
import org.kamired.client.event.Event

class PlayerAttackEvent(val entity: Entity) : Event, Cancellable()