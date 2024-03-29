package org.kamired.client.event

import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import org.kamired.client.command.CommandManager
import org.kamired.client.event.events.BaritoneCommandEvent
import org.kamired.client.event.events.ConnectionEvent
import org.kamired.client.event.events.RenderWorldEvent
import org.kamired.client.event.events.ResolutionUpdateEvent
import org.kamired.client.gui.mc.KamiGuiChat
import org.kamired.client.module.ModuleManager
import org.kamired.client.util.Wrapper
import org.kamired.client.util.graphics.KamiTessellator
import org.kamired.client.util.graphics.ProjectionUtils
import org.kamired.client.util.text.MessageDetection
import org.lwjgl.input.Keyboard
import java.util.*

internal object ForgeEventProcessor {
    private val mc = Wrapper.minecraft
    private var prevWidth = mc.displayWidth
    private var prevHeight = mc.displayHeight

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            mc.profiler.startSection("kbTickPre")
        } else {
            mc.profiler.startSection("kbTickPost")
        }

        KamiEventBus.postProfiler(event)

        if (event.phase == TickEvent.Phase.END && (prevWidth != mc.displayWidth || prevHeight != mc.displayHeight)) {
            prevWidth = mc.displayWidth
            prevHeight = mc.displayHeight
            KamiEventBus.post(ResolutionUpdateEvent(mc.displayWidth, mc.displayHeight))
        }

        mc.profiler.endSection()
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onWorldRender(event: RenderWorldLastEvent) {
        ProjectionUtils.updateMatrix()
        KamiTessellator.prepareGL()
        KamiEventBus.post(RenderWorldEvent())
        KamiTessellator.releaseGL()
    }

    @SubscribeEvent
    fun onRenderPre(event: RenderGameOverlayEvent.Pre) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (!Keyboard.getEventKeyState()) return

        if (!mc.gameSettings.keyBindSneak.isKeyDown) {
            val prefix = CommandManager.prefix
            val typedChar = Keyboard.getEventCharacter().toString()
            if (prefix.length == 1 && typedChar.equals(CommandManager.prefix, true)) {
                mc.displayGuiScreen(KamiGuiChat(CommandManager.prefix))
            }
        }

        KamiEventBus.post(event)
        ModuleManager.onBind(Keyboard.getEventKey())
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChatSent(event: ClientChatEvent) {
        MessageDetection.Command.BARITONE.removedOrNull(event.message)?.let {
            KamiEventBus.post(BaritoneCommandEvent(it.toString().substringBefore(' ').toLowerCase(Locale.ROOT)))
        }

        if (MessageDetection.Command.KAMI_BLUE detect event.message) {
            CommandManager.runCommand(event.message.removePrefix(CommandManager.prefix))
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onEventMouse(event: InputEvent.MouseInputEvent) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onChunkLoaded(event: ChunkEvent.Unload) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onInputUpdate(event: InputUpdateEvent) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onLivingEntityUseItemEventTick(event: LivingEntityUseItemEvent.Tick) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onRenderBlockOverlay(event: RenderBlockOverlayEvent) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    fun onClientChat(event: ClientChatReceivedEvent) {
        KamiEventBus.post(event)
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onServerDisconnect(event: FMLNetworkEvent.ServerDisconnectionFromClientEvent) {
        KamiEventBus.post(ConnectionEvent.Disconnect())
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onClientDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        KamiEventBus.post(ConnectionEvent.Disconnect())
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onClientConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        KamiEventBus.post(ConnectionEvent.Connect())
    }

    @SubscribeEvent
    fun onRenderFogColors(event: EntityViewRenderEvent.FogColors) {
        KamiEventBus.post(event)
    }
}
