package nl.snoworange.cranberry.features.module.modules.movement;

import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class LiquidSpeed extends Module {

    public LiquidSpeed() {
        super("LiquidSpeed",
                Category.MOVEMENT
        );
    }

    public final Setting<Float> movementFactor = register(new Setting<>("MovementFactor", 0.25f, 0.0f, 2.5f));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (n()) return;

        if (PlayerUtils.isInLiquid()) {
            mc.player.jumpMovementFactor = movementFactor.getValue();
        }
    }
}
