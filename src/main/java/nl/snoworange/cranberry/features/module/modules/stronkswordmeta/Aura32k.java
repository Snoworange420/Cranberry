package nl.snoworange.cranberry.features.module.modules.stronkswordmeta;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;

public class Aura32k extends Module {

    public final Setting<Double> range = register(new Setting<>("Range", 7.5, 0.0, 12.0));
    public final Setting<Boolean> swing = register(new Setting<>("SwingArm", true));
    public final Setting<Boolean> attackFriends = register(new Setting<>("AttackFriends", false));

    private EntityPlayer currentTarget;

    public Aura32k() {
        super(
                "32kAura",
                "A killaura with high cps for killing players with 32k items",
                Category.STRONKSWORDMETA
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!n()) mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    @Override
    public void onFastTick() {

        if (n()) return;

        int stronkswordslot = InventoryUtils.findHotbar32k();

        if (stronkswordslot == -1) return;

        for (EntityPlayer target : mc.world.playerEntities) {

            if (target == mc.player) continue;

            if (!attackFriends.getValue() && Main.friendManager.isFriend(target.getName())) continue;

            if (target.getHealth() < 0 || target.isDead) continue;

            if (mc.player.getDistance(target) <= range.getValue()) {

                currentTarget = target;

                mc.player.connection.sendPacket(new CPacketHeldItemChange(stronkswordslot));
                mc.player.connection.sendPacket(new CPacketUseEntity(target));
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            } else {
                currentTarget = null;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPacket(PacketEvent.Receive event) {
        if (this.isEnabled() && !n()) {

            if (event.getPacket() instanceof SPacketEntityStatus) {

                SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

                if (packet.getOpCode() == 35) {

                    Entity entity = packet.getEntity(mc.world);

                    if (entity instanceof EntityPlayer && !entity.getName().equalsIgnoreCase(mc.player.getName())) {

                        int stronkswordslot = InventoryUtils.findHotbar32k();

                        if (currentTarget == null || stronkswordslot == -1) return;

                        new Thread(() -> {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(stronkswordslot));
                            mc.player.connection.sendPacket(new CPacketUseEntity(currentTarget));
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                            if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
                        }).start();
                    }
                }
            }
        }
    }

    /*
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onKrypticAndKeiranSkillIssue(BozoJustDiedEvent event) {
        event.tooEasy(true);
        Bad.getSkill().getGood().info(event.getPlayer().getName() + " is bad ong imagine being that bad");
    }

     */
}
