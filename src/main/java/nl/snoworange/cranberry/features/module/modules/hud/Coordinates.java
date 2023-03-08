package nl.snoworange.cranberry.features.module.modules.hud;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.awt.*;
import java.util.Random;

public class Coordinates extends Module {

    public Coordinates() {
        super("Coordinates",
                "displays your current player position",
                Category.HUD
        );
    }

    public final Setting<Boolean> spoof = register(new Setting<>("Spoof", false));
    public final Setting<Boolean> showNether = register(new Setting<>("NetherCoords", true));
    public final Setting<Integer> round = register(new Setting<>("Round", 1, 0, 8));

    public final Setting<Integer> x = register(new Setting<>("X", 2, 0, 1080));
    public final Setting<Integer> y = register(new Setting<>("Y", 512, 0, 1080));
    public final Setting<Integer> red = register(new Setting<>("Red", 166, 0, 255));
    public final Setting<Integer> blue = register(new Setting<>("Blue", 10, 0, 255));
    public final Setting<Integer> green = register(new Setting<>("Green", 61, 0, 255));
    public final Setting<Boolean> dropShadow = register(new Setting<>("Dropshadow", true));

    public int randomX = 0;
    public int randomZ = 0;

    @Override
    public void onEnable() {
        super.onEnable();

        randomX = new Random().nextInt(420420 + 420420) - 420420;
        randomZ = new Random().nextInt(420420 + 420420) - 420420;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (this.isEnabled()) {

            if (n() ||  mc.gameSettings.showDebugInfo) return;

            mc.fontRenderer.drawString((String.format("XYZ: %." + round.getValue() + "f, %." + round.getValue() + "f, %."  + round.getValue() + "f",
                    mc.player.posX + (spoof.getValue() ? randomX : 0),
                    mc.player.posY,
                    mc.player.posZ + (spoof.getValue() ? randomZ : 0))

                    + (showNether.getValue() ? (mc.player.dimension == 0 ? String.format(" [%." + round.getValue() + "f, %." + round.getValue() + "f]",
                            (((mc.player.posX + (spoof.getValue() ? randomX : 0)) / 8)),
                            (((mc.player.posZ + (spoof.getValue() ? randomZ : 0)) / 8))) :
                            String.format(" [%." + round.getValue() + "f, %." + round.getValue() + "f]",
                                    (((mc.player.posX + (spoof.getValue() ? randomX : 0)) * 8)),
                                    (((mc.player.posZ + (spoof.getValue() ? randomZ : 0)) * 8)))) : "")
                    ),
                    x.getValue(),
                    y.getValue(),
                    new Color(red.getValue(),
                    blue.getValue(),
                    green.getValue()
                    ).getRGB(), dropShadow.getValue()
            );
        }
    }
}
