package nl.snoworange.cranberry.features.module.modules.render.popchams;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

import java.awt.*;

public class PopChams extends Module {

    private static PopChams instance;

    public PopChams() {
        super("PopChams",
                Category.RENDER
        );
        instance = this;
    }

    public static PopChams getInstance() {
        return instance != null ? instance : new PopChams();
    }

    public EntityOtherPlayerMP player;
    public ModelPlayer playerModel;

    public enum MoveMode {
        UP,
        DOWN,
        NONE
    }

    public final Setting<Boolean> selfRender = register(new Setting<>("SelfRender", false));
    public final Setting<MoveMode> moveMode = register(new Setting<>("MoveMode", MoveMode.DOWN));
    public final Setting<Integer> moveSpeed = register(new Setting<>("MoveSpeed", 20, 0, 50));
    public final Setting<Integer> fillFadeSpeed = register(new Setting<>("FillFadeSpeed", 1, 0, 10));
    public final Setting<Integer> outlineFadeSpeed = register(new Setting<>("OutlineFadeSpeed", 1, 0, 10));
    public final Setting<Integer> fillRed = register(new Setting<>("FillRed", 244, 0, 255));
    public final Setting<Integer> fillGreen = register(new Setting<>("FillGreen", 244, 0, 255));
    public final Setting<Integer> fillBlue = register(new Setting<>("FillBlue", 244, 0, 255));
    public final Setting<Integer> fillAlpha = register(new Setting<>("FillAlpha", 155, 0, 255));
    public final Setting<Integer> outlineRed = register(new Setting<>("OutlineRed", 244, 0, 255));
    public final Setting<Integer> outlineGreen = register(new Setting<>("OutlineGreen", 244, 0, 255));
    public final Setting<Integer> outlineBlue = register(new Setting<>("OutlineBlue", 244, 0, 255));
    public final Setting<Integer> outlineAlpha = register(new Setting<>("OutlineAlpha", 220, 0, 255));
    public final Setting<Float> lineWidth = register(new Setting<>("LineWidth", 1.0f, 0.1f, 3.5f));


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {

        if (!n() && this.isEnabled()) {

            if (event.getPacket() instanceof SPacketEntityStatus) {

                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

                if (packet.getOpCode() == 35) {
                    Entity entity = packet.getEntity(mc.world);

                    if (!(entity instanceof EntityPlayer) || (!selfRender.getValue() && entity.getName().equals(mc.player.getName()))) return;

                    GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");

                    player = new EntityOtherPlayerMP(mc.world, profile);
                    player.copyLocationAndAnglesFrom(packet.getEntity(mc.world));

                    playerModel = new ModelPlayer(0.0f, false);

                    playerModel.bipedHead.showModel = false;
                    playerModel.bipedBody.showModel = false;
                    playerModel.bipedLeftArmwear.showModel = false;
                    playerModel.bipedLeftLegwear.showModel = false;
                    playerModel.bipedRightArmwear.showModel = false;
                    playerModel.bipedRightLegwear.showModel = false;

                    PopChamsRenderer popChamsRenderer = new PopChamsRenderer(
                            player,
                            playerModel,
                            new Color(fillRed.getValue(), fillGreen.getValue(), fillBlue.getValue(), fillAlpha.getValue()),
                            new Color(outlineRed.getValue(), outlineGreen.getValue(), outlineBlue.getValue(), outlineAlpha.getValue()),
                            fillFadeSpeed.getValue(),
                            outlineFadeSpeed.getValue(),
                            lineWidth.getValue()
                    );
                }
            }
        }
    }
}