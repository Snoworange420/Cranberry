package nl.snoworange.cranberry.features.module.modules.misc;

import net.minecraft.client.multiplayer.ServerData;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;

public class ConnectingGuiPlus extends Module {

    private static ConnectingGuiPlus instance;

    public ConnectingGuiPlus() {
        super("ConnectingGui+",
                "Better connecting to the server... gui",
                Category.MISC
        );

        instance = this;
    }

    public static ConnectingGuiPlus getInstance() {
        return instance != null ? instance : new ConnectingGuiPlus();
    }

    public static ServerData currentServerData;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
