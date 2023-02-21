package nl.snoworange.cranberry.util.minecraft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

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
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
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
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
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
        for (arrayOfBlockPos1 = touchingBlocks = new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, i = arrayOfBlockPos1.length, b = 0; b < i; ) {
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
}
