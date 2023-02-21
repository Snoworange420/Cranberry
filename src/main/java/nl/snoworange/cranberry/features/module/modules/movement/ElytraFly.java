package nl.snoworange.cranberry.features.module.modules.movement;

import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;
import nl.snoworange.cranberry.util.minecraft.EntityUtils;

public class ElytraFly extends Module {

    public ElytraFly() {
        super("ElytraFly",
                "Fly using elytra like a superman",
                Category.MOVEMENT
        );
    }

    public enum TakeoffMode {
        KEYBINDDETECTION,
        ONGROUND;
    }

    public enum FlyMode {
        MOTION;
    }


    public enum FreezeMode {
        NONE,
        MOTION,
        VELOCITY;
    }

    public final Setting<Boolean> autoTakeoff = register(new Setting<>("AutoTakeoff", true));
    public final Setting<Enum> takeoffMode = register(new Setting<>("TakeoffMode", TakeoffMode.KEYBINDDETECTION));
    public final Setting<Integer> takeoffDelayTicks = register(new Setting<>("TakeoffDelayTicks", 33, 0, 100));
    public final Setting<Double> horizontalSpeed = register(new Setting<>("HorizontalSpeed", 1.0, 0.0, 10.0));
    public final Setting<Double> verticalSpeed = register(new Setting<>("VerticalSpeed", 1.0, 0.0, 10.0));
    public final Setting<Boolean> lockY = register(new Setting<>("LockY", false));
    public final Setting<Enum> freezeMode = register(new Setting<>("FreezeMode", FreezeMode.NONE));
    public final Setting<Enum> flyMode = register(new Setting<>("FlyMode", FlyMode.MOTION));


    public int takeoffTicks = 0;
    public boolean takeoffStarted = false;

    @Override
    public void onEnable() {
        super.onEnable();

        takeoffTicks = 0;
        takeoffStarted = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {

        if (n()) return;


        //takeoff
        if (takeoffStarted) {
            takeoffTicks++;

            if (mc.player.onGround) {
                takeoffStarted = false;
                takeoffTicks = 0;
            }

            if (takeoffTicks >= takeoffDelayTicks.getValue() && !mc.player.isElytraFlying() && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));

                takeoffStarted = false;
                takeoffTicks = 0;
            }
        }

        if (autoTakeoff.getValue() && !takeoffStarted) {
            if (takeoffMode.getValue().equals(TakeoffMode.KEYBINDDETECTION)) {
                if (mc.gameSettings.keyBindJump.isPressed()) {
                    takeoffStarted = true;
                }
            } else if (takeoffMode.getValue().equals(TakeoffMode.ONGROUND)) {
                if (!mc.player.onGround) {
                    takeoffStarted = true;
                }
            }
        }
    }

    @Override
    public void onLiving() {

        if (n()) return;

        //efly part
        if (mc.player.isElytraFlying()) {

            if (freezeMode.getValue().equals(FreezeMode.VELOCITY)) {
                mc.timer.tickLength = 50;
                mc.player.setVelocity(0.0,
                        0.0,
                        0.0
                );
            } else if (freezeMode.getValue().equals(FreezeMode.MOTION)) {
                mc.player.motionX = 0;
                mc.player.motionY = 0;
                mc.player.motionZ = 0;
            }

            if (flyMode.getValue().equals(FlyMode.MOTION)) {
                EntityUtils.moveEntityWithSpeed(mc.player, horizontalSpeed.getValue(), verticalSpeed.getValue(), !lockY.getValue());
            }
        }
    }
}
