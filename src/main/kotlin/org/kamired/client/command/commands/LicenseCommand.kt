package org.kamired.client.command.commands

import org.kamired.client.command.ClientCommand
import org.kamired.client.util.text.MessageSendHelper

object LicenseCommand : ClientCommand(
    name = "license",
    description = "Information about KAMI Blue's license"
) {
    init {
        execute {
            MessageSendHelper.sendChatMessage("You can view KAMI Blue's &7client&f License (LGPLv3) at &9https://kamiblue.org/license")
        }
    }
}