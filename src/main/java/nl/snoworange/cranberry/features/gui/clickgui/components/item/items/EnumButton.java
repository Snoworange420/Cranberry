package nl.snoworange.cranberry.features.gui.clickgui.components.item.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.snoworange.cranberry.features.setting.Setting;

import java.awt.*;

public class EnumButton extends Button {
    public Setting setting;

    public EnumButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect((int) this.x, (int) this.y, (int) (this.x + (float) this.width + 7.4f), (int) (this.y + (float) this.height - 0.5f), new Color(155, 23, 42).getRGB());
        mc.fontRenderer.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + (this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName()), this.x + 2.3f, this.y - 1.7f - (float) -6, this.getState() ? -1 : -5592406);
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
        this.setting.increaseEnumNoEvent();
    }

    @Override
    public boolean getState() {
        return true;
    }
}
