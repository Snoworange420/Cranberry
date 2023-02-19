package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import nl.snoworange.cranberry.features.module.modules.render.CleanGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Redirect(method = "drawChat",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V")
    )
    private void drawRectBackgroundClean(int left, int top, int right, int bottom, int color) {
        if (!(CleanGUI.getInstance().isEnabled() && CleanGUI.getInstance().removeChatTint.getValue())) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
