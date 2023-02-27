package nl.snoworange.cranberry.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.NonNullList;

import java.util.List;

public class InventoryUtils {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean is32k(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null) {
            return false;
        }

        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack) > Enchantments.SHARPNESS.getMaxLevel();
    }

    public static boolean isReverted32k(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null) {
            return false;
        }

        return itemStack.getItem() instanceof ItemSword && EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack) <= Enchantments.SHARPNESS.getMaxLevel();
    }

    public static int findHotbarReverted32k() {
        for (int i = 0; i < 9; ++i) {

            ItemStack slot = mc.player.inventory.mainInventory.get(i);

            if (isReverted32k(slot)) {
                return i;
            }
        }

        return -1;
    }

    public static int findHotbar32k() {
        for (int i = 0; i < 9; ++i) {

            ItemStack slot = mc.player.inventory.mainInventory.get(i);

            if (is32k(slot)) {
                return i;
            }
        }

        return -1;
    }

    public static int findHotbarItemStack(ItemStack itemStack) {
        for (int i = 0; i < 9; ++i) {

            ItemStack slot = mc.player.inventory.mainInventory.get(i);

            if (slot.equals(itemStack)) {
                return i;
            }
        }

        return -1;
    }

    public static int findHotbarItem(Item item) {
        for (int i = 0; i < 9; ++i) {

            Item slot = mc.player.inventory.mainInventory.get(i).getItem();

            if (slot.equals(item)) {
                return i;
            }
        }

        return -1;
    }

    public static int findInv(Item item) {
        for (int i = 0; i < 36; ++i) {

            Item slot = mc.player.inventory.getStackInSlot(i).getItem();

            if (slot.equals(item)) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findShulker() {
        for (int i = 0; i < 36; ++i) {
            Item slot = mc.player.inventory.getStackInSlot(i).getItem();
            if (slot instanceof ItemShulkerBox) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findHotbarPiston() {
        for (int i = 0; i < 9; ++i) {

            ItemStack slot = mc.player.inventory.mainInventory.get(i);

            if (slot.getItem() instanceof ItemPiston) {
                return i;
            }
        }
        return -1;
    }

    public static int findShulkerWith32ksInside() {
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemShulkerBox) {

                List<ItemStack> items = getShulkerContents(itemStack);

                for (ItemStack content : items) {
                    if (is32k(content)) {
                        return i < 9 ? i + 36 : i;
                    }
                }
            }
        }
        return -1;
    }


    public static List<ItemStack> getShulkerContents(ItemStack shulkerStack) {
        NonNullList<ItemStack> shulkerList = NonNullList.withSize(27, ItemStack.EMPTY);

        NBTTagCompound compound = shulkerStack.getTagCompound();

        if (compound != null && compound.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound tags = compound.getCompoundTag("BlockEntityTag");
            if (tags.hasKey("Items", 9)) {
                ItemStackHelper.loadAllItems(tags, shulkerList);
            }
        }
        return shulkerList;
    }

    public static void swapItem(int from, int to) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, to, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, from, 0, ClickType.PICKUP, mc.player);
    }

    public static void update(int index, boolean silent) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(index));
        if (!silent) mc.player.inventory.currentItem = index;
        mc.playerController.updateController();
    }
}
