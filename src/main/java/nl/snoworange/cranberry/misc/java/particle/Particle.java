package nl.snoworange.cranberry.misc.java.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import nl.snoworange.cranberry.features.module.modules.render.Particles;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;

import java.util.Random;

/**
 * @author Vitox
 * @version 3.0
 */

public class Particle {
    public final float size;
    private float ySpeed = new Random().nextInt(Particles.getInstance().maxRandomXSpeed.getValue());
    private float xSpeed = new Random().nextInt(Particles.getInstance().maxRandomYSpeed.getValue());
    public float x;
    public float y;
    public int color;
    private int height;
    private int width;

    Particle(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = genRandom();
    }

    private float lint1(float f) {
        return ((float) 1.02 * (1.0f - f)) + (f);
    }

    private float lint2(float f) {
        return (float) 1.02 + f * ((float) 1.0 - (float) 1.02);
    }

    void connect(float x, float y) {
        RenderUtils.connectPoints(getX(),
                getY(),
                x,
                y,
                Particles.getInstance().lineWidth.getValue(),
                this.color);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    void interpolation() {
        for (int n = 0; n <= 64; ++n) {
            final float f = n / 64.0f;
            final float p1 = lint1(f);
            final float p2 = lint2(f);

            if (p1 != p2) {
                y -= f;
                x -= f;
            }
        }
    }

    void fall() {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        y = (y + ySpeed);
        x = (x + xSpeed);

        if (y > mc.displayHeight)
            y = 1;

        if (x > mc.displayWidth)
            x = 1;

        if (x < 1)
            x = scaledResolution.getScaledWidth();

        if (y < 1)
            y = scaledResolution.getScaledHeight();
    }

    private float genRandom() {
        return (float) (Particles.getInstance().minSize.getValue() + Math.random());
    }
}