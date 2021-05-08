package org.kamired.client.event.events

import net.minecraft.util.math.BlockPos
import org.kamired.client.event.Event

class BlockBreakEvent(val breakerID: Int, val position: BlockPos, val progress: Int) : Event