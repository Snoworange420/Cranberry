package nl.snoworange.cranberry.features.module.modules.misc;

import net.minecraft.client.multiplayer.ServerData;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;

public class BetterConnectingGUI extends Module {

    private static BetterConnectingGUI instance;

    public BetterConnectingGUI() {
        super("BetterConnectingGUI",
                Category.MISC
        );

        instance = this;
    }

    public static BetterConnectingGUI getInstance() {
        return instance != null ? instance : new BetterConnectingGUI();
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
