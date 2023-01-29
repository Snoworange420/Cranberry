package nl.snoworange.cranberry.features.gui.clickgui.components.item.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.snoworange.cranberry.features.gui.clickgui.CranberryClickGUI;
import nl.snoworange.cranberry.features.gui.clickgui.components.Component;
import nl.snoworange.cranberry.features.gui.clickgui.components.item.Item;

import java.awt.*;

public class Button extends Item {

    public static final Minecraft mc = Minecraft.getMinecraft();
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect((int) this.x, (int) this.y, (int) (this.x + (float) this.width), (int) (this.y + (float) this.height - 0.5f), this.getState() ? (!this.isHovering(mouseX, mouseY) ? new Color(166, 13, 61).getRGB() : new Color(135, 23, 21).getRGB()) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float) -6, this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : CranberryClickGUI.getInstance().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}
