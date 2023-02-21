package nl.snoworange.cranberry.event.events;

import net.minecraft.entity.player.EntityPlayer;
import nl.snoworange.cranberry.event.EventStage;

public class TotemPopEvent extends EventStage {

    private final EntityPlayer player;

    public TotemPopEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

}
