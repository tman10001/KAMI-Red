package org.kamired.client.mixin.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import org.kamired.client.gui.mc.KamiGuiAntiDisconnect;
import org.kamired.client.module.modules.misc.AntiDisconnect;
import org.kamired.client.util.Wrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu {

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.id == 1) {
            if (AntiDisconnect.INSTANCE.isEnabled()) {
                Wrapper.getMinecraft().displayGuiScreen(new KamiGuiAntiDisconnect());

                callbackInfo.cancel();
            }
        }
    }
}
