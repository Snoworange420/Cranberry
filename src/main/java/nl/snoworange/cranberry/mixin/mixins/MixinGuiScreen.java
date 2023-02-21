package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import nl.snoworange.cranberry.features.module.modules.render.CleanGUI;
import nl.snoworange.cranberry.features.module.modules.render.DVDIcon;
import nl.snoworange.cranberry.features.module.modules.render.Particles;
import nl.snoworange.cranberry.features.module.modules.render.Tooltip;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

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

    @Redirect(method = "getItemToolTip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;")
    )
    private List<String> getItemTooltip(ItemStack instance, EntityPlayer entityPlayer, ITooltipFlag nbttagcompound) {
        if (Tooltip.getInstance().isEnabled() && Tooltip.getInstance().displayNbtTags.getValue().isDown()) {
            List<String> nbtList = new ArrayList<>();

            if (instance.getTagCompound() != null) {
                nbtList.add(TextFormatting.GRAY + instance.getTagCompound().toString());
            } else {
                nbtList.add(TextFormatting.GRAY + "{}");
            }

            return nbtList;
        }

        List<String> list = instance.getTooltip(Minecraft.getMinecraft().player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, instance.getRarity().color + (String) list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + (String) list.get(i));
            }
        }

        return list;
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
