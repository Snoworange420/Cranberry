package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.BlockUtils;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class Surround extends Module {

    public Surround() {
        super("Surround",
                "Surrounds urself",
                Category.COMBAT
        );
    }

    public enum Mode {
        OBSIDIAN,
        GLASS;
    }

    public BlockPos middlePos;
    public int phase = 0;
    public int originalSlot = -1;

    public final Setting<Enum> mode = register(new Setting<>("Mode", Mode.OBSIDIAN));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swingArm = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> jumpDisable = register(new Setting<>("JumpDisable", true));
    public final Setting<Boolean> disableOnBigDistance = register(new Setting<>("DistanceDisable", true));
    public final Setting<Boolean> centerTp = register(new Setting<>("CenterTP", true));

    @Override
    public void onEnable() {
        super.onEnable();

        middlePos = null;
        phase = 0;

        if (!n()) {
            originalSlot = mc.player.inventory.currentItem;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void init() {
        this.setModuleStack(new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN)));
    }

    @Override
    public void onTick() {

        if (n()) return;

        int obsidianIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.OBSIDIAN));
        int glassIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.GLASS));
        int stainedGlassIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.STAINED_GLASS));

        if (mode.getValue().equals(Mode.OBSIDIAN) && obsidianIndex == -1) {

            error("No glass found in your hotbar!");

            disable();
            return;
        } else if (mode.getValue().equals(Mode.GLASS) && (glassIndex == -1 && stainedGlassIndex == -1)) {

            error("No glass found in your hotbar!");

            disable();
            return;
        }

        if (phase == 0) {
            if (middlePos == null) {
                middlePos = new BlockPos(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ));

                if (centerTp.getValue()) {
                    PlayerUtils.teleportPlayerToCenter();
                }

                phase = 1;
            }
        }

        if (phase == 1) {

            if (jumpDisable.getValue() && ((mc.player.posY - 0.9) > middlePos.getY() || (mc.player.posY + 0.9) < middlePos.getY())) {
                disable();
                return;
            } else if (disableOnBigDistance.getValue() && mc.player.getDistanceSq(middlePos) > 1.1D) {
                disable();
                return;
            }

            if (mode.getValue().equals(Mode.OBSIDIAN)) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (BlockUtils.isEmptyBlock(middlePos.offset(facing), true)
                            && BlockUtils.checkIfBlockIsPlaceable(middlePos.offset(facing))) {
                        if (mc.player.onGround) {
                            update(obsidianIndex, silent.getValue());
                            placeBlock(middlePos.offset(facing));

                            if (originalSlot != -1) update(originalSlot, false);
                        }
                    }
                }
            }

            if (mode.getValue().equals(Mode.GLASS)) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (BlockUtils.isEmptyBlock(middlePos.offset(facing), true)
                            && BlockUtils.checkIfBlockIsPlaceable(middlePos.offset(facing))) {
                        if (mc.player.onGround) {
                            update(glassIndex != -1 ? glassIndex : stainedGlassIndex, silent.getValue());
                            placeBlock(middlePos.offset(facing));

                            if (originalSlot != -1) update(originalSlot, false);
                        }
                    }
                }
            }
        }
    }

    public void update(int index, boolean silent) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(index));
        if (!silent) mc.player.inventory.currentItem = index;
        mc.playerController.updateController();
    }

    private void placeBlock(BlockPos pos) {

        if (n()) return;

        EnumFacing side = BlockUtils.getPlaceableSide(pos);

        if (side == null) {
            return;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        Vec3d hitVec = new Vec3d(neighbour).add((new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(opposite.getDirectionVec()).scale(0.5)));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

        if (BlockUtils.blackList.contains(neighbourBlock) || BlockUtils.shulkerList.contains(neighbourBlock)) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }

        if (rotate.getValue()) BlockUtils.sendRotatingPacket(hitVec);

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

        if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
    }
}
