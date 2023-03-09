package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.client.gui.GuiHopper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.module.modules.exploit.SecretClose;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;

public class AutoTotem extends Module {

    public final Setting<Integer> slot = register(new Setting<>("MainhandSlot", -1, -1, 8));

    public AutoTotem() {
        super("FastTotem", Category.COMBAT);
    }

    public int offhandTotemSlot;
    public int mainhandTotemSlot;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void init() {
        this.setModuleStack(new ItemStack(Items.TOTEM_OF_UNDYING));
    }

    @Override
    public void onFastTick() {

        if (n()) return;

        if (mc.currentScreen instanceof GuiHopper || (Main.moduleManager.getModuleByName("SecretClose").isEnabled() && SecretClose.lastGui instanceof GuiHopper)) return;

        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            offhandTotemSlot = InventoryUtils.findInv(Items.TOTEM_OF_UNDYING);

            if (offhandTotemSlot != -1) InventoryUtils.swapItem(offhandTotemSlot, 45);
        }

        if (slot.getValue() == -1) return;

        if (mc.player.inventory.mainInventory.get(slot.getValue()).getItem() != Items.TOTEM_OF_UNDYING) {
            mainhandTotemSlot = InventoryUtils.findInv(Items.TOTEM_OF_UNDYING);

            if (mainhandTotemSlot == 45) return;

            if (mainhandTotemSlot != -1) {
                InventoryUtils.swapItem(mainhandTotemSlot, slot.getValue() + 36);
            }
        }
    }
}