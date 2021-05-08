package org.kamired.client.gui.rgui.component

import org.kamired.client.util.graphics.AnimationUtils

open class BooleanSlider(
    name: String,
    valueIn: Double,
    description: String,
    visibility: (() -> Boolean)? = null
) : Slider(name, valueIn, description, visibility) {
    override val renderProgress: Double
        get() = AnimationUtils.exponent(AnimationUtils.toDeltaTimeDouble(prevValue.lastUpdateTime), 200.0, prevValue.value, value)
}