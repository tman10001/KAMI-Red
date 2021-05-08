package org.kamired.client.setting

import org.kamired.client.KamiMod
import org.kamired.client.module.AbstractModule
import org.kamired.client.module.modules.client.Configurations
import org.kamired.client.setting.configs.NameableConfig
import java.io.File

internal object ModuleConfig : NameableConfig<AbstractModule>(
    "modules",
    "${KamiMod.DIRECTORY}config/modules",
) {
    override val file: File get() = File("$filePath/${Configurations.modulePreset}.json")
    override val backup get() = File("$filePath/${Configurations.modulePreset}.bak")
}