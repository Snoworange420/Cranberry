package nl.snoworange.cranberry.features.notification.minecraft;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.List;

public class NotificationToast implements IToast {

    public String title;
    public String subTitle;
    public ItemStack displayStack;

    public NotificationToast(String title, String subTitle, ItemStack displayStack) {
        this.title = title;
        this.subTitle = subTitle;
        this.displayStack = displayStack;
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta) {

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);

        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

        if (title != null && subTitle != null && displayStack != null) {

            List<String> list = toastGui.getMinecraft().fontRenderer.listFormattedStringToWidth(subTitle, 125);

            int i = 16776960;

            if (list.size() == 1) {
                toastGui.getMinecraft().fontRenderer.drawString(title, 30, 7, i | -16777216);
                toastGui.getMinecraft().fontRenderer.drawString(subTitle, 30, 18, -1);
            } else {
                int i1;
                if (delta < 1500L) {
                    i1 = MathHelper.floor(MathHelper.clamp((float)(1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    toastGui.getMinecraft().fontRenderer.drawString(title, 30, 11, i | i1);
                } else {
                    i1 = MathHelper.floor(MathHelper.clamp((float)(delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

                    for (Iterator var11 = list.iterator(); var11.hasNext(); l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT) {
                        String s = (String)var11.next();
                        toastGui.getMinecraft().fontRenderer.drawString(s, 30, l, 16777215 | i1);
                    }
                }
            }

            RenderHelper.enableGUIStandardItemLighting();
            if (displayStack != null) toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null, displayStack, 8, 8);

            return delta >= 3000L ? Visibility.HIDE : Visibility.SHOW;
        } else {
            return Visibility.HIDE;
        }
    }
}