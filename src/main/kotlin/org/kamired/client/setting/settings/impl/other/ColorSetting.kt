package org.kamired.client.setting.settings.impl.other

import org.kamired.client.setting.settings.MutableSetting
import org.kamired.client.util.color.ColorHolder

class ColorSetting(
    name: String,
    value: ColorHolder,
    val hasAlpha: Boolean = true,
    visibility: () -> Boolean = { true },
    description: String = ""
) : MutableSetting<ColorHolder>(name, value, visibility, { _, input -> if (!hasAlpha) input.apply { a = 255 } else input }, description)