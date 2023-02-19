package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;

import java.awt.*;

public class Info32k extends Module {

    public Info32k() {
        super("32kInfo", Category.STRONKSWORDMETA);
    }

    public final Setting<Integer> x = register(new Setting<>("TextX", 100, 0, 2000));
    public final Setting<Integer> y = register(new Setting<>("TextY", 100, 0, 2000));

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
            mc.fontRenderer.drawString(((InventoryUtils.findHotbar32k() == -1 ? "No " : "") + "32k in hotbar!"),
                    x.getValue(),
                    y.getValue(),
                    (InventoryUtils.findHotbar32k() != -1 ? (new Color(0, 255, 42).getRGB()) : (new Color(255, 0, 0)).getRGB())
            );
        }
    }
}
