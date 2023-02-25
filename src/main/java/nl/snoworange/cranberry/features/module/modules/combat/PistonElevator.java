package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.BlockUtils;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class PistonElevator extends Module {

    public PistonElevator() {
        super("PistonElevator",
                "Pushes enemy out of hole using pistons",
                Category.COMBAT);
    }

    public int phase = 0;
    public BlockPos basePos;
    public BlockPos holePos;
    public BlockPos redstonePos;
    public EnumFacing pistonDirection;
    public int tickTimer = 0;
    public boolean placedPiston = false;


    public final Setting<Boolean> targetFriends = register(new Setting<>("TargetFriends", false));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swingArm = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", false));
    public final Setting<Integer> placeDelayTicks = register(new Setting<>("PlaceDelayTicks", 5, 0, 20));
    public final Setting<Boolean> autoDisable = register(new Setting<>("AutoDisable", true));
    public final Setting<Double> maxRange = register(new Setting<>("MaxTargetRange", 5.5, 0.1, 7.0));
    public final Setting<Double> maxPlaceRange = register(new Setting<>("MaxPlaceRange", 6.0, 0.1, 7.0));

    @Override
    public void onEnable() {
        super.onEnable();

        reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void reset() {
        phase = 0;
        basePos = null;
        holePos = null;
        redstonePos = null;
        tickTimer = 0;
        placedPiston = false;
    }

    @Override
    public void onTick() {

        if (n()) return;

        if (placedPiston) tickTimer++;

        int pistonIndex = InventoryUtils.findHotbarPiston();
        int redstoneIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK));

        if (phase == 0 && (pistonIndex == -1 || redstoneIndex == -1)) {

            if (pistonIndex == -1) ChatUtils.sendMessage("Missing piston in your hotbar!");
            if (redstoneIndex == -1) ChatUtils.sendMessage("Missing redstone block in your hotbar!");

            disable();
            return;
        }

        if (phase == 0) {

            if (findEnemy() == null) return;

            if (PlayerUtils.isSufflocating(findEnemy())) return;

            searchValidPlacementForPistons(findEnemy());
        }

        if (phase == 1) {

            if (basePos == null) return;

            mc.player.setSprinting(false);

            float yaw;

            switch (pistonDirection) {
                case NORTH:
                    yaw = -179.9f;
                    break;
                case EAST:
                    yaw = -89.9f;
                    break;
                case SOUTH:
                    yaw = 0.1f;
                    break;
                case WEST:
                    yaw = 90.1f;
                    break;
                default:
                    yaw = mc.player.rotationYaw;
            }

            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, mc.player.rotationPitch, mc.player.onGround));

            update(pistonIndex, silent.getValue());
            placeBlock(basePos);
            update(mc.player.inventory.currentItem, silent.getValue());

            placedPiston = true;

            phase = 2;
        }

        if (phase == 2) {

            if (redstonePos == null) return;

            if (tickTimer >= placeDelayTicks.getValue()) {
                update(redstoneIndex, silent.getValue());
                placeBlock(redstonePos);

                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));

                if (autoDisable.getValue()) {
                    disable();
                    phase = 3;
                } else {
                    reset();
                }
            }
        }
    }

    public EntityPlayer findEnemy() {
        for (EntityPlayer target : mc.world.playerEntities) {

            if (target == mc.player) continue;

            if (!targetFriends.getValue() && Main.friendManager.isFriend(target.getName())) continue;

            if (target.isDead || target.getHealth() < 0) continue;

            if (mc.player.getDistance(target) <= maxRange.getValue() && (
                  PlayerUtils.isInHole(target) || PlayerUtils.isBurrowing(target)
            )) {

                return target;
            }
        }
        return null;
    }

    public void searchValidPlacementForPistons(EntityPlayer target) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(mc.player.posX - maxPlaceRange.getValue(), mc.player.posY - maxPlaceRange.getValue(), mc.player.posZ - maxPlaceRange.getValue()), new BlockPos(mc.player.posX + maxPlaceRange.getValue(), mc.player.posY + maxPlaceRange.getValue(), mc.player.posZ + maxPlaceRange.getValue()))) {

                AxisAlignedBB box = new AxisAlignedBB(blockPos.offset(facing));

                if (BlockUtils.isEmptyBlock(blockPos, true)
                        && BlockUtils.checkIfBlockIsPlaceable(blockPos)
                        && mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= maxPlaceRange.getValue()
                        && box.intersects(target.getEntityBoundingBox())
                        && target.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= 2
                        && BlockUtils.isEmptyBlock(blockPos.offset(facing), false)
                        && EnumFacing.getDirectionFromEntityLiving(blockPos, mc.player).equals(facing)
                ) {
                    basePos = blockPos;
                    pistonDirection = facing.getOpposite();

                    phase = 1;

                    holePos = blockPos.offset(facing).down();

                    for (EnumFacing redstoneFacing : EnumFacing.VALUES) {
                        if (BlockUtils.isEmptyBlock(basePos.offset(redstoneFacing), true)
                                && !basePos.offset(redstoneFacing).equals(basePos.offset(facing))
                        ) {
                            redstonePos = basePos.offset(redstoneFacing);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void update(int index, boolean silent) {

        if (index == -1) return;

        mc.player.connection.sendPacket(new CPacketHeldItemChange(index));
        if (!silent) mc.player.inventory.currentItem = index;
        mc.playerController.updateController();
    }

    private void placeBlock(BlockPos pos) {

        if (n()) {
            disable();
            return;
        }

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

        if (phase != 1 && rotate.getValue()) BlockUtils.sendRotatingPacket(hitVec);

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

        if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
    }
}