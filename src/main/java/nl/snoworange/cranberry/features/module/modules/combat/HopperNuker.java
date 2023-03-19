package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.module.modules.stronkswordmeta.Auto32k;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.BlockUtils;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import nl.snoworange.cranberry.util.minecraft.PlayerUtils;

public class HopperNuker extends Module {

    public HopperNuker() {
        super("HopperNuker",
                "Nukes hoppers (?)",
                Category.COMBAT
        );
    }

    public enum BreakMode {
        VANILLA,
        PACKET
    }

    public final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public final Setting<Boolean> swingArm = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> onlyOnGround = register(new Setting<>("OnlyOnGround", true));
    public final Setting<Boolean> noSelfBreaking = register(new Setting<>("NoSelfBreak", true));
    public final Setting<Boolean> only32kHoppers = register(new Setting<>("Only32kHoppers", true));
    public final Setting<BreakMode> breakmode = register(new Setting<>("BreakMode", BreakMode.VANILLA));
    public final Setting<Integer> range = register(new Setting<>("Range", 5, 1, 7));

    public BlockPos hopperPos;
    public static BlockPos selfPos;

    @Override
    public void onEnable() {
        super.onEnable();

        selfPos = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {

        if (n()) return;

        int pickaxeIndex = InventoryUtils.findHotbarPickaxe();

        if (pickaxeIndex == -1) return;

        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(mc.player.posX - range.getValue(), mc.player.posY - 3, mc.player.posZ - range.getValue()), new BlockPos(mc.player.posX + range.getValue(), mc.player.posY + 3, mc.player.posZ + range.getValue()))) {

            if (mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) <= range.getValue()
                    && (only32kHoppers.getValue() ? (mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper
            && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockShulkerBox) : (mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper))
                    && (!blockPos.equals(selfPos) && noSelfBreaking.getValue())) {
                hopperPos = blockPos;
                break;
            } else {
                hopperPos = null;
            }
        }

        if (hopperPos != null) {

            int oldSlot = mc.player.inventory.currentItem;

            if ((onlyOnGround.getValue() && !mc.player.onGround)
            || (Auto32k.getInstance().isEnabled() && Auto32k.getInstance().getPhase() != 9)) return;

            mc.player.inventory.currentItem = pickaxeIndex;
            mc.playerController.updateController();

            if (rotate.getValue()) PlayerUtils.faceVector(new Vec3d(hopperPos.getX(), hopperPos.getY(), hopperPos.getZ()), true);

            if (breakmode.getValue().equals(BreakMode.PACKET)) {

                if (!BlockUtils.isEmptyBlock(hopperPos, false)) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, hopperPos, mc.player.getAdjustedHorizontalFacing()));
                }

                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, hopperPos, mc.player.getAdjustedHorizontalFacing()));
            } else {
                mc.playerController.onPlayerDamageBlock(hopperPos, mc.player.getAdjustedHorizontalFacing());
            }

            if (swingArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);

            mc.player.inventory.currentItem = oldSlot;

        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.isEnabled()) {

            if (n()) return;

            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {

                CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                BlockPos packetPos = packet.getPos();

                if (mc.world.getBlockState(packetPos).getBlock() instanceof BlockHopper) {
                    selfPos = packetPos;
                }
            }
        }
    }
}
