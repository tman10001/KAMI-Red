package org.kamired.client.module.modules.render

import net.minecraft.block.state.IBlockState
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.setting.settings.impl.collection.CollectionSetting

internal object Xray : Module(
    name = "Xray",
    description = "Lets you see through blocks",
    category = Category.RENDER
) {
    private val defaultVisibleList = linkedSetOf("minecraft:diamond_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:portal", "minecraft:cobblestone")

    val visibleList = setting(CollectionSetting("Visible List", defaultVisibleList, { false }))

    @JvmStatic
    fun shouldReplace(state: IBlockState): Boolean {
        return isEnabled && !visibleList.contains(state.block.registryName.toString())
    }

    init {
        onToggle {
            mc.renderGlobal.loadRenderers()
        }

        visibleList.editListeners.add {
            mc.renderGlobal.loadRenderers()
        }
    }
}
