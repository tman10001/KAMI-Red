package org.kamired.client.command.commands

import org.kamired.client.command.ClientCommand
import org.kamired.client.util.text.MessageSendHelper.sendServerMessage

object SayCommand : ClientCommand(
    name = "say",
    description = "Allows you to send any message, even with a prefix in it."
) {
    init {
        greedy("message") { messageArg ->
            executeSafe {
                sendServerMessage(messageArg.value.trim())
            }
        }
    }
}