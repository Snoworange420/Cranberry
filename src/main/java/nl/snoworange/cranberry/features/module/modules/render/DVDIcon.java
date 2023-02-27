package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

import java.awt.*;
import java.util.Random;

/**
 * @author Snoworange
 */

public class DVDIcon extends Module {

    private static DVDIcon instance;

    public DVDIcon() {
        super("DVDIcon",
                "Displays DVD™ Icon in guis",
                Category.RENDER
        );

        instance = this;
    }

    public enum ColorMode {
        RANDOM,
        CLASSIC;
    }

    public static DVDIcon getInstance() {
        return instance != null ? instance : new DVDIcon();
    }

    public static final ResourceLocation DVD_ICON = new ResourceLocation(Main.MOD_ID, "dvd.png");
    public static final Color[] classicColors = new Color[] {
            new Color(255, 38, 0),
            new Color(255, 131, 0),
            new Color(255, 250, 1),
            new Color(37, 255, 1),
            new Color(0, 254, 255),
            new Color(0, 38, 255),
            new Color(190, 0, 255),
            new Color(255, 0, 139)
    };

    public final Setting<Enum> colorMode = register(new Setting<>("ColorMode", ColorMode.RANDOM));
    public final Setting<Boolean> inChat = register(new Setting<>("InChat", true));
    public final Setting<Boolean> inOtherGui = register(new Setting<>("InOtherGui", true));
    public final Setting<Integer> speed = register(new Setting<>("Speed", 2, 1, 5));

    public int x = 69;
    public int y = 69;
    public int vx = 2;
    public int vy = 2;

    @Override
    public void onEnable() {
        super.onEnable();

        vx = speed.getValue();
        vy = speed.getValue();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        x = 69;
        y = 69;
    }

    public Color dvdColor = new Color(212, 61, 89);

    public void drawDVDIcon() {

        x += vx;
        y += vy;

        ScaledResolution sr = new ScaledResolution(mc);

        if (x < -6 || x > sr.getScaledWidth() - 252) {
            updateColor();
            vx *= -1;
        }

        if (y < -74 || y > sr.getScaledHeight() - 184) {
            updateColor();
            vy *= -1;
        }

        mc.getTextureManager().bindTexture(DVD_ICON);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color( (float) dvdColor.getRed() / 255,
                (float) dvdColor.getBlue() / 255,
                (float) dvdColor.getGreen() / 255,
                1f
        );


        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 256, 256, 256, 256);
        GlStateManager.popMatrix();
    }

    public void updateColor() {

        if (colorMode.getValue().equals(ColorMode.RANDOM)) {
            dvdColor = new Color(new Random().nextInt(256),
                    new Random().nextInt(256),
                    new Random().nextInt(256)
            );
        }

        if (colorMode.getValue().equals(ColorMode.CLASSIC))  {
            int random = new Random().nextInt(classicColors.length);
            dvdColor = classicColors[random];
        }

    }
}
