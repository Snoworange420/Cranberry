package nl.snoworange.cranberry.features.gui.clickgui.components.item.items;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.snoworange.cranberry.features.setting.Setting;

import java.awt.*;

public class BooleanButton
        extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect((int) this.x, (int) this.y, (int) (this.x + (float) this.width + 7.4f), (int) (this.y + (float) this.height - 0.5f), (Boolean) setting.getValue() ? new Color(166, 13, 61).getRGB() : new Color(135, 23, 21).getRGB());
        mc.fontRenderer.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) -6, this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void toggle() {
        this.setting.setValue(!((Boolean) this.setting.getValue()));
    }

    @Override
    public boolean getState() {
        return (Boolean) this.setting.getValue();
    }
}
