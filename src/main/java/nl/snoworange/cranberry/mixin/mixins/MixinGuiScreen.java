package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import nl.snoworange.cranberry.features.module.modules.render.Tooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = { "renderToolTip" },
            at = {@At("HEAD")},
            cancellable = true
    )
    public void renderToolTip(final ItemStack itemStack, final int x, final int y, final CallbackInfo ci) {
        Tooltip.onRenderTooltip(itemStack, x, y, ci);
    }
}
