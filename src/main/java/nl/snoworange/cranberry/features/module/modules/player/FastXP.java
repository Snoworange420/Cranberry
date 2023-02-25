package nl.snoworange.cranberry.features.module.modules.player;

import net.minecraft.item.ItemExpBottle;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.mixin.mixins.IMinecraft;

public class FastXP extends Module {

    public FastXP() {
        super("FastXP",
                "Modifies right click delay timer when you hold xp",
                Category.PLAYER
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

    @Override
    public void onFastTick() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle
        || mc.player.getHeldItemOffhand().getItem() instanceof ItemExpBottle) {
            if(((IMinecraft) mc).getRightClickDelayTimer() != 0) {
                ((IMinecraft) mc).setRightClickDelayTimer(0);
            }
        }
    }
}
