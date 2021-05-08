package org.kamired.client.gui

import kotlinx.coroutines.Deferred
import org.kamired.client.AsyncLoader
import org.kamired.client.KamiMod
import org.kamired.client.event.KamiEventBus
import org.kamired.client.gui.clickgui.KamiClickGui
import org.kamired.client.gui.hudgui.AbstractHudElement
import org.kamired.client.gui.hudgui.KamiHudGui
import org.kamired.client.util.AsyncCachedValue
import org.kamired.client.util.StopTimer
import org.kamired.client.util.TimeUnit
import org.kamired.commons.collections.AliasSet
import org.kamired.commons.utils.ClassUtils
import org.kamired.commons.utils.ClassUtils.instance
import java.lang.reflect.Modifier

internal object GuiManager : AsyncLoader<List<Class<out AbstractHudElement>>> {
    override var deferred: Deferred<List<Class<out AbstractHudElement>>>? = null
    private val hudElementSet = AliasSet<AbstractHudElement>()

    val hudElements by AsyncCachedValue(5L, TimeUnit.SECONDS) {
        hudElementSet.distinct().sortedBy { it.name }
    }

    override fun preLoad0(): List<Class<out AbstractHudElement>> {
        val stopTimer = StopTimer()

        val list = ClassUtils.findClasses<AbstractHudElement>("org.kamiblue.client.gui.hudgui.elements") {
            filter { Modifier.isFinal(it.modifiers) }
        }

        val time = stopTimer.stop()

        KamiMod.LOG.info("${list.size} hud elements found, took ${time}ms")
        return list
    }

    override fun load0(input: List<Class<out AbstractHudElement>>) {
        val stopTimer = StopTimer()

        for (clazz in input) {
            register(clazz.instance)
        }

        val time = stopTimer.stop()
        KamiMod.LOG.info("${input.size} hud elements loaded, took ${time}ms")

        KamiClickGui.onGuiClosed()
        KamiHudGui.onGuiClosed()

        KamiEventBus.subscribe(KamiClickGui)
        KamiEventBus.subscribe(KamiHudGui)
    }

    internal fun register(hudElement: AbstractHudElement) {
        hudElementSet.add(hudElement)
        KamiHudGui.register(hudElement)
    }

    internal fun unregister(hudElement: AbstractHudElement) {
        hudElementSet.remove(hudElement)
        KamiHudGui.unregister(hudElement)
    }

    fun getHudElementOrNull(name: String?) = name?.let { hudElementSet[it] }
}