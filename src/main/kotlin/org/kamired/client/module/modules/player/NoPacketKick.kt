package org.kamired.client.module.modules.player

import org.kamired.client.mixin.client.network.MixinNetworkManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.text.MessageSendHelper.sendWarningMessage

/**
 * @see MixinNetworkManager
 */
internal object NoPacketKick : Module(
    name = "NoPacketKick",
    category = Category.PLAYER,
    description = "Suppress network exceptions and prevent getting kicked",
    showOnArray = false
) {
    @JvmStatic
    fun sendWarning(throwable: Throwable) {
        sendWarningMessage("$chatName Caught exception - \"$throwable\" check log for more info.")
        throwable.printStackTrace()
    }
}
