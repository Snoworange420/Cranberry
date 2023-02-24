package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright",
                "Lighhhhhhhhhhttttttt",
                Category.RENDER);
    }

    public enum Mode {
        GAMMA,
        POTION
    }

    public final Setting<Enum> mode = register(new Setting<>("Mode", Mode.GAMMA));
    public final Setting<Float> gammaAmount = register(new Setting<>("GammaAmount", 1000f, 0f, 1000f));
    public float lastGamma = -1f;

    @Override
    public void onEnable() {
        super.onEnable();

        lastGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) {
            if (mode.getValue().equals(Mode.POTION)) {
                mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
            }

            if (mode.getValue().equals(Mode.GAMMA)) {
                if (lastGamma != -1f) {
                    mc.gameSettings.gammaSetting = lastGamma;
                }
            }
        }
    }

    @Override
    public void onTick() {
        if (mode.getValue().equals(Mode.GAMMA)) {
            mc.gameSettings.gammaSetting = gammaAmount.getValue();
        }

        if (mode.getValue().equals(Mode.POTION)) {
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5200));
        }
    }
}
