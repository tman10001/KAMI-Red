package org.kamired.client.setting.configs

import org.kamired.client.setting.settings.AbstractSetting
import org.kamired.commons.interfaces.Nameable

open class NameableConfig<T : Nameable>(
    name: String,
    filePath: String
) : AbstractConfig<T>(name, filePath) {

    override fun addSettingToConfig(owner: T, setting: AbstractSetting<*>) {
        getGroupOrPut(owner.name).addSetting(setting)
    }

    open fun getSettings(nameable: Nameable) = getGroup(nameable.name)?.getSettings() ?: emptyList()

}
