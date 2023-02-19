package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.util.math.BlockPos;
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

    public final Setting<Integer> h1 = register(new Setting<>("Hue1", 270, 0, 360));
    public final Setting<Integer> s1 = register(new Setting<>("Saturation1", 287, 0, 360));
    public final Setting<Integer> l1 = register(new Setting<>("Lightness1", 220, 0, 360));
    public final Setting<Integer> h2 = register(new Setting<>("Hue2", 220, 0, 360));
    public final Setting<Integer> s2 = register(new Setting<>("Saturation2", 200, 0, 360));
    public final Setting<Integer> l2 = register(new Setting<>("Lightness2", 230, 0, 360));
    public final Setting<Float> lineWidth = register(new Setting<>("LineWidth", 2.5f, 0.1f, 7.5f));
    public final Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public final Setting<Integer> rainbowSpeed = register(new Setting<>("RainbowSpeed", 75, 0, 100));
    public final Setting<Integer> rainbowDelay = register(new Setting<>("RainbowDelay", 450, 0, 1000));


    //TODO: highlight the "real" bounding box of a block
    @Override
    public void onRender3d() {

        if (n()) return;

        try {
            if (mc.objectMouseOver != null && mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getMaterial().isSolid()) {

                BlockPos renderPos = mc.objectMouseOver.getBlockPos();

                if (rainbow.getValue()) {
                    RenderUtils.drawBlockOutline(renderPos,
                            0.0,
                            lineWidth.getValue(),
                            new Color(ColorUtils.rainbow(rainbowSpeed.getValue(), s1.getValue(), h1.getValue())),
                            new Color(ColorUtils.rainbow(rainbowSpeed.getValue() + rainbowDelay.getValue(), s2.getValue(), h2.getValue()))
                    );
                } else {
                    RenderUtils.drawBlockOutline(renderPos,
                            0.0,
                            lineWidth.getValue(),
                            Color.getHSBColor(h1.getValue(),
                                    s1.getValue(),
                                    l1.getValue()
                            ),
                            Color.getHSBColor(h2.getValue(),
                                    s2.getValue(),
                                    l2.getValue()
                            ));
                }
            }
        } catch (NullPointerException ignored) {}
    }
}
