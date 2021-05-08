package org.kamired.client.plugin.api

import org.kamired.client.command.ClientCommand
import org.kamired.client.command.CommandManager
import org.kamired.client.event.KamiEventBus
import org.kamired.client.gui.GuiManager
import org.kamired.client.manager.Manager
import org.kamired.client.module.ModuleManager
import org.kamired.client.plugin.PluginInfo
import org.kamired.client.setting.ConfigManager
import org.kamired.client.setting.configs.PluginConfig
import org.kamired.client.util.threads.BackgroundJob
import org.kamired.client.util.threads.BackgroundScope
import org.kamired.commons.collections.CloseableList
import org.kamired.commons.interfaces.Nameable
import org.kamired.event.ListenerManager

/**
 * A plugin. All plugin main classes must extend this class.
 *
 * The methods onLoad and onUnload may be implemented by your
 * plugin in order to do stuff when the plugin is loaded and
 * unloaded, respectively.
 */
open class Plugin : Nameable {

    private lateinit var info: PluginInfo
    override val name: String get() = info.name
    val version: String get() = info.version
    val kamiVersion: String get() = info.minApiVersion
    val description: String get() = info.description
    val authors: Array<String> get() = info.authors
    val requiredPlugins: Array<String> get() = info.requiredPlugins
    val url: String get() = info.url
    val hotReload: Boolean get() = info.hotReload

    /**
     * Config for the plugin
     */
    val config by lazy { PluginConfig(name) }

    /**
     * The list of [Manager] the plugin will add.
     *
     * @sample org.kamired.client.manager.managers.KamiMojiManager
     */
    val managers = CloseableList<Manager>()

    /**
     * The list of [ClientCommand] the plugin will add.
     *
     * @sample org.kamired.client.command.commands.CreditsCommand
     */
    val commands = CloseableList<ClientCommand>()

    /**
     * The list of [PluginModule] the plugin will add.
     *
     * @sample org.kamired.client.module.modules.combat.KillAura
     */
    val modules = CloseableList<PluginModule>()

    /**
     * The list of [PluginHudElement] the plugin will add.
     *
     * @sample org.kamired.client.gui.hudgui.elements.client.ModuleList
     */
    val hudElements = CloseableList<PluginHudElement>()

    /**
     * The list of [BackgroundJob] the plugin will add.
     *
     * @sample org.kamired.client.module.modules.client.CommandConfig
     */
    val bgJobs = CloseableList<BackgroundJob>()

    internal fun setInfo(infoIn: PluginInfo) {
        info = infoIn
    }

    internal fun register() {
        managers.close()
        commands.close()
        modules.close()
        hudElements.close()
        bgJobs.close()

        ConfigManager.register(config)

        managers.forEach(KamiEventBus::subscribe)
        commands.forEach(CommandManager::register)
        modules.forEach(ModuleManager::register)
        hudElements.forEach(GuiManager::register)
        bgJobs.forEach(BackgroundScope::launchLooping)

        ConfigManager.load(config)

        modules.forEach {
            if (it.isEnabled) it.enable()
        }
    }

    internal fun unregister() {
        ConfigManager.save(config)
        ConfigManager.unregister(config)

        managers.forEach {
            KamiEventBus.unsubscribe(it)
            ListenerManager.unregister(it)
        }
        commands.forEach {
            CommandManager.unregister(it)
            ListenerManager.unregister(it)
        }
        modules.forEach {
            ModuleManager.unregister(it)
            ListenerManager.unregister(it)
        }
        hudElements.forEach {
            GuiManager.unregister(it)
            ListenerManager.unregister(it)
        }
        bgJobs.forEach(BackgroundScope::cancel)
    }

    /**
     * Called when the plugin is loaded. Override / implement this method to
     * do something when the plugin is loaded.
     */
    open fun onLoad() {}

    /**
     * Called when the plugin is unloaded. Override / implement this method to
     * do something when the plugin is unloaded.
     */
    open fun onUnload() {}

    override fun equals(other: Any?) = this === other
        || (other is Plugin
        && name == other.name)

    override fun hashCode() = name.hashCode()

    override fun toString() = info.toString()

}