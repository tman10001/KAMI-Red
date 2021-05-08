package org.kamired.client.module.modules.misc

import club.minnced.discord.rpc.DiscordEventHandlers
import club.minnced.discord.rpc.DiscordRichPresence
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.capeapi.CapeType
import org.kamired.client.KamiMod
import org.kamired.client.event.events.ShutdownEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.InfoCalculator
import org.kamired.client.util.InfoCalculator.speed
import org.kamired.client.util.TickTimer
import org.kamired.client.util.TimeUnit
import org.kamired.client.util.TpsCalculator
import org.kamired.client.util.math.CoordinateConverter.asString
import org.kamired.client.util.math.VectorUtils.toBlockPos
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.threads.BackgroundJob
import org.kamired.client.util.threads.BackgroundScope
import org.kamired.client.util.threads.runSafeR
import org.kamired.client.util.threads.safeListener
import org.kamired.commons.utils.MathUtils
import org.kamired.event.listener.listener

internal object DiscordRPC : Module(
    name = "DiscordRPC",
    category = Category.MISC,
    description = "Discord Rich Presence",
    enabledByDefault = true
) {
    private val line1Left by setting("Line 1 Left", LineInfo.VERSION) // details left
    private val line1Right by setting("Line 1 Right", LineInfo.USERNAME) // details right
    private val line2Left by setting("Line 2 Left", LineInfo.SERVER_IP) // state left
    private val line2Right by setting("Line 2 Right", LineInfo.HEALTH) // state right
    private val coordsConfirm by setting("Coords Confirm", false, { showCoordsConfirm() })

    private enum class LineInfo {
        VERSION, WORLD, DIMENSION, USERNAME, HEALTH, HUNGER, SERVER_IP, COORDS, SPEED, HELD_ITEM, FPS, TPS, NONE
    }

    private val presence = DiscordRichPresence()
    private val rpc = club.minnced.discord.rpc.DiscordRPC.INSTANCE
    private var connected = false
    private val timer = TickTimer(TimeUnit.SECONDS)
    private val job = BackgroundJob("Discord RPC", 5000L) { updateRPC() }

    init {
        onEnable {
            start()
        }

        onDisable {
            end()
        }

        safeListener<TickEvent.ClientTickEvent> {
            if (showCoordsConfirm() && !coordsConfirm && timer.tick(10L)) {
                MessageSendHelper.sendWarningMessage("$chatName Warning: In order to use the coords option please enable the coords confirmation option. " +
                    "This will display your coords on the discord rpc. " +
                    "Do NOT use this if you do not want your coords displayed")
            }
        }

        listener<ShutdownEvent> {
            end()
        }
    }

    private fun start() {
        if (connected) return

        KamiMod.LOG.info("Starting Discord RPC")
        connected = true
        rpc.Discord_Initialize(KamiMod.APP_ID, DiscordEventHandlers(), true, "")
        presence.startTimestamp = System.currentTimeMillis() / 1000L

        BackgroundScope.launchLooping(job)

        KamiMod.LOG.info("Discord RPC initialised successfully")
    }

    private fun end() {
        if (!connected) return

        KamiMod.LOG.info("Shutting down Discord RPC...")
        BackgroundScope.cancel(job)
        connected = false
        rpc.Discord_Shutdown()
    }

    private fun showCoordsConfirm(): Boolean {
        return line1Left == LineInfo.COORDS
            || line2Left == LineInfo.COORDS
            || line1Right == LineInfo.COORDS
            || line2Right == LineInfo.COORDS
    }

    private fun updateRPC() {
        presence.details = getLine(line1Left) + getSeparator(0) + getLine(line1Right)
        presence.state = getLine(line2Left) + getSeparator(1) + getLine(line2Right)
        rpc.Discord_UpdatePresence(presence)
    }

    private fun getLine(line: LineInfo): String {
        return when (line) {
            LineInfo.VERSION -> {
                KamiMod.VERSION_SIMPLE
            }
            LineInfo.WORLD -> {
                when {
                    mc.isIntegratedServerRunning -> "Singleplayer"
                    mc.currentServerData != null -> "Multiplayer"
                    else -> "Main Menu"
                }
            }
            LineInfo.DIMENSION -> {
                InfoCalculator.dimension()
            }
            LineInfo.USERNAME -> {
                mc.session.username
            }
            LineInfo.HEALTH -> {
                if (mc.player != null) "${mc.player.health.toInt()} HP"
                else "No HP"
            }
            LineInfo.HUNGER -> {
                if (mc.player != null) "${mc.player.foodStats.foodLevel} hunger"
                else "No Hunger"
            }
            LineInfo.SERVER_IP -> {
                InfoCalculator.getServerType()
            }
            LineInfo.COORDS -> {
                if (mc.player != null && coordsConfirm) "(${mc.player.positionVector.toBlockPos().asString()})"
                else "No Coords"
            }
            LineInfo.SPEED -> {
                runSafeR {
                    "${"%.1f".format(speed())} m/s"
                } ?: "No Speed"
            }
            LineInfo.HELD_ITEM -> {
                "Holding ${mc.player?.heldItemMainhand?.displayName ?: "Air"}" // Holding air meme
            }
            LineInfo.FPS -> {
                "${Minecraft.getDebugFPS()} FPS"
            }
            LineInfo.TPS -> {
                if (mc.player != null) "${MathUtils.round(TpsCalculator.tickRate, 1)} tps"
                else "No Tps"
            }
            else -> {
                " "
            }
        }
    }

    private fun getSeparator(line: Int): String {
        return if (line == 0) {
            if (line1Left == LineInfo.NONE || line1Right == LineInfo.NONE) " " else " | "
        } else {
            if (line2Left == LineInfo.NONE || line2Right == LineInfo.NONE) " " else " | "
        }
    }

    fun setCustomIcons(capeType: CapeType?) {
        presence.smallImageKey = capeType?.imageKey ?: ""
        presence.smallImageText = when (capeType) {
            CapeType.BOOSTER -> "booster"
            CapeType.CONTEST -> "contest winner!"
            CapeType.CONTRIBUTOR -> "code contributor!"
            CapeType.DONOR -> "donator <3"
            CapeType.INVITER -> "inviter"
            CapeType.SPECIAL -> "special cape!"
            else -> ""
        }
    }

    init {
        presence.largeImageKey = "kamiblue"
        presence.largeImageText = "nigga"
    }
}
