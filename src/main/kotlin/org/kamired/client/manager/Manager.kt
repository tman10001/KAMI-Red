package org.kamired.client.manager

import org.kamired.client.util.Wrapper

interface Manager {
    val mc get() = Wrapper.minecraft
}