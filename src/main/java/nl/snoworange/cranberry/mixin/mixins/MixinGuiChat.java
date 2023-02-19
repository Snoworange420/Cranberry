package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.gui.GuiChat;
import nl.snoworange.cranberry.features.module.modules.render.DVDIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {

    @Inject(method = "drawScreen",
            at = @At("HEAD")
    )
    private void drawDVDIcon(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (DVDIcon.getInstance().isEnabled() && DVDIcon.getInstance().inChat.getValue()) DVDIcon.getInstance().drawDVDIcon();
    }
}
