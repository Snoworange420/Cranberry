package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.BlockUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class SelfAnvil extends Module {

    public SelfAnvil() {
        super("SelfAnvil",
                "anvils yourself so the enemy cant move into your hole",
                Category.COMBAT
        );
    }

    public int phase = 0;
    public BlockPos anvilPos = null;

    public final Setting<Boolean> centerTp = register(new Setting<>("CenterTP", true));
    public final Setting<Boolean> selfAnvil = register(new Setting<>("SelfAnvil", true));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swing = register(new Setting<>("SwingArm", true));
    public final Setting<Double> maxAnvilRange = register(new Setting<>("MaxAnvilRange", 5.0, 1.0, 7.0));

    @Override
    public void onEnable() {
        super.onEnable();

        if (n()) return;

        phase = 0;
        anvilPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) InventoryUtils.update(mc.player.inventory.currentItem, false);
    }

    @Override
    public void init() {
        setModuleStack(new ItemStack(Item.getItemFromBlock(Blocks.ANVIL)));
    }

    @Override
    public void onTick() {

        int anvilIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.ANVIL));

        if (phase == 0 && (selfAnvil.getValue() && anvilIndex == -1)) {
            error("No anvil found in your hotbar!");

            disable();
            return;
        }

        if (phase == 0) {

            if (centerTp.getValue()) {
                PlayerUtils.teleportPlayerToCenter();

                if (canDisable()) {

                    disable();
                    phase = -1;
                    return;
                }

                phase = 1;
            }
        }

        if (phase == 1) {

            if (selfAnvil.getValue()) {

                if (anvilPos == null) {
                    searchAnvilPos();
                    return;
                }

                InventoryUtils.update(anvilIndex, silent.getValue());
                BlockUtils.placeBlock(anvilPos, rotate.getValue(), swing.getValue());
                InventoryUtils.update(mc.player.inventory.currentItem, false);

                disable();
            } else {
                disable();
            }
        }
    }

    public void searchAnvilPos() {

        for (int y = (int) (mc.player.posY + 1.0); y < (int) (mc.player.posY + maxAnvilRange.getValue()); ++y) {

            BlockPos blockPos = new BlockPos(new Vec3d(mc.player.posX, y, mc.player.posZ));

            if (BlockUtils.isEmptyBlock(blockPos, true)
                    && BlockUtils.checkIfBlockIsPlaceable(blockPos)
                    && BlockUtils.isEmptyBlock(blockPos.down(), false)) {
                anvilPos = blockPos;
                break;
            }
        }
    }

    public boolean canDisable() {
        return !selfAnvil.getValue();
    }
}
