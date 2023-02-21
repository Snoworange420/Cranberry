package nl.snoworange.cranberry.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.event.events.BozoJustDiedEvent;
import nl.snoworange.cranberry.event.events.DeathEvent;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.event.events.TotemPopEvent;

public class ForgeEventListener {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent //doesn't work, why?
    public void onLiving(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player == null || player.getHealth() > 0.0F)
                    continue;
                MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
            }
        }
    }

    //bozo
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDeath(DeathEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("asdacashier")
        || event.getPlayer().getName().equalsIgnoreCase("Kryptic_MZ")) {
            MinecraftForge.EVENT_BUS.post(new BozoJustDiedEvent(event.getPlayer()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPacket(PacketEvent.Receive event) {

        if (mc.world == null) return;

        if (event.getPacket() instanceof SPacketEntityStatus) {

            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();

            if (packet.getOpCode() == 35) {
                Entity entity = packet.getEntity(mc.world);

                if (!(entity instanceof EntityPlayer) || entity.getName().equalsIgnoreCase(Minecraft.getMinecraft().player.getName()))
                    return;

                MinecraftForge.EVENT_BUS.post(new TotemPopEvent((EntityPlayer) entity));
            }
        }
    }
}