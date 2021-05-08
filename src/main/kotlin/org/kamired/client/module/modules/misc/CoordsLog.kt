package org.kamired.client.module.modules.misc

import net.minecraftforge.fml.common.gameevent.TickEvent
import org.kamired.client.manager.managers.WaypointManager
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.InfoCalculator
import org.kamired.client.util.TickTimer
import org.kamired.client.util.TimeUnit
import org.kamired.client.util.math.CoordinateConverter.asString
import org.kamired.client.util.math.VectorUtils.toBlockPos
import org.kamired.client.util.text.MessageSendHelper
import org.kamired.client.util.threads.safeListener

internal object CoordsLog : Module(
    name = "CoordsLog",
    description = "Automatically logs your coords, based on actions",
    category = Category.MISC
) {
    private val saveOnDeath = setting("Save On Death", true)
    private val autoLog = setting("Automatically Log", false)
    private val delay = setting("Delay", 15, 1..60, 1)

    private var previousCoord: String? = null
    private var savedDeath = false
    private var timer = TickTimer(TimeUnit.SECONDS)

    init {
        safeListener<TickEvent.ClientTickEvent> {
            if (autoLog.value && timer.tick(delay.value.toLong())) {
                val currentCoord = player.positionVector.toBlockPos().asString()

                if (currentCoord != previousCoord) {
                    WaypointManager.add("autoLogger")
                    previousCoord = currentCoord
                }
            }

            if (saveOnDeath.value) {
                savedDeath = if (player.isDead || player.health <= 0.0f) {
                    if (!savedDeath) {
                        val deathPoint = WaypointManager.add("Death - " + InfoCalculator.getServerType()).pos
                        MessageSendHelper.sendChatMessage("You died at ${deathPoint.x}, ${deathPoint.y}, ${deathPoint.z}")
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

}
