package nl.snoworange.cranberry.features.module.modules.render;

import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

public class CleanGUI extends Module {

    private static CleanGUI instance;

    public CleanGUI() {
        super("CleanGUI",
                "Removes tint in guis",
                Category.RENDER);

        instance = this;
    }

    public static CleanGUI getInstance() {
        return instance != null ? instance : new CleanGUI();
    }

    public final Setting<Boolean> removeAllGuiTint = register(new Setting<>("RemoveAllGuiTint", false));
    public final Setting<Boolean> removeCGuiTint = register(new Setting<>("RemoveContainerTint", true));
    public final Setting<Boolean> removeChatTint = register(new Setting<>("RemoveChatTint", true));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
