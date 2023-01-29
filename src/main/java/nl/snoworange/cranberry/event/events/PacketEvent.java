package nl.snoworange.cranberry.event.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import nl.snoworange.cranberry.event.EventStage;

@Cancelable
public class PacketEvent extends EventStage {

    private final Packet<?> packet;
    public int stage;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public int getStage() {
        return this.stage;
    }

    public static class PostSend extends PacketEvent {
        public PostSend(Packet<?> packet) {
            super(packet);
        }
    }

    public static class PostReceive extends PacketEvent {
        public PostReceive(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
