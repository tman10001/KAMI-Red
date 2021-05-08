package org.kamired.client.module.modules.render

import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import org.kamired.client.manager.managers.FriendManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.color.EnumTextColor
import org.kamired.client.util.text.format

internal object TabFriends : Module(
    name = "TabFriends",
    description = "Highlights friends in the tab menu",
    category = Category.RENDER,
    showOnArray = false
) {
    private val color = setting("Color", EnumTextColor.GREEN)

    @JvmStatic
    fun getPlayerName(info: NetworkPlayerInfo): String {
        val name = info.displayName?.formattedText
            ?: ScorePlayerTeam.formatPlayerName(info.playerTeam, info.gameProfile.name)

        return if (FriendManager.isFriend(name)) {
            color.value format name
        } else {
            name
        }
    }
}