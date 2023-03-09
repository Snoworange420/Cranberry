package nl.snoworange.cranberry.util.minecraft;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

public class PlayerUtils {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isSufflocating(EntityPlayer player) {

        return mc.world.getBlockState(new BlockPos(player.posX,
                player.posY + 1,
                player.posZ)).causesSuffocation();
    }

    public static boolean isBurrowing(EntityPlayer player) {
        return mc.world.getBlockState(new BlockPos(player.posX,
                player.posY,
                player.posZ)).getMaterial().isSolid();
    }

    public static boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.OBSIDIAN) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.BEDROCK) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        BlockPos[] arrayOfBlockPos1;
        int i;
        byte b;
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
            BlockPos pos = arrayOfBlockPos1[b];
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN)) {
                b++;
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isInLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }

    public static void teleportPlayerToCenter() {
        double centerX = Math.floor(mc.player.posX) + 0.5;
        double centerZ = Math.floor(mc.player.posZ) + 0.5;
        mc.player.setPosition(centerX, mc.player.posY, centerZ);
        mc.player.connection.sendPacket(new CPacketPlayer.Position(centerX, mc.player.posY, centerZ, mc.player.onGround));
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = getLegitRotations(vec);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], mc.player.onGround));
    }
}
