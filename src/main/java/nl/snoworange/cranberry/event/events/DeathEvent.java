package nl.snoworange.cranberry.event.events;

import net.minecraft.entity.player.EntityPlayer;
import nl.snoworange.cranberry.event.EventStage;

public class DeathEvent extends EventStage {

    public EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
