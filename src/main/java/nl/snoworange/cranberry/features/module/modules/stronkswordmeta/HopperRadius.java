package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.ColorUtils;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class HopperRadius extends Module {

    public BlockPos hopperPos;
    public double wallHeight;
    public BlockPos oldHopperPos;
    public double radius;
    public double hitradius;
    public HashMap<BlockPos, Color> map = new HashMap<>();
    public Color color = new Color(255, 255, 255);

    private static HopperRadius instance;

    public enum DrawMode {
        ONLYSELF,
        EXCLUDESELF,
        BOTH
    }

    public enum DetectMode {
        NONE,
        ONLYSHULKER,
        INCLUDEDISPENSER
    }

    public final Setting<Boolean> ignoreRecievedPackets = register(new Setting<>("IgnoreRPackets", false));
    public final Setting<Boolean> ignoreSentPackets = register(new Setting<>("IgnoreSPackets", false));
    public final Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    public final Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    public final Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    public final Setting<Integer> fillAlpha = register(new Setting<>("FillAlpha", 120, 0, 255));
    public final Setting<Boolean> colorRandom = register(new Setting<>("ColorRandom", true));
    public final Setting<Boolean> highlightHopper = register(new Setting<>("HighlightHopper", true));
    public final Setting<Boolean> drawHitRange = register(new Setting<>("DrawHitRange", true));
    public final Setting<DrawMode> drawMode = register(new Setting<>("DrawMode", DrawMode.ONLYSELF));
    public final Setting<DetectMode> detectMode = register(new Setting<>("DetectMode", DetectMode.ONLYSHULKER, v -> !drawMode.getValue().equals(DrawMode.ONLYSELF)));
    public final Setting<Integer> range = register(new Setting<>("Range", 20, 1, 40, v -> !drawMode.getValue().equals(DrawMode.ONLYSELF)));
    public final Setting<Boolean> sync32kHopper = register(new Setting<>("Sync32kHopper", true));
    public final Setting<Double> height = register(new Setting<>("Height", 0.0, 0.0, 3.0));
    public final Setting<Float> lineWidth = register(new Setting("LineWidth", 2.5f, 0.0f, 5.0f));
    public final Setting<Double> spreadSpeed = register(new Setting<>("SpreadSpeed", 0.1, 0.0, 1.0));

    public HopperRadius() {
        super("HopperRadius",
                "renders what the maximum hopper range is where you can stay in the hopper gui server-side",
                Category.STRONKSWORDMETA
        );

        instance = this;
    }

    public static HopperRadius getInstance() {
        return instance != null ? instance : new HopperRadius();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        map.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void init() {
        this.setModuleStack(new ItemStack(Item.getItemFromBlock(Blocks.HOPPER)));
    }

    @Override
    public void onTick() {
        if (n()) return;

        if (hopperPos == null) {
            return;
        }

        if (!(mc.world.getBlockState(hopperPos).getBlock() instanceof BlockHopper) || mc.player.getDistanceSq(hopperPos.getX() + 0.5, hopperPos.getY() + 0.5, hopperPos.getZ() + 0.5) > 64.0) {
            hopperPos = null;
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
                    wallHeight = 0;
                    hopperPos = packetPos;
                }
            }

            if (ignoreSentPackets.getValue()) return;

            if (event.getPacket() instanceof CPacketCloseWindow) {
                hopperPos = null;
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.isEnabled()) {

            if (n() || ignoreRecievedPackets.getValue()) return;

            if (event.getPacket() instanceof SPacketOpenWindow) {
                if (((SPacketOpenWindow) event.getPacket()).getWindowTitle().getUnformattedText().equalsIgnoreCase("Item Hopper")) {
                    return;
                }
                hopperPos = null;
            }
        }
    }

    @Override
    public void onRender3d() {

        if (n()) return;

        color = new Color(red.getValue(), green.getValue(), blue.getValue());

        if (drawMode.getValue().equals(DrawMode.ONLYSELF)
        || drawMode.getValue().equals(DrawMode.BOTH)) {

            if (wallHeight < height.getValue()) {
                wallHeight += 0.005 * height.getValue();
            } else if (wallHeight > height.getValue()) {
                wallHeight -= 0.01;
            }

            if (hopperPos != null) {
                RenderUtils.drawCircle(hopperPos, 8, wallHeight, color, lineWidth.getValue());
                if (drawHitRange.getValue()) RenderUtils.drawCircle(hopperPos, 8 + 7, wallHeight, color, lineWidth.getValue());

                if (highlightHopper.getValue()) RenderUtils.drawFilledBox(hopperPos, 0.0, ColorUtils.newAlpha(color, fillAlpha.getValue()), lineWidth.getValue());

                oldHopperPos = hopperPos;

                radius = 7.5;
                hitradius = radius + 7;

                return;
            }

            if (oldHopperPos != null) {
                RenderUtils.drawCircle(oldHopperPos, radius, wallHeight, color, lineWidth.getValue());
                if (drawHitRange.getValue()) RenderUtils.drawCircle(oldHopperPos, hitradius, wallHeight, color, lineWidth.getValue());

                if (highlightHopper.getValue()) RenderUtils.drawFilledBox(oldHopperPos, 0.0, ColorUtils.newAlpha(color, fillAlpha.getValue()), lineWidth.getValue());

                if (wallHeight > 0) {
                    wallHeight -= 0.1;
                    return;
                }

                if (radius > 0) {
                    radius -= spreadSpeed.getValue();
                } else {
                    radius = 0;
                }

                if (hitradius > 0) {
                    hitradius -= spreadSpeed.getValue() * 2;
                } else {
                    hitradius = 0;
                }
            }
        }

        if (drawMode.getValue().equals(DrawMode.EXCLUDESELF) || drawMode.getValue().equals(DrawMode.BOTH)) {

            //draw hoppers in a range
            for (int y = -range.getValue(); y < range.getValue(); ++y) {
                for (int x = -range.getValue(); x < range.getValue(); ++x) {
                    for (int z = -range.getValue(); z < range.getValue(); ++z) {

                        BlockPos blockPos = new BlockPos(new Vec3d(x + mc.player.posX, y + mc.player.posY, z + mc.player.posZ));

                        boolean foundEnemy = false;

                        for (EntityPlayer player : mc.world.playerEntities) {

                            if (player == mc.player) continue;

                            if (!(player.getDistanceSq(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) > 64.0)) {
                                foundEnemy = true;
                                break;
                            }
                        }

                        if (!foundEnemy) continue;

                        if ((detectMode.getValue().equals(DetectMode.ONLYSHULKER) || detectMode.getValue().equals(DetectMode.INCLUDEDISPENSER))
                        && !(mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockShulkerBox)) continue;

                        if (detectMode.getValue().equals(DetectMode.INCLUDEDISPENSER)) {

                            boolean foundDispenser = false;

                            for (EnumFacing facing : EnumFacing.values()) {
                                if (facing.equals(EnumFacing.DOWN)) continue;

                                if (mc.world.getBlockState(blockPos.up().offset(facing)).getBlock() instanceof BlockDispenser) {
                                    foundDispenser = true;
                                    break;
                                }
                            }

                            if (!foundDispenser) continue;
                        }

                        if (mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper) {

                            if (map.containsKey(blockPos)) {
                                color = map.get(blockPos);
                            } else {
                                if (colorRandom.getValue()) {
                                    color = new Color(new Random().nextInt(256),
                                            new Random().nextInt(256),
                                            new Random().nextInt(256)
                                    );
                                }

                                map.put(blockPos, color);
                            }

                            RenderUtils.drawCircle(blockPos, 7.5, wallHeight, color, lineWidth.getValue());
                            if (drawHitRange.getValue()) RenderUtils.drawCircle(blockPos, 7.5 + 7, wallHeight, color, lineWidth.getValue());

                            if (highlightHopper.getValue()) RenderUtils.drawFilledBox(blockPos, 0.0, ColorUtils.newAlpha(color, fillAlpha.getValue()), lineWidth.getValue());
                        }
                    }
                }
            }
        }
    }
}
