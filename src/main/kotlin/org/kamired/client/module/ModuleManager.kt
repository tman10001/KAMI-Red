package org.kamired.client.module

import kotlinx.coroutines.Deferred
import org.kamired.client.AsyncLoader
import org.kamired.client.KamiMod
import org.kamired.client.event.KamiEventBus
import org.kamired.client.util.AsyncCachedValue
import org.kamired.client.util.StopTimer
import org.kamired.client.util.TimeUnit
import org.kamired.commons.collections.AliasSet
import org.kamired.commons.utils.ClassUtils
import org.kamired.commons.utils.ClassUtils.instance
import org.lwjgl.input.Keyboard
import java.lang.reflect.Modifier

object ModuleManager : AsyncLoader<List<Class<out AbstractModule>>> {
    override var deferred: Deferred<List<Class<out AbstractModule>>>? = null

    private val moduleSet = AliasSet<AbstractModule>()
    private val modulesDelegate = AsyncCachedValue(5L, TimeUnit.SECONDS) {
        moduleSet.distinct().sortedBy { it.name }
    }

    val modules by modulesDelegate

    override fun preLoad0(): List<Class<out AbstractModule>> {
        val stopTimer = StopTimer()

        val list = ClassUtils.findClasses<AbstractModule>("org.kamired.client.module.modules") {
            filter { Modifier.isFinal(it.modifiers) }
        }

        val time = stopTimer.stop()

        KamiMod.LOG.info("${list.size} modules found, took ${time}ms")
        return list
    }

    override fun load0(input: List<Class<out AbstractModule>>) {
        val stopTimer = StopTimer()

        for (clazz in input) {
            register(clazz.instance)
        }

        val time = stopTimer.stop()
        KamiMod.LOG.info("${input.size} modules loaded, took ${time}ms")
    }

    internal fun register(module: AbstractModule) {
        moduleSet.add(module)
        if (module.enabledByDefault || module.alwaysEnabled) module.enable()
        if (module.alwaysListening) KamiEventBus.subscribe(module)

        modulesDelegate.update()
    }

    internal fun unregister(module: AbstractModule) {
        moduleSet.remove(module)
        KamiEventBus.unsubscribe(module)

        modulesDelegate.update()
    }

    internal fun onBind(eventKey: Int) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) return  // if key is the 'none' key (stuff like mod key in i3 might return 0)
        for (module in modules) {
            if (module.bind.value.isDown(eventKey)) module.toggle()
        }
    }

    fun getModuleOrNull(moduleName: String?) = moduleName?.let { moduleSet[it] }
}