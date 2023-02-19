package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.item.ItemStack;
import nl.snoworange.cranberry.features.module.modules.render.CleanGUI;
import nl.snoworange.cranberry.features.module.modules.render.DVDIcon;
import nl.snoworange.cranberry.features.module.modules.render.Particles;
import nl.snoworange.cranberry.features.module.modules.render.Tooltip;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Shadow
    public int width;
    @Shadow
    public int height;

    @Inject(method = "renderToolTip",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderToolTip(final ItemStack itemStack, final int x, final int y, final CallbackInfo ci) {
        Tooltip.onRenderTooltip(itemStack, x, y, ci);
    }

    @Inject(method = "drawWorldBackground(I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void drawWorldBackgroundWrapper(int tint, final CallbackInfo ci) {

        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (Particles.getInstance().isEnabled()) Particles.getInstance().renderParticles(Mouse.getX() * width / Minecraft.getMinecraft().displayWidth,
                height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1);

        if ((Minecraft.getMinecraft().currentScreen instanceof GuiMultiplayer
                || Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu
                || Minecraft.getMinecraft().currentScreen instanceof GuiVideoSettings
                || Minecraft.getMinecraft().currentScreen instanceof GuiCreateWorld
                || Minecraft.getMinecraft().currentScreen instanceof GuiCreateFlatWorld
                || Minecraft.getMinecraft().currentScreen instanceof GuiConnecting
                || Minecraft.getMinecraft().currentScreen instanceof GuiSnooper
                || Minecraft.getMinecraft().currentScreen instanceof GuiDownloadTerrain
                || Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected)) return;

        if (DVDIcon.getInstance().isEnabled() && DVDIcon.getInstance().inOtherGui.getValue()) DVDIcon.getInstance().drawDVDIcon();

        if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer
                && CleanGUI.getInstance().isEnabled()
                && CleanGUI.getInstance().removeCGuiTint.getValue()
        ) {
            ci.cancel();
        }

        if (CleanGUI.getInstance().isEnabled() && CleanGUI.getInstance().removeAllGuiTint.getValue()) {
            ci.cancel();
        }
    }
}
