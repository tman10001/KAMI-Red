package org.kamired.client.event.events

import org.kamired.client.event.Event
import org.kamired.client.util.graphics.VertexHelper

class RenderRadarEvent(
    val vertexHelper: VertexHelper,
    val radius: Float,
    val scale: Float
) : Event