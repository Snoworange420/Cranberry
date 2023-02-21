package nl.snoworange.cranberry.features.module.modules.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.ColorUtils;

import java.awt.*;
import java.util.Comparator;

public class ArrayList extends Module {

    public ArrayList() {
        super("ArrayList", Category.HUD);
    }

    public final Setting<Integer> x = register(new Setting<>("X", 5, 0, 1920));
    public final Setting<Integer> y = register(new Setting<>("Y", 150, 0, 1920));
    public final Setting<Integer> h = register(new Setting<>("Hue", 340, 0, 360));
    public final Setting<Integer> s = register(new Setting<>("Saturation", 241, 0, 360));
    public final Setting<Integer> l = register(new Setting<>("Lightness", 304, 0, 360));
    public final Setting<Boolean> dropShadow = register(new Setting<>("Dropshadow", true));
    public final Setting<Boolean> rainbowColor = register(new Setting<>("Rainbow", true));
    public final Setting<Integer> rainbowSpeed = register(new Setting<>("RainbowSpeed", 100, 0, 255));
    public final Setting<Boolean> fromDown = register(new Setting<>("FromDown", false));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static class ModuleComparator implements Comparator<Module> {

        @Override
        public int compare(Module arg0, Module arg1) {
            return Integer.compare(Minecraft.getMinecraft().fontRenderer.getStringWidth(arg1.getName()), Minecraft.getMinecraft().fontRenderer.getStringWidth(arg0.getName()));
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (this.isEnabled()) {

            if (mc.gameSettings.showDebugInfo) return;

            java.util.ArrayList<Module> modules = new java.util.ArrayList<>(Main.moduleManager.getModules());

            modules.sort(new ModuleComparator());

            int sy = y.getValue();
            final int[] counter = {1};
            for (Module module : modules) {
                if (module.isEnabled()) {
                    mc.fontRenderer.drawString(module.getDisplayName(),
                            x.getValue(),
                            sy, rainbowColor.getValue() ?
                                    ColorUtils.rainbow(counter[0] * rainbowSpeed.getValue(),
                                            s.getValue(),
                                            l.getValue()
                                    ) : Color.getHSBColor((float) h.getValue() / 360,
                                    (float) s.getValue() / 360,
                                    (float) l.getValue() / 360
                            ).getRGB(),
                            dropShadow.getValue()
                    );

                    if (fromDown.getValue()) {
                        sy -= mc.fontRenderer.FONT_HEIGHT;
                    } else {
                        sy += mc.fontRenderer.FONT_HEIGHT;
                    }

                    counter[0]++;
                }
            }
        }
    }
}