package nl.snoworange.cranberry.features.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Bind;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;
import nl.snoworange.cranberry.util.minecraft.InventoryUtils;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tooltip extends Module {
    private static Tooltip instance;
    public boolean enteredShulker = false;
    public float renderPartialTicks = 0.0f;
    public boolean generatedShulkerTooltip;
    private boolean isMouseInShulkerGui = false;
    public int systemX;
    public int systemY;

    private final List<GuiShulkerPreview> guiCache = new ArrayList<>();

    public Tooltip() {
        super("Tooltip",
                Category.RENDER
        );

        instance = this;
    }

    public static Tooltip getInstance() {
        if (instance == null) {
            return new Tooltip();
        }

        return instance;
    }

    public final Setting<Boolean> alwaysDurability = register(new Setting<>("AlwaysDurability", true));
    public final Setting<Boolean> shulker = register(new Setting<>("PreviewShulker", true));
    public final Setting<Bind> displayShulker = register(new Setting<>("DisplayShulker", new Bind(Keyboard.KEY_V)));
    public final Setting<Bind> displayNbtTags = register(new Setting<>("DisplayNBTtags", new Bind(Keyboard.KEY_N)));
    public final Setting<Integer> shulkerOffsetX = register(new Setting<>("ShulkerOffsetX", 0, -100, 100));
    public final Setting<Integer> shulkerOffsetY = register(new Setting<>("ShulkerOffsetY", 0, -100, 100));

    @Override
    public void onEnable() {
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static void onRenderTooltip(ItemStack itemStack, int x, int y, CallbackInfo ci) {
        getInstance().renderShulkerTooltip(itemStack, x, y);
        getInstance().updateKey();
    }

    public void renderShulkerTooltip(ItemStack itemStack, int x, int y) {

        if (!shulker.getValue() || this.isDisabled()) return;

        if (!(itemStack.getItem() instanceof ItemShulkerBox)
                || itemStack == ItemStack.EMPTY) return;

        systemX = x;
        systemY = y;

        if (!enteredShulker) {
            renderShulkerF(itemStack, x, y);
        }
    }

    private void setInCache(int index, @Nullable GuiShulkerPreview viewer) {
        if (viewer == null && index > 1 && index == guiCache.size() - 1) {
            guiCache.remove(index); // remove non-reserved extras
        } else if (index > guiCache.size() - 1) { // array not big enough
            for (int i = Math.max(guiCache.size(), 1); i < index; ++i)
                guiCache.add(i, null); // fill with nulls up to the index
            guiCache.add(index, viewer);
        } else {
            guiCache.set(index, viewer);
        }
    }

    public void renderShulkerF(ItemStack itemStack, int x, int y) {
        try {

            setInCache(0, new GuiShulkerPreview(
                    new ShulkerContainer(new ShulkerInventory(InventoryUtils.getShulkerContents(itemStack)), 27),
                    itemStack,
                    1
            ));

            guiCache
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .forEach(
                            GuiShulkerPreview -> {
                                GuiShulkerPreview.posX = x;
                                GuiShulkerPreview.posY = y;
                                GuiShulkerPreview.drawScreen(x, y, renderPartialTicks);
                            });
        } catch (Exception exception) {
            ChatUtils.sendMessage(exception.toString());
        }
    }

    public void bindShulkerTextureWithColor(ItemStack itemStack) {
        try {
            mc.getTextureManager().bindTexture(new ResourceLocation(Main.MOD_ID, "shulkertextures/shulker_" + Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString().replace("minecraft:", "").replace("_shulker_box", "") + ".png"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //fixing this part soon

    /*
    @SubscribeEvent
    public void onPreTooptipRender(RenderTooltipEvent.Pre event) {
        if (!(mc.currentScreen instanceof GuiContainer) || generatedShulkerTooltip) return;

        if (isMouseInShulkerGui) {
            // do not render tooltips that are inside the shulker gui
            event.setCanceled(true);
        }
    }

     */

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (this.isEnabled()) {
            renderPartialTicks = event.getRenderPartialTicks();
        }
    }

    public void updateKey() {
        try {
            if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();

                if (keyCode <= 0) return;

                if (displayShulker.getValue().getKey() == keyCode) {
                    enteredShulker = true;
                } else {
                    enteredShulker = false;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    class GuiShulkerPreview extends GuiContainer implements Comparable<GuiShulkerPreview> {
        private final ItemStack shulkerStack;
        private final int priority;
        public int posX = 0;
        public int posY = 0;

        public GuiShulkerPreview(Container inventorySlotsIn, ItemStack shulkerStack, int priority) {
            super(inventorySlotsIn);
            this.shulkerStack = shulkerStack;
            this.priority = priority;
            this.mc = Minecraft.getMinecraft();
            this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
            this.width = Minecraft.getMinecraft().displayWidth;
            this.height = Minecraft.getMinecraft().displayHeight;
            this.xSize = 176;
            this.ySize = 76;
        }

        public ItemStack getParentShulker() {
            return shulkerStack;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getWidth() {
            return xSize;
        }

        public int getHeight() {
            return ySize;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {

            int x = posX;
            int y = posY;

            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            mc.getRenderItem().zLevel = 300;

            GlStateManager.color(1F, 1F, 1F, 1F);

            GlStateManager.enableBlend();
            GlStateManager.enableTexture2D();

            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            bindShulkerTextureWithColor(shulkerStack);

            Gui.drawModalRectWithCustomSizedTexture(x + shulkerOffsetX.getValue() + 8, y + shulkerOffsetY.getValue() - 97, 0, 0, 256, 256, 256, 256);

            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();

            Slot hoveringOver = null;

            int sx = x + shulkerOffsetX.getValue() + 16;
            int sy = y + shulkerOffsetY.getValue() - 98;

            for (int j = 0; j < inventorySlots.inventorySlots.size(); ++j) {

                int ix = sx + j % 9 * 18;
                int iy = sy + ((j / 9 + 1) * 18) + 1;

                int px = ix + 8;
                int py = iy - 1;

                Slot slot = inventorySlots.inventorySlots.get(j);

                if (slot != null && slot.getHasStack()) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), ix, iy);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, slot.getStack(), ix, iy, null);
                }

                if (isPointInRegion(px, py, 16, 16, mouseX, mouseY)) hoveringOver = slot;
            }

            RenderHelper.disableStandardItemLighting();
            mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();

            if (hoveringOver != null) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.colorMask(true, true, true, false);

                //Minecraft wtf i dont understand
                this.drawGradientRect(
                        sx + hoveringOver.xPos,
                        sy + hoveringOver.yPos,
                        sx + hoveringOver.xPos + 16,
                        sy + hoveringOver.yPos + 16,
                        -2130706433,
                        -2130706433
                );

                GlStateManager.colorMask(true, true, true, true);

                //tooltip of the item itself
                GlStateManager.color(1.f, 1.f, 1.f, 1.0f);
                GlStateManager.pushMatrix();
                generatedShulkerTooltip = true;
                renderToolTip(hoveringOver.getStack(), mouseX + 8, mouseY + 8);
                generatedShulkerTooltip = false;
                GlStateManager.popMatrix();
                GlStateManager.enableDepth();
            }

            if (isPointInRegion(this.posX, this.posY, getWidth(), getHeight(), mouseX, mouseY))
                isMouseInShulkerGui = true;

            GlStateManager.disableBlend();
            GlStateManager.color(1.f, 1.f, 1.f, 1.0f);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        }

        @Override
        public int compareTo(GuiShulkerPreview o) {
            return Integer.compare(priority, o.priority);
        }
    }

    static class ShulkerContainer extends Container {
        public ShulkerContainer(ShulkerInventory inventory, int size) {
            for (int i = 0; i < size; ++i) {
                int x = i % 9 * 18;
                int y = ((i / 9 + 1) * 18) + 1;
                addSlotToContainer(new Slot(inventory, i, x, y));
            }
        }

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return false;
        }
    }

    static class ShulkerInventory implements IInventory {
        private final List<ItemStack> contents;

        public ShulkerInventory(List<ItemStack> contents) {
            this.contents = contents;
        }

        @Override
        public int getSizeInventory() {
            return contents.size();
        }

        @Override
        public boolean isEmpty() {
            return contents.isEmpty();
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return contents.get(index);
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getInventoryStackLimit() {
            return 27;
        }

        @Override
        public void markDirty() {}

        @Override
        public boolean isUsableByPlayer(EntityPlayer player) {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer player) {}

        @Override
        public void closeInventory(EntityPlayer player) {}

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return index > 0 && index < contents.size() && contents.get(index).equals(stack);
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {}

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {}

        @Override
        public String getName() {
            return "";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString("");
        }
    }
}