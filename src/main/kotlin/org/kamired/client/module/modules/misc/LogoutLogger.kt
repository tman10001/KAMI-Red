package org.kamired.client.module.modules.misc

import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.event.events.ConnectionEvent
import org.kamired.client.manager.managers.WaypointManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.EntityUtils.flooredPosition
import org.kamired.client.util.EntityUtils.isFakeOrSelf
import org.kamired.client.util.TickTimer
import org.kamired.client.util.TimeUnit
import org.kamired.client.util.math.CoordinateConverter.asString
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.threads.onMainThread
import org.kamired.client.util.threads.safeListener
import org.kamired.event.listener.asyncListener

internal object LogoutLogger : Module(
    name = "LogoutLogger",
    category = Category.MISC,
    description = "Logs when a player leaves the game"
) {
    private val saveWaypoint by setting("Save Waypoint", true)
    private val print by setting("Print To Chat", true)

    private val loggedPlayers = LinkedHashMap<GameProfile, BlockPos>()
    private val timer = TickTimer(TimeUnit.SECONDS)

    init {
        asyncListener<ConnectionEvent.Disconnect> {
            onMainThread {
                loggedPlayers.clear()
            }
        }

        safeListener<TickEvent.ClientTickEvent> {
            if (it.phase != TickEvent.Phase.END) return@safeListener

            for (loadedPlayer in world.playerEntities) {
                if (loadedPlayer !is EntityOtherPlayerMP) continue
                if (loadedPlayer.isFakeOrSelf) continue

                val info = connection.getPlayerInfo(loadedPlayer.gameProfile.id) ?: continue
                loggedPlayers[info.gameProfile] = loadedPlayer.flooredPosition
            }

            if (timer.tick(1L)) {
                val toRemove = ArrayList<GameProfile>()

                loggedPlayers.entries.removeIf { (profile, pos) ->
                    @Suppress("SENSELESS_COMPARISON")
                    if (connection.getPlayerInfo(profile.id) == null) {
                        if (saveWaypoint) WaypointManager.add(pos, "${profile.name} Logout Spot")
                        if (print) MessageSendHelper.sendChatMessage("${profile.name} logged out at ${pos.asString()}")
                        true
                    } else {
                        false
                    }
                }

                loggedPlayers.keys.removeAll(toRemove)
            }
        }
    }
}