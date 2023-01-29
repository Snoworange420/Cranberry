package nl.snoworange.cranberry.features.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;

public class AutoSprint extends Module {

    public AutoSprint() {
        super(
                "AutoSprint"
                ,"Automatically sprints"
                , Category.MOVEMENT
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

    @Override
    public void onTick() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
}
