package nl.snoworange.cranberry.features.module.modules.player;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
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
    public void init() {
        this.setModuleStack(new ItemStack(Items.EXPERIENCE_BOTTLE));
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
