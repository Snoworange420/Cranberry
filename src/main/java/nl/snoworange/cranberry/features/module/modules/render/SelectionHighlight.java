package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.ColorUtils;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;

import java.awt.*;

public class SelectionHighlight extends Module {

    public SelectionHighlight() {
        super("SelectionHighlight",
                "highlights block where are you aim at",
                Category.RENDER
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public final Setting<Integer> h1 = register(new Setting<>("Hue1", 0, 0, 360));
    public final Setting<Integer> s1 = register(new Setting<>("Saturation1", 360, 0, 360));
    public final Setting<Integer> l1 = register(new Setting<>("Lightness1", 280, 0, 360));
    public final Setting<Integer> h2 = register(new Setting<>("Hue2", 45, 0, 360));
    public final Setting<Integer> s2 = register(new Setting<>("Saturation2", 320, 0, 360));
    public final Setting<Integer> l2 = register(new Setting<>("Lightness2", 290, 0, 360));
    public final Setting<Float> lineWidth = register(new Setting<>("LineWidth", 3.0f, 0.1f, 7.5f));
    public final Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public final Setting<Integer> rainbowSpeed = register(new Setting<>("RainbowSpeed", 75, 0, 100));
    public final Setting<Integer> rainbowDelay = register(new Setting<>("RainbowDelay", 450, 0, 1000));


    //TODO: highlight the "real" bounding box of a block
    @Override
    public void onRender3d() {

        if (n()) return;

        try {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {

                BlockPos renderPos = mc.objectMouseOver.getBlockPos();

                if (rainbow.getValue()) {
                    RenderUtils.drawBlockOutline(renderPos,
                            0.0,
                            lineWidth.getValue(),
                            new Color(ColorUtils.rainbow(rainbowSpeed.getValue(),
                                    s1.getValue(),
                                    l1.getValue())),
                            new Color(ColorUtils.rainbow(rainbowSpeed.getValue() + rainbowDelay.getValue(), s2.getValue(), l2.getValue()))
                    );
                } else {
                    RenderUtils.drawBlockOutline(renderPos,
                            0.0,
                            lineWidth.getValue(),
                            Color.getHSBColor((float) h1.getValue() / 360,
                                    (float) s1.getValue() / 360,
                                    (float) l1.getValue() / 360
                            ),
                            Color.getHSBColor((float) h2.getValue() / 360,
                                    (float) s2.getValue() / 360,
                                    (float) l2.getValue() / 360
                            ));
                }
            }
        } catch (NullPointerException ignored) {}
    }
}