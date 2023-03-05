package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraft.block.*;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
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
import nl.snoworange.cranberry.util.minecraft.*;
import nl.snoworange.cranberry.util.Timer;

/**
 * @author CinnamonApple
 * @author Snoworange
 */

public class Auto32k extends Module {

    public int phase = 0;
    public BlockPos basePos;
    public BlockPos tempBasePos;
    public BlockPos redstonePos;
    public EnumFacing dispenserDirection;
    public boolean placeVertically;
    public int originalSlot = -1;
    public int lastAirIndex = -1;
    public int tickTimer = 0;

    Timer timer = new Timer();

    public final Setting<Boolean> skipShulkerCheck = register(new Setting<>("SkipShulkerCheck", true));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swingArm = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> autoClose = register(new Setting<>("AutoClose", true));
    public final Setting<SearchMode> searchMode = register(new Setting<>("SearchMode", SearchMode.FROMDOWN));
    public final Setting<Boolean> timeout = register(new Setting<>("Timeout", true));
    public final Setting<Long> timeoutMs = register(new Setting<>("TimeoutMillis", 1250L, 49L, 3200L));
    public final Setting<Boolean> blockShulker = register(new Setting<>("BlockShulker", true));
    public final Setting<Integer> horizontalRange = register(new Setting<>("HorizontalRange", 6, 1, 7));
    public final Setting<Integer> plusVerticalRange = register(new Setting<>("+VerticalRange", 6, -7, 7));
    public final Setting<Integer> minusVerticalRange = register(new Setting<>("-VerticalRange", 6, -7, 7));
    public final Setting<Double> maxPlaceRange = register(new Setting<>("MaxPlaceRange", 6.0, 1.0, 7.0));
    public final Setting<Double> maxHopperRange = register(new Setting<>("MaxHopperRange", 5.0, 1.0, 7.0));
    public final Setting<Boolean> selectSwordSlot = register(new Setting<>("Select32kSlot", true));
    public final Setting<Boolean> checkFor32kShulks = register(new Setting<>("Force32kShulker", true));
    public final Setting<Boolean> debug = register(new Setting<>("Debug", false));

    public enum SearchMode {
        FROMUP,
        FROMDOWN;
    }

    public Auto32k() {
        super("Auto32k",
                "Automatically bypasses the anti-illegal with a setup to retrieve a 32k sword",
                Category.STRONKSWORDMETA
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        tickTimer = 0;

        phase = 0;
        lastAirIndex = -1;

        basePos = null;
        redstonePos = null;
        tempBasePos = null;
        dispenserDirection = null;

        if (!n()) originalSlot = mc.player.inventory.currentItem;

        timer.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) {
            if (phase < 7 && mc.currentScreen instanceof GuiDispenser) {
                mc.player.closeScreen();
            }

            if (originalSlot != -1 && !selectSwordSlot.getValue()) update(originalSlot, false);

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Override
    public void onFastTick() {

        if (n()) return;

        tickTimer++;

        if (timer.passedMs(timeoutMs.getValue()) && timeout.getValue()) disable();

        //item checks
        int hopperIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.HOPPER));
        int redstoneIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK));
        int dispenserIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.DISPENSER));
        int shulkerIndex = checkFor32kShulks.getValue() ? InventoryUtils.findShulkerWith32ksInside() : InventoryUtils.findShulker();
        int overenchantedSwordIndex = InventoryUtils.findHotbar32k();
        int revertedSwordIndex = InventoryUtils.findHotbarReverted32k();

        if (phase == 0 && (hopperIndex == -1 || dispenserIndex == -1 || redstoneIndex == -1 || shulkerIndex == -1)) {

            if (hopperIndex == -1) {
                error("Missing hopper in your hotbar!");
            }

            if (dispenserIndex == -1) {
                error("Missing dispenser in your hotbar!");
            }

            if (redstoneIndex == -1) {
                error("Missing redstone block in your hotbar!");
            }

            if (shulkerIndex == -1) {
                error((checkFor32kShulks.getValue() ?
                        "Cannot find a valid shulker box with 32k's in your inventory!"
                        :  "Cannot find shulker box in your inventory!"
                ));
            }

            disable();
            return;
        }

        if (phase == 0) {

            if (basePos == null) {
                placeVertically = false;
                searchBestPlacement();

                if (basePos == null) {
                    placeVertically = true;
                    searchBestPlacement();
                }
            }
        }

        if (phase == 1) {

            mc.player.setSprinting(false);

            if (debug.getValue()) info("Start placing 32k " + (placeVertically ? "vertically" : "normally") + " facing " + dispenserDirection.getOpposite().getName());

            float yaw;

            switch (dispenserDirection) {
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

            //Sends rotation packet to rotate dispenser, if needed
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, mc.player.rotationPitch, mc.player.onGround));

            update(dispenserIndex, silent.getValue());

            //Place dispenser
            placeBlock(placeVertically ? basePos : basePos.up());

            if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);

            //Open dispenser
            if (placeVertically) {
                mc.playerController.processRightClickBlock(mc.player,
                        mc.world,
                        basePos,
                        EnumFacing.UP,
                        new Vec3d(basePos.getX(),
                                basePos.getY(),
                                basePos.getZ()),
                        EnumHand.MAIN_HAND
                );
            } else {
                mc.playerController.processRightClickBlock(mc.player,
                        mc.world,
                        basePos.up(),
                        EnumFacing.UP,
                        new Vec3d(basePos.up().getX(),
                                basePos.up().getY(),
                                basePos.up().getZ()),
                        EnumHand.MAIN_HAND
                );
            }

            if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);

            phase = 2;
        }

        if (phase == 2) {

            if (mc.player.openContainer != null
                    && mc.currentScreen instanceof GuiDispenser
                    && mc.player.openContainer.inventorySlots != null) {

                //swap shulker box
                mc.playerController.windowClick(mc.player.openContainer.windowId, shulkerIndex, 0, ClickType.QUICK_MOVE, mc.player);

                if (debug.getValue()) info("Swapped shulker @ slot " + shulkerIndex);

                if (skipShulkerCheck.getValue()) {

                    phase = 3;

                    if (mc.currentScreen instanceof GuiDispenser) mc.player.closeScreen();

                } else if (mc.player.openContainer.inventorySlots.get(0).getStack().getItem() instanceof ItemShulkerBox) { //check if shulker is "actually" is swapped

                    phase = 3;

                    if (mc.currentScreen instanceof GuiDispenser) mc.player.closeScreen();
                }
            }
        }

        if (phase == 3) {

            //place redstone
            if (redstonePos != null) {

                update(redstoneIndex, silent.getValue());
                placeBlock(redstonePos);

                phase = 4;

            } else {
                error("Coudn't find any valid redstone placement! disabling...");

                disable();
                return;
            }
        }

        if (phase == 4) {

            update(hopperIndex, silent.getValue());

            //place hopper
            placeBlock(placeVertically ? basePos.down(2) : basePos.offset(dispenserDirection.getOpposite()));

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

            if (placeVertically) {
                mc.playerController.processRightClickBlock(mc.player,
                        mc.world,
                        basePos.down(2),
                        EnumFacing.UP,
                        new Vec3d(basePos.down(2).getX(),
                                basePos.down(2).getY(),
                                basePos.down(2).getZ()),
                        EnumHand.MAIN_HAND
                );
            } else {
                mc.playerController.processRightClickBlock(mc.player,
                        mc.world,
                        basePos.offset(dispenserDirection.getOpposite()),
                        EnumFacing.UP,
                        new Vec3d(basePos.offset(dispenserDirection.getOpposite()).getX(),
                                basePos.offset(dispenserDirection.getOpposite()).getY(),
                                basePos.offset(dispenserDirection.getOpposite()).getZ()),
                        EnumHand.MAIN_HAND
                );
            }

            phase = 5;
        }

        if (phase == 5) {
            //swap 32k
            if (overenchantedSwordIndex == -1
                    && mc.player.openContainer != null
                    && mc.currentScreen instanceof GuiHopper
                    && mc.player.openContainer.inventorySlots != null
                    && !mc.player.openContainer.inventorySlots.isEmpty()
            ) {

                for (int i = 0; i < 5; i++) {
                    if (InventoryUtils.is32k(mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i))) {
                        overenchantedSwordIndex = i;
                        break;
                    }
                }

                if (overenchantedSwordIndex == -1) {
                    return;
                }

                int airIndex = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.AIR));

                mc.playerController.windowClick(mc.player.openContainer.windowId,
                        overenchantedSwordIndex,
                        (airIndex != -1 ? airIndex : (revertedSwordIndex != -1 ? revertedSwordIndex : mc.player.inventory.currentItem)),
                        ClickType.SWAP,
                        mc.player
                );

                lastAirIndex = airIndex;

                if (selectSwordSlot.getValue() && (!blockShulker.getValue() || placeVertically)) update((airIndex != -1 ? airIndex : (revertedSwordIndex != -1 ? revertedSwordIndex : mc.player.inventory.currentItem)), false);

                if (debug.getValue()) info("32k found in slot " + overenchantedSwordIndex);

                if (autoClose.getValue() && mc.currentScreen instanceof GuiHopper) mc.player.closeScreen();

                if (debug.getValue() && !blockShulker.getValue()) info("Process ended in " + timer.getPassedTimeMs() + " ms / " + tickTimer + "tx");

                if (!blockShulker.getValue() || placeVertically) disable();

                phase = 6;
            }
        }

        if (phase == 6) {
            if (!blockShulker.getValue() || placeVertically) {
                phase = 7;
                return;
            } else {

                if (BlockUtils.isEmptyBlock(basePos.offset(dispenserDirection.getOpposite()).up(2), true)
                        && BlockUtils.checkIfBlockIsPlaceable(basePos.offset(dispenserDirection.getOpposite()).up(2))) {

                    if (dispenserIndex == -1 && redstoneIndex == -1) {

                        phase = 7;

                        error("No valid blocks to block shulker found!");
                        return;
                    }

                    update(dispenserIndex != -1 ? dispenserIndex : redstoneIndex, silent.getValue());
                    placeBlock(basePos.offset(dispenserDirection.getOpposite()).up(2));
                    update(mc.player.inventory.currentItem, false);

                }

                phase = 7;
            }
        }

        if (phase == 7) {

            if (selectSwordSlot.getValue()) update((lastAirIndex != -1 ? lastAirIndex : (revertedSwordIndex != -1 ? revertedSwordIndex : mc.player.inventory.currentItem)), false);

            if (debug.getValue()) info("Process ended in " + timer.getPassedTimeMs() + " ms / " + tickTimer + "tx");

            phase = 8;

            disable();
        }

        if (phase == 8) {
            if (HopperRadius.getInstance().sync32kHopper.getValue() && basePos != null) {
                HopperRadius.getInstance().hopperPos = placeVertically ? basePos.down(2) : basePos.offset(dispenserDirection.getOpposite());
            }
        }
    }

    public void searchBestPlacement() {
        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            if (searchMode.getValue().equals(SearchMode.FROMDOWN)) {
                for (int y = -minusVerticalRange.getValue(); y < plusVerticalRange.getValue(); ++y) {
                    for (int x = -horizontalRange.getValue(); x < horizontalRange.getValue(); ++x) {
                        for (int z = -horizontalRange.getValue(); z < horizontalRange.getValue(); ++z) {
                            if (basePos == null) start32k(new BlockPos(new Vec3d(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z)), direction);
                        }
                    }
                }
            } else if (searchMode.getValue().equals(SearchMode.FROMUP)) {
                for (int y = plusVerticalRange.getValue(); y > -minusVerticalRange.getValue(); --y) {
                    for (int x = -horizontalRange.getValue(); x < horizontalRange.getValue(); ++x) {
                        for (int z = -horizontalRange.getValue(); z < horizontalRange.getValue(); ++z) {
                            if (basePos == null) start32k(new BlockPos(new Vec3d(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z)), direction);
                        }
                    }
                }
            }
        }
    }

    public void start32k(BlockPos blockPos, EnumFacing direction) {
        if (placeVertically) {
            if (mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= maxPlaceRange.getValue()
                    && BlockUtils.isEmptyBlock(blockPos, true)
                    && BlockUtils.isEmptyBlock(blockPos.down(), true)
                    && BlockUtils.isEmptyBlock(blockPos.down(2), true)
                    && mc.player.getDistance(blockPos.down(2).getX(), blockPos.down(2).getY(), blockPos.down(2).getX()) <= maxHopperRange.getValue()
                    && EnumFacing.getDirectionFromEntityLiving(blockPos, mc.player).equals(EnumFacing.DOWN)
                    && BlockUtils.checkIfBlockIsPlaceable(blockPos)
                    && (BlockUtils.checkIfBlockIsPlaceable(blockPos.down(2)) || mc.world.getBlockState(blockPos.down(3)).getMaterial().isSolid())
                    && !(BlockUtils.exsistsBlocksNearby(blockPos.down(2), Blocks.REDSTONE_BLOCK))
            ) {
                basePos = blockPos;
                dispenserDirection = EnumFacing.UP; //does nothing because down bruh

                phase = 1;

                for (EnumFacing tempRedstoneFacing : EnumFacing.values()) {

                    if (tempRedstoneFacing.equals(EnumFacing.DOWN)) continue;

                    if (BlockUtils.isEmptyBlock(blockPos.offset(tempRedstoneFacing), true)) {
                        redstonePos = blockPos.offset(tempRedstoneFacing);
                        break;
                    }
                }
            }
        } else {
            if (mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= maxPlaceRange.getValue()
                    && !(EnumFacing.getDirectionFromEntityLiving(blockPos.up(), mc.player).equals(EnumFacing.UP)
                    || EnumFacing.getDirectionFromEntityLiving(blockPos.up(), mc.player).equals(EnumFacing.DOWN))
                    && BlockUtils.checkIfBlockIsPlaceable(blockPos.up())
                    && BlockUtils.checkIfBlockIsPlaceable(blockPos.offset(direction))
                    && mc.player.getDistance(blockPos.offset(direction).getX(), blockPos.offset(direction).getY(), blockPos.offset(direction).getZ()) <= maxHopperRange.getValue()
                    && BlockUtils.hasEmptyBlockIn4Directions(blockPos.up())
                    && BlockUtils.isEmptyBlock(blockPos.offset(direction), true)
                    && BlockUtils.isEmptyBlock(blockPos.offset(direction).up(), true)
                    && !(BlockUtils.exsistsBlocksNearby(blockPos.offset(direction), Blocks.REDSTONE_BLOCK))
            ) {
                basePos = blockPos;
                dispenserDirection = direction.getOpposite();

                phase = 1;

                for (EnumFacing redstoneFacing : EnumFacing.values()) {
                    if (!(redstoneFacing.equals(EnumFacing.DOWN)
                            || blockPos.up().offset(redstoneFacing).equals(blockPos.up().offset(direction)))) {
                        if (BlockUtils.isEmptyBlock(blockPos.up().offset(redstoneFacing), true)) {
                            redstonePos = blockPos.up().offset(redstoneFacing);
                            break;
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

        //except dispenser placing phase because the rotations will break bruh
        if (phase != 1 && rotate.getValue()) BlockUtils.sendRotatingPacket(hitVec);

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

        if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
    }
}