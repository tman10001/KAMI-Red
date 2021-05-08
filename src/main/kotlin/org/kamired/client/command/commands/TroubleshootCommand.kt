package org.kamired.client.command.commands

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraftforge.common.ForgeVersion
import org.kamired.client.KamiMod
import org.kamired.client.command.ClientCommand
import org.kamired.client.module.ModuleManager
import org.kamired.client.util.text.MessageSendHelper
import org.lwjgl.opengl.GL11

object TroubleshootCommand : ClientCommand(
    name = "troubleshoot",
    alias = arrayOf("tsc"),
    description = "Prints troubleshooting information"
) {
    init {
        execute("Print troubleshooting information") {
            MessageSendHelper.sendErrorMessage("&l&cSend a screenshot of all information below this line!")
            MessageSendHelper.sendChatMessage("Enabled Modules:\n" + ModuleManager.modules.filter { it.isEnabled }.joinToString { it.name })
            MessageSendHelper.sendChatMessage("Forge ${ForgeVersion.getMajorVersion()}.${ForgeVersion.getMinorVersion()}.${ForgeVersion.getRevisionVersion()}.${ForgeVersion.getBuildVersion()}")
            MessageSendHelper.sendChatMessage("${KamiMod.NAME} ${KamiMod.KAMI_KATAKANA} ${KamiMod.VERSION}")
            MessageSendHelper.sendChatMessage("CPU: ${OpenGlHelper.getCpu()} GPU: ${GlStateManager.glGetString(GL11.GL_VENDOR)}")
            MessageSendHelper.sendErrorMessage("&l&cPlease send a screenshot of the full output to the developer or moderator who's helping you!")
        }
    }
}
