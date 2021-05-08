package org.kamired.client.manager.managers

import org.kamired.client.event.events.RunGameLoopEvent
import org.kamired.client.manager.Manager
import org.kamired.client.mixin.extension.tickLength
import org.kamired.client.mixin.extension.timer
import org.kamired.client.module.AbstractModule
import org.kamired.client.util.TickTimer
import org.kamired.client.util.TimeUnit
import org.kamired.commons.extension.synchronized
import org.kamired.event.listener.listener
import java.util.*

object TimerManager : Manager {
    private val timer = TickTimer(TimeUnit.TICKS)
    private val modifications = TreeMap<AbstractModule, Pair<Float, Long>>(compareByDescending { it.modulePriority }).synchronized() // <Module, <Tick length, Added Time>>

    private var modified = false

    var tickLength = 50.0f; private set

    init {
        listener<RunGameLoopEvent.Start> {
            if (timer.tick(5L)) {
                val removeTime = System.currentTimeMillis() - 250L
                modifications.values.removeIf { it.second < removeTime }
            }

            if (mc.player != null && modifications.isNotEmpty()) {
                modifications.firstEntry()?.let {
                    mc.timer.tickLength = it.value.first
                }
                modified = true
            } else if (modified) {
                reset()
            }

            tickLength = mc.timer.tickLength
        }
    }

    fun AbstractModule.resetTimer() {
        modifications.remove(this)
    }

    fun AbstractModule.modifyTimer(tickLength: Float) {
        if (mc.player != null) {
            modifications[this] = tickLength to System.currentTimeMillis()
        }
    }

    private fun reset() {
        mc.timer.tickLength = 50.0f
        modified = false
    }
}