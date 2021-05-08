package org.kamired.client.gui.rgui.windows

import org.kamired.client.module.modules.client.ClickGUI
import org.kamired.client.module.modules.client.GuiColors
import org.kamired.client.setting.GuiConfig
import org.kamired.client.setting.configs.AbstractConfig
import org.kamired.client.util.graphics.RenderUtils2D
import org.kamired.client.util.graphics.VertexHelper
import org.kamired.client.util.math.Vec2d
import org.kamired.client.util.math.Vec2f
import org.kamired.commons.interfaces.Nameable

/**
 * Window with rectangle rendering
 */
open class BasicWindow(
    name: String,
    posX: Float,
    posY: Float,
    width: Float,
    height: Float,
    settingGroup: SettingGroup,
    config: AbstractConfig<out Nameable> = GuiConfig
) : CleanWindow(name, posX, posY, width, height, settingGroup, config) {

    override fun onRender(vertexHelper: VertexHelper, absolutePos: Vec2f) {
        super.onRender(vertexHelper, absolutePos)
        RenderUtils2D.drawRoundedRectFilled(vertexHelper, Vec2d(0.0, 0.0), Vec2f(renderWidth, renderHeight).toVec2d(), ClickGUI.radius, color = GuiColors.backGround)
        RenderUtils2D.drawRoundedRectOutline(vertexHelper, Vec2d(0.0, 0.0), Vec2f(renderWidth, renderHeight).toVec2d(), ClickGUI.radius, lineWidth = 2.5f, color = GuiColors.primary)
    }

}