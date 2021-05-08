package org.kamired.client.event.events

import net.minecraft.network.Packet
import org.kamired.client.event.Cancellable
import org.kamired.client.event.Event
import org.kamired.client.event.ICancellable

abstract class PacketEvent(val packet: Packet<*>) : Event, ICancellable by Cancellable() {
    class Receive(packet: Packet<*>) : PacketEvent(packet)
    class PostReceive(packet: Packet<*>) : PacketEvent(packet)
    class Send(packet: Packet<*>) : PacketEvent(packet)
    class PostSend(packet: Packet<*>) : PacketEvent(packet)
}