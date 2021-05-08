package org.kamired.client.setting

import org.kamired.client.KamiMod
import org.kamired.client.setting.configs.NameableConfig

internal object GenericConfig : NameableConfig<GenericConfigClass>(
    "generic",
    "${KamiMod.DIRECTORY}config/"
)