package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.awt.*;
import java.util.Random;

public class DVDIcon extends Module {

    private static DVDIcon instance;

    public DVDIcon() {
        super("DVDIcon",
                "Displays DVD™ Icon in guis",
                Category.RENDER
        );

        instance = this;
    }

    public static DVDIcon getInstance() {
        return instance != null ? instance : new DVDIcon();
    }

    public static final ResourceLocation DVD_ICON = new ResourceLocation(Main.MOD_ID, "dvd.png");

    public final Setting<Boolean> inChat = register(new Setting<>("InChat", true));
    public final Setting<Boolean> inOtherGui = register(new Setting<>("InOtherGui", true));
    public final Setting<Integer> speed = register(new Setting<>("Speed", 2, 1, 5));

    public int x = 69;
    public int y = 42;
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
        y = 42;
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
                1F
        );


        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 256, 256, 256, 256);
        GlStateManager.popMatrix();
    }

    public void updateColor() {
        dvdColor = new Color(new Random().nextInt(256),
                new Random().nextInt(256),
                new Random().nextInt(256)
        );
    }
}
