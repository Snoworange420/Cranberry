package nl.snoworange.cranberry.event.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import nl.snoworange.cranberry.event.EventStage;

@Cancelable
public class BozoJustDiedEvent extends EventStage {

    private final EntityPlayer player;
    private boolean easy = false;

    public BozoJustDiedEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void tooEasy(boolean easy) {
        this.easy = easy;
    }

    public boolean isEasy() {
        return this.easy;
    }
}
