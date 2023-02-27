package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.BlockUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class AutoAnvil extends Module {

    public AutoAnvil() {
        super("AutoAnvil",
                "places anvils on enemies in surrounded hole to rubberband them out",
                Category.COMBAT
        );
    }

    public int phase = 0;
    public int secondAnvilTimer = 0;
    public BlockPos anvilPos = null;
    public boolean placedFirstAnvil = false;


    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swing = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> autoDisable = register(new Setting<>("AutoDisable", true));
    public final Setting<Boolean> placeSecond = register(new Setting<>("Place2ndAnvil", true));
    public final Setting<Integer> secondAnvilDelay = register(new Setting<>("SecondAnvilDelay", 36, 20, 50));
    public final Setting<Double> maxRange = register(new Setting<>("MaxTargetRange", 5.5, 0.1, 7.0));
    public final Setting<Double> maxAnvilRange = register(new Setting<>("MaxAnvilRange", 5.0, 1.0, 7.0));


    @Override
    public void onEnable() {
        super.onEnable();

        reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) InventoryUtils.update(mc.player.inventory.currentItem, false);
    }

    public void reset() {
        phase = 0;
        anvilPos = null;
        placedFirstAnvil = false;
        secondAnvilTimer = 0;
    }

    @Override
    public void init() {
        setModuleStack(new ItemStack(Item.getItemFromBlock(Blocks.ANVIL)));
    }

    @Override
    public void onTick() {

        if (placedFirstAnvil) secondAnvilTimer++;

        int anvilIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.ANVIL));

        if (phase == 0 && anvilIndex == -1) {
            error("No anvil found in your hotbar!");

            disable();
            return;
        }

        if (phase == 0) {

            if (findEnemy() == null) return;

            if (anvilPos == null) {
                searchAnvilPos(findEnemy());
            } else {
                phase = 1;
            }
        }

        if (phase == 1) {

            InventoryUtils.update(anvilIndex, silent.getValue());
            BlockUtils.placeBlock(anvilPos, rotate.getValue(), swing.getValue());
            InventoryUtils.update(mc.player.inventory.currentItem, false);

            placedFirstAnvil = true;

            if (placeSecond.getValue()) {
                phase = 2;
            } else if (!autoDisable.getValue()) {
                reset();
                return;
            } else {
                disable();
                return;
            }
        }

        if (phase == 2) {

            if (secondAnvilTimer >= secondAnvilDelay.getValue()) {

                if (findEnemy() == null) return;

                if (anvilPos == null) {
                    searchAnvilPos(findEnemy());
                } else {
                    InventoryUtils.update(anvilIndex, silent.getValue());
                    BlockUtils.placeBlock(anvilPos, rotate.getValue(), swing.getValue());
                    InventoryUtils.update(mc.player.inventory.currentItem, false);

                    if (autoDisable.getValue()) {
                        disable();
                    } else {
                        reset();
                    }
                }
            }
        }
    }

    public void searchAnvilPos(EntityPlayer target) {

        for (int y = (int) (target.posY + 1.0); y < (int) (target.posY + 6); ++y) {

            BlockPos blockPos = new BlockPos(new Vec3d(target.posX, y, target.posZ));

            if (target.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > maxAnvilRange.getValue()) return;

            if (BlockUtils.isEmptyBlock(blockPos, true) && BlockUtils.checkIfBlockIsPlaceable(blockPos)) {
                anvilPos = blockPos;
                break;
            }
        }
    }

    public EntityPlayer findEnemy() {
        for (EntityPlayer target : mc.world.playerEntities) {

            if (target == mc.player) continue;

            if (target.isDead || target.getHealth() < 0) continue;

            if (mc.player.getDistance(target) <= maxRange.getValue() && (
                    PlayerUtils.isInHole(target) || PlayerUtils.isBurrowing(target)
            )) {

                return target;
            }
        }
        return null;
    }

}
