package nl.snoworange.cranberry.features.module.modules.combat;

import net.minecraft.client.gui.GuiHopper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.module.modules.exploit.SecretClose;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;

public class AutoTotem extends Module {

    public final Setting<Integer> slot = register(new Setting<>("MainhandSlot", -1, -1, 8));
    public final Setting<Boolean> ignoreHopper = register(new Setting<>("IgnoreHopper", false, v -> slot.getValue() != -1));

    public AutoTotem() {
        super("AutoTotem", Category.COMBAT);
    }

    public int offhandTotemSlot;
    public int mainhandTotemSlot;

    public static boolean isInHopper;

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

    @SubscribeEvent
    public void onGuiSwitch(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiHopper) {
            isInHopper = true;
        }
    }

    @Override
    public void onFastTick() {

        if (n()) return;

        isInHopper = mc.currentScreen instanceof GuiHopper;

        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && !isInHopper) {
            offhandTotemSlot = InventoryUtils.findInv(Items.TOTEM_OF_UNDYING);

            if (offhandTotemSlot != -1) InventoryUtils.swapItem(offhandTotemSlot, 45);
        }

        if (slot.getValue() == -1) return;

        if (mc.player.inventory.mainInventory.get(slot.getValue()).getItem() != Items.TOTEM_OF_UNDYING && !(ignoreHopper.getValue() && isInHopper)) {
            mainhandTotemSlot = InventoryUtils.findInv(Items.TOTEM_OF_UNDYING);

            if (mainhandTotemSlot == 45) return;

            if (mainhandTotemSlot != -1) InventoryUtils.swapItem(isInHopper ? mainhandTotemSlot - 4 : mainhandTotemSlot, slot.getValue() + (isInHopper ? 36 - 4 : 36));

        }
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketOpenWindow || event.getPacket() instanceof SPacketCloseWindow) {
            isInHopper = false;
        }
    }
}