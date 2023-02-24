package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.misc.java.particle.ParticleGenerator;

public class Particles extends Module {

    private static Particles instance;

    public Particles() {
        super("Particles",
                "LAGGGGGGGGGGG",
                Category.RENDER
        );

        instance = this;
    }

    public static Particles getInstance() {
        return instance != null ? instance : new Particles();
    }

    public final Setting<Integer> amount = register(new Setting<>("ParticleAmount", 100, 0, 512));
    public final Setting<Boolean> resetOnGuiSwitch = register(new Setting<>("ResetOnGuiSwitch", true));
    public final Setting<Integer> h = register(new Setting<>("Hue", 360, 0, 360));
    public final Setting<Integer> s = register(new Setting<>("Saturation", 360, 0, 360));
    public final Setting<Integer> l = register(new Setting<>("Lightness", 360, 0, 360));
    public final Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public final Setting<Integer> rainbowSpeed = register(new Setting<>("RainbowSpeed", 12, 0, 35));
    public final Setting<Float> minSize = register(new Setting<>("MinSize", 0.7f, 0f, 2.5f));
    public final Setting<Float> lineWidth = register(new Setting<>("LineWidth", 1.2f, 0f, 2.5f));
    public final Setting<Integer> maxRandomXSpeed = register(new Setting<>("MaxRandomXSpeed", 5, 0, 10));
    public final Setting<Integer> maxRandomYSpeed = register(new Setting<>("MaxRandomYSpeed", 5, 0, 10));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public ParticleGenerator particleGenerator = new ParticleGenerator(amount.getValue());

    public void renderParticles(int mouseX, int mouseY) {
        particleGenerator.draw(mouseX, mouseY);
    }

    public void resetParticles() {
        particleGenerator.reset();
    }

    @SubscribeEvent
    public void onGuiSwitch(GuiOpenEvent event) {
        if (resetOnGuiSwitch.getValue()) resetParticles();
    }
}
