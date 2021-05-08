package org.kamired.client.plugin.api

import org.kamired.commons.interfaces.Nameable

interface IPluginClass : Nameable {
    val pluginMain: Plugin
}