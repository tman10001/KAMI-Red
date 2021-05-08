package org.kamired.client.module.modules.combat

import kotlinx.coroutines.launch
import net.minecraft.util.math.AxisAlignedBB
import org.kamired.client.event.SafeClientEvent
import org.kamired.client.event.events.RenderWorldEvent
import org.kamired.client.module.Category
import org.kamired.client.module.Module
import org.kamired.client.util.TickTimer
import org.kamired.client.util.color.ColorHolder
import org.kamired.client.util.combat.SurroundUtils
import org.kamired.client.util.combat.SurroundUtils.checkHole
import org.kamired.client.util.graphics.ESPRenderer
import org.kamired.client.util.graphics.GeometryMasks
import org.kamired.client.util.math.VectorUtils.toBlockPos
import org.kamired.client.util.threads.defaultScope
import org.kamired.client.util.threads.safeListener

internal object HoleESP : Module(
    name = "HoleESP",
    category = Category.COMBAT,
    description = "Show safe holes for crystal pvp"
) {
    private val range = setting("Render Distance", 8, 4..32, 1)
    private val filled = setting("Filled", true)
    private val outline = setting("Outline", false)
    private val hideOwn = setting("Hide Own", true)
    private val colorObsidian by setting("Obby Color", ColorHolder(150, 0, 0), false, visibility = { shouldAddObsidian() })
    private val colorBedrock by setting("Bedrock Color", ColorHolder(104, 0, 0), false, visibility = { shouldAddBedrock() })
    private val aFilled = setting("Filled Alpha", 31, 0..255, 1, { filled.value })
    private val aOutline = setting("Outline Alpha", 127, 0..255, 1, { outline.value })
    private val renderMode = setting("Mode", Mode.BLOCK_HOLE)
    private val holeType = setting("Hole Type", HoleType.BOTH)

    private enum class Mode {
        BLOCK_HOLE, BLOCK_FLOOR, FLAT
    }

    private enum class HoleType {
        OBSIDIAN, BEDROCK, BOTH
    }

    private val renderer = ESPRenderer()
    private val timer = TickTimer()

    init {
        safeListener<RenderWorldEvent> {
            if (timer.tick(133L)) { // Avoid running this on a tick
                updateRenderer()
            }
            renderer.render(false)
        }
    }

    private fun SafeClientEvent.updateRenderer() {
        renderer.aFilled = if (filled.value) aFilled.value else 0
        renderer.aOutline = if (outline.value) aOutline.value else 0

        val playerPos = player.positionVector.toBlockPos()
        val side = if (renderMode.value != Mode.FLAT) GeometryMasks.Quad.ALL
        else GeometryMasks.Quad.DOWN

        defaultScope.launch {
            val cached = ArrayList<Triple<AxisAlignedBB, ColorHolder, Int>>()

            for (x in -range.value..range.value) for (y in -range.value..range.value) for (z in -range.value..range.value) {
                if (hideOwn.value && x == 0 && y == 0 && z == 0) continue
                val pos = playerPos.add(x, y, z)

                val holeType = checkHole(pos)
                if (holeType == SurroundUtils.HoleType.NONE) continue

                val bb = AxisAlignedBB(if (renderMode.value == Mode.BLOCK_FLOOR) pos.down() else pos)

                if (holeType == SurroundUtils.HoleType.OBBY && shouldAddObsidian()) {
                    cached.add(Triple(bb, colorObsidian, side))
                }

                if (holeType == SurroundUtils.HoleType.BEDROCK && shouldAddBedrock()) {
                    cached.add(Triple(bb, colorBedrock, side))
                }
            }

            renderer.replaceAll(cached)
        }
    }

    private fun shouldAddObsidian() = holeType.value == HoleType.OBSIDIAN || holeType.value == HoleType.BOTH

    private fun shouldAddBedrock() = holeType.value == HoleType.BEDROCK || holeType.value == HoleType.BOTH

}