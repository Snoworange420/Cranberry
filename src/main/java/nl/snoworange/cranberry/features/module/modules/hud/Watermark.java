package nl.snoworange.cranberry.features.module.modules.hud;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

import java.awt.*;

public class Watermark extends Module {

    private static Watermark instance;

    public Watermark() {
        super("Watermark", Category.HUD);

        instance = this;
    }

    public static Watermark getInstance() {
        return instance != null ? instance : new Watermark();
    }

    public enum Mode {
        ONLYNAME,
        INCLUDEVERSION;
    }

    public final Setting<Enum> mode = register(new Setting("Mode", Mode.INCLUDEVERSION));
    public final Setting<Integer> x = register(new Setting<>("X", 2, 0, 1080));
    public final Setting<Integer> y = register(new Setting<>("Y", 12, 0, 1080));
    public final Setting<Integer> red = register(new Setting<>("Red", 166, 0, 255));
    public final Setting<Integer> blue = register(new Setting<>("Blue", 10, 0, 255));
    public final Setting<Integer> green = register(new Setting<>("Green", 61, 0, 255));
    public final Setting<Boolean> dropShadow = register(new Setting<>("Dropshadow", true));


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (this.isEnabled()) {

            if (mc.gameSettings.showDebugInfo) return;

            mc.fontRenderer.drawString(Main.NAME + " " + (mode.getValue().equals(Mode.INCLUDEVERSION) ? Main.VERSION : ""),
                    x.getValue(),
                    y.getValue(),
                    new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB(),
                    dropShadow.getValue()
            );
        }
    }
}