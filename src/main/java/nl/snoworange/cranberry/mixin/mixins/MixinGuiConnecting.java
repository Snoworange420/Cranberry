package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import nl.snoworange.cranberry.features.module.modules.misc.ConnectingGuiPlus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {

    @Redirect(method = "drawScreen",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/GuiConnecting;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V")
    )
    private void onDrawScreen(GuiConnecting instance, FontRenderer fontRenderer, String s, int i, int j, int k) {
        instance.drawCenteredString(instance.mc.fontRenderer,
                (ConnectingGuiPlus.getInstance().isEnabled() && ConnectingGuiPlus.currentServerData != null) ?
                        ("Connecting to " + ConnectingGuiPlus.currentServerData.serverIP + "...")
                : I18n.format("connect.connecting"),
                instance.width / 2,
                instance.height / 2 - 50,
                16777215
        );
    }
}
