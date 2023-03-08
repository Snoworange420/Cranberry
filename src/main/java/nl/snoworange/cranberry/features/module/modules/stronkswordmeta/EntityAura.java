package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;

public class EntityAura extends Module {

    public EntityAura() {
        super("EntityAura",
                "automatically hits entites",
                Category.STRONKSWORDMETA
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
    public void onTick() {

        if (n()) return;

        for (Entity entity : mc.world.loadedEntityList) {

            if (entity == mc.player) continue;

            if (entity.isDead) continue;

            if (entity instanceof EntityXPOrb
                    || entity instanceof EntityItem
                    || entity instanceof EntityEnderPearl
                    || entity instanceof EntityArrow
                    || entity instanceof EntitySnowball
            ) continue;

            if (mc.player.getDistance(entity) <= 8) {
                mc.playerController.attackEntity(mc.player, entity);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}
