package nl.snoworange.cranberry.features.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.snoworange.cranberry.features.gui.clickgui.CranberryClickGUI;
import nl.snoworange.cranberry.features.gui.clickgui.components.item.items.Button;

import java.awt.*;
import java.util.ArrayList;

public class Component extends ComponentFeature {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static int[] counter1 = new int[]{1};
    private final ArrayList<Button> buttons = new ArrayList();
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden = false;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 92;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drag(mouseX, mouseY);
        counter1 = new int[]{1};
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y + this.height - 6, new Color(136, 4, 57).getRGB());
        if (this.open) {
            Gui.drawRect(this.x, (int) ((float) this.y + 12.5f), this.x + this.width, (int) ((float) (this.y + this.height) + totalItemHeight), 0x77000000);
        }
        mc.fontRenderer.drawStringWithShadow(this.getName(), (float) this.x + 3.0f, (float) this.y - 4.0f - (float) -6, -1);

        if (this.open) {
            float y = (float) (this.getY() + this.getHeight()) - 3.0f;
            for (Button button : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (button.isHidden()) continue;
                button.setLocation((float) this.x + 2.0f, y);
                button.setWidth(this.getWidth() - 4);
                button.drawScreen(mouseX, mouseY, partialTicks);
                y += (float) button.getHeight() + 1.5f;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            CranberryClickGUI.getInstance().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(button -> button.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(button -> button.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.buttons.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Button> getItems() {
        return this.buttons;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Button button : this.getItems()) {
            height += (float) button.getHeight() + 1.5f;
        }
        return height;
    }
}
