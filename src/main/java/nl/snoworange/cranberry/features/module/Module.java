package nl.snoworange.cranberry.features.module;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import nl.snoworange.cranberry.features.setting.Bind;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class Module {

    public final Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public String description;
    public Category category;
    public String displayName;
    public boolean toggled;
    public boolean visible;
    public List<Setting> settings = new ArrayList<Setting>();

    public Module(String name, Category category) {
        this.name = name;
        this.description = "No discription provided.";
        this.category = category;
        this.displayName = name;
        this.toggled = false;
        this.visible = true;
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.displayName = name;
        this.toggled = false;
        this.visible = true;
    }

    public Module(String name, String description, Category category, boolean visible) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.displayName = name;
        this.toggled = false;
        this.visible = visible;
    }

    public Module(String name, String description, Category category, String displayName) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.displayName = displayName;
        this.toggled = false;
        this.visible = true;
    }

    //stuff

    public void enable() {
        onEnable();
        this.toggled = true;
    }

    public void disable() {
        onDisable();
        this.toggled = false;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

        ChatUtils.sendTempMessage("{}" + TextFormatting.GREEN + " enabled.", this.displayName);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);

        ChatUtils.sendTempMessage("{}" + TextFormatting.RED + " disabled.", this.displayName);
    }

    public void onTick() {}

    public void onFastTick() {}

    public void onLiving() {}

    public void onRender2d() {}

    public void onRender3d() {}

    public void toggle() {
        if (toggled) {
            onDisable();
            toggled = false;
        } else {
            onEnable();
            toggled = true;
        }
    }


    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isToggled() {
        return toggled;
    }

    public boolean isEnabled() {
        return toggled;
    }

    public boolean isDisabled() {
        return !toggled;
    }

    public boolean isVisible() {
        return visible;
    }

    public Bind getKeybind() {
        return bind.getValue();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if (toggled) disable();
        if (!toggled) enable();
    }

    public void setEnabled(boolean enabled) {
        setToggled(enabled);
    }

    public void setEnabledNoEvent(boolean enabled) {
        this.toggled = enabled;
    }

    public void setKeybind(int keybind) {
        this.bind.setValue(new Bind(keybind));
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public Setting register(Setting setting) {
        this.settings.add(setting);
        return setting;
    }

    //i have to put it here so i wont crash lmao
    public Setting<Bind> bind = register(new Setting<Bind>("Keybind", new Bind(-1)));

    public Setting getSettingByName(String name) {
        for (Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public void reset() {
        for (Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }

    public boolean n() {
        return mc.player == null || mc.world == null;
    }
}