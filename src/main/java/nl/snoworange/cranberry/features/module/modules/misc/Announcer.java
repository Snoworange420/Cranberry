package nl.snoworange.cranberry.features.module.modules.misc;

import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

public class Announcer extends Module {

    private static Announcer instance;

    public Announcer() {
        super("Announcer",
                "Announces client events, e.g. module updates",
                Category.MISC
        );

        instance = this;
    }

    public static Announcer getInstance() {
        return instance != null ? instance : new Announcer();
    }

    public enum AnnouceMode {
        CHAT,
        TOAST,
        BOTH;
    }

    public final Setting<Boolean> annouceModuleEvent = register(new Setting<>("ModuleEvent", true));
    public final Setting<AnnouceMode> annouceMode = register(new Setting("Mode", AnnouceMode.TOAST));
    public final Setting<Boolean> warnInToast = register(new Setting<>("WarnInToast", false));


    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public boolean shouldAnnounceInChat() {
        return (this.isEnabled() && (annouceMode.getValue().equals(AnnouceMode.CHAT) || annouceMode.getValue().equals(AnnouceMode.BOTH)));
    }

    public boolean shouldAnnounceInToast() {
        return (this.isEnabled() && (annouceMode.getValue().equals(AnnouceMode.TOAST) || annouceMode.getValue().equals(AnnouceMode.BOTH)));
    }
}
