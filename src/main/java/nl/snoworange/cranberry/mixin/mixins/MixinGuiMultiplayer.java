package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import nl.snoworange.cranberry.features.module.modules.misc.BetterConnectingGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer {

    @Inject(method = "connectToServer",
            at = @At("HEAD")
    )
    private void onConnectToServer(ServerData server, CallbackInfo ci) {
        BetterConnectingGUI.currentServerData = server;
    }
}
