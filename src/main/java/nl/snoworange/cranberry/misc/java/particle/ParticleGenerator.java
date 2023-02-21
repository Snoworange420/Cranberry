package nl.snoworange.cranberry.misc.java.particle;

import net.minecraft.client.Minecraft;
import nl.snoworange.cranberry.features.module.modules.render.Particles;
import nl.snoworange.cranberry.util.ColorUtils;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Vitox
 * @version 3.0
 */

public class ParticleGenerator {

    private final List<Particle> particles = new ArrayList<>();
    private final int amount;

    private int prevWidth;
    private int prevHeight;

    public ParticleGenerator(final int amount) {
        this.amount = amount;
    }

    public void reset() {
        if (!particles.isEmpty()) {
            particles.clear();
        }
    }

    public void draw(final int mouseX, final int mouseY) {
        if (particles.isEmpty() || prevWidth != Minecraft.getMinecraft().displayWidth || prevHeight != Minecraft.getMinecraft().displayHeight) {
            particles.clear();
            create();
        }

        prevWidth = Minecraft.getMinecraft().displayWidth;
        prevHeight = Minecraft.getMinecraft().displayHeight;

        for (final Particle particle : particles) {
            particle.fall();
            particle.interpolation();

            int range = 50;
            final boolean mouseOver = (mouseX >= particle.x - range) && (mouseY >= particle.y - range) && (mouseX <= particle.x + range) && (mouseY <= particle.y + range);

            if (!mouseOver) {
                particles.stream()
                        .filter(part -> (part.getX() > particle.getX() && part.getX() - particle.getX() < range
                                && particle.getX() - part.getX() < range)
                                && (part.getY() > particle.getY() && part.getY() - particle.getY() < range
                                || particle.getY() > part.getY() && particle.getY() - part.getY() < range))
                        .forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }

            RenderUtils.drawCircle2d(particle.getX(), particle.getY(), particle.size, particle.color);
        }
    }

    private void create() {
        final Random random = new Random();

        final int[] counter = {1};
        for (int i = 0; i < amount; i++) {
            particles.add(new Particle(random.nextInt(Minecraft.getMinecraft().displayWidth),
                    random.nextInt(Minecraft.getMinecraft().displayHeight),
                    Particles.getInstance().rainbow.getValue() ? ColorUtils.rainbow(counter[0] * Particles.getInstance().rainbowSpeed.getValue(),
                            Particles.getInstance().s.getValue(),
                            Particles.getInstance().l.getValue()
                    ) : Color.getHSBColor((float) Particles.getInstance().h.getValue() / 360,
                            (float) Particles.getInstance().s.getValue() / 360,
                            (float) Particles.getInstance().l.getValue() / 360
                    ).getRGB()
            ));
            counter[0]++;
        }
    }
}
