package org.kamired.client.command.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kamired.client.KamiMod
import org.kamired.client.command.ClientCommand
import org.kamired.client.module.AbstractModule
import org.kamired.client.module.Category
import org.kamired.client.module.ModuleManager
import org.kamired.client.util.text.MessageSendHelper
import java.io.File
import java.util.*

object GenerateWebsiteCommand : ClientCommand(
    name = "generatewebsite",
    description = "Generates the website modules to the file"
) {
    private const val path = "${KamiMod.DIRECTORY}modules.md"

    init {
        executeAsync {
            val modulesList = ModuleManager.modules
            val moduleMap = TreeMap<Category, MutableList<AbstractModule>>()
            modulesList.groupByTo(moduleMap) { it.category }

            coroutineScope {
                launch(Dispatchers.IO) {
                    val file = File(path)
                    if (!file.exists()) file.createNewFile()
                    file.bufferedWriter().use {
                        it.appendLine("---")
                        it.appendLine("layout: default")
                        it.appendLine("title: Modules")
                        it.appendLine("description: A list of modules and commands this mod has")
                        it.appendLine("---")
                        it.appendLine("## Modules (${modulesList.size})")
                        it.newLine()

                        for ((category, modules) in moduleMap) {
                            it.appendLine("<details>")
                            it.appendLine("    <summary>$category (${modules.size})</summary>")
                            it.appendLine("    <p><ul>")
                            for (module in modules) {
                                it.appendLine("        <li>${module.name}<p><i>${module.description}</i></p></li>")
                            }
                            it.appendLine("    </ul></p>")
                            it.appendLine("</details>")
                        }
                    }
                }
            }

            MessageSendHelper.sendChatMessage("Generated website to .minecraft/$path!")
        }
    }
}