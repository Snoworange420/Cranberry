package nl.snoworange.cranberry.features.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.gui.clickgui.components.Component;
import nl.snoworange.cranberry.features.gui.clickgui.components.ComponentFeature;
import nl.snoworange.cranberry.features.gui.clickgui.components.item.Item;
import nl.snoworange.cranberry.features.gui.clickgui.components.item.items.ModuleButton;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.util.FileUtils;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class CranberryClickGUI extends GuiScreen {

    public static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Component> components = new ArrayList();

    private static CranberryClickGUI instance;

    public CranberryClickGUI() {
        instance = this;
        load();
    }

    public static CranberryClickGUI getInstance() {
        return instance == null ? new CranberryClickGUI() : instance;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        FileUtils.saveAll(FileUtils.cranberry);
    }

    private void load() {
        int x = -84;
        for (Category category : Category.values()) {
            this.components.add(new Component(category.name, x += 95, 4, true) {

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    Main.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (module.isVisible()) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }

            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(ComponentFeature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {

                if (!(item instanceof ModuleButton)) continue;

                ModuleButton button = (ModuleButton) item;
                Module buttonModule = button.getModule();

                if (module == null || !module.equals(buttonModule)) continue;

                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}
