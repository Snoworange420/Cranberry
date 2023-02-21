package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;

import java.awt.*;

public class HopperRadius extends Module {

    public BlockPos hopperPos;
    public double wallHeight;
    public BlockPos oldHopperPos;
    public double radius;
    public double hitradius;
    public Color color = new Color(255, 255, 255);

    private static HopperRadius instance;

    public final Setting<Boolean> ignoreRecievedPackets = register(new Setting<>("IgnoreRPackets", false));
    public final Setting<Boolean> ignoreSentPackets = register(new Setting<>("IgnoreSPackets", false));
    public final Setting<Boolean> sync32kHopper = register(new Setting<>("Sync32kHopper", true));
    public final Setting<Double> height = register(new Setting<>("Height", 0.5, 0.0, 3.0));
    public final Setting<Double> spreadSpeed = register(new Setting<>("SpreadSpeed", 0.1, 0.0, 1.0));

    public HopperRadius() {
        super("HopperRadius",
                "renders in what hopper you're at server side",
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
    }

    @Override
    public void onDisable() {
        super.onDisable();
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

            if (!(event.getPacket() instanceof SPacketCloseWindow)) {
                if (event.getPacket() instanceof SPacketOpenWindow) {
                    if (((SPacketOpenWindow) event.getPacket()).getWindowTitle().getUnformattedText().equalsIgnoreCase("Item Hopper")) {
                        return;
                    }
                    hopperPos = null;
                }
            }
        }
    }

    @Override
    public void onRender3d() {

        if (n()) return;

        if (wallHeight < height.getValue()) {
            wallHeight += 0.005 * height.getValue();
        } else if (wallHeight > height.getValue()) {
            wallHeight -= 0.01;
        }

        if (hopperPos != null) {
            RenderUtils.drawCircle(hopperPos, 8, wallHeight, color);
            RenderUtils.drawCircle(hopperPos, 8 + 7, wallHeight, color);

            oldHopperPos = hopperPos;

            radius = 7.5;
            hitradius = radius + 7;

            return;
        }

        if (oldHopperPos != null) {
            RenderUtils.drawCircle(oldHopperPos, radius, wallHeight, color);
            RenderUtils.drawCircle(oldHopperPos, hitradius, wallHeight, color);

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
}
