package org.kamired.client

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kamired.client.event.ForgeEventProcessor
import org.kamired.client.util.ConfigUtils
import org.kamired.client.util.threads.BackgroundScope

@Mod(
    modid = KamiMod.ID,
    name = KamiMod.NAME,
    version = KamiMod.VERSION
)
class KamiMod {

    companion object {
        const val NAME = "KAMI Red"
        const val ID = "kamired"
        const val DIRECTORY = "kamired/"

        const val VERSION = "0.0.1" // Used for debugging. R.MM.DD-hash format.
        const val VERSION_SIMPLE = "0.0.1" // Shown to the user. R.MM.DD[-beta] format.
        const val VERSION_MAJOR = "0.0.1" // Used for update checking. RR.MM.01 format.
        const val BUILD_NUMBER = -1 // Do not remove, currently unused but will be used in the future.

        const val APP_ID = "835235017134440458"

        const val DOWNLOADS_API = "https://pastebin.pl/view/raw/f7a94e18"
        const val GITHUB_LINK = "https://github.com/kami-red"
        const val WEBSITE_LINK = "https://kamired.org"

        const val KAMI_KATAKANA = "ናይጋ"

        val LOG: Logger = LogManager.getLogger(NAME)

        var ready: Boolean = false; private set
    }


    @Suppress("UNUSED_PARAMETER")
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        LOG.info("Initializing $NAME $VERSION")

        LoaderWrapper.loadAll()

        MinecraftForge.EVENT_BUS.register(ForgeEventProcessor)

        ConfigUtils.moveAllLegacyConfigs()
        ConfigUtils.loadAll()

        BackgroundScope.start()

        LOG.info("$NAME initialized!")
    }

    @Suppress("UNUSED_PARAMETER")
    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        ready = true
    }
}