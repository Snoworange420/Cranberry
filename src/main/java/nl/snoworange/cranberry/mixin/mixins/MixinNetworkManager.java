package nl.snoworange.cranberry.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import nl.snoworange.cranberry.event.events.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" },
            at = { @At("HEAD") },
            cancellable = true
    )
    public void onPacketSend(final Packet<?> packet, final CallbackInfo ci) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = { "channelRead0" },
            at = { @At("HEAD") },
            cancellable = true
    )
    public void onPacketReceive(final ChannelHandlerContext chc, final Packet<?> packet, final CallbackInfo ci) {
        PacketEvent.Receive event = new PacketEvent.Receive(packet);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
