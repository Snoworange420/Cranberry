package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.item.ItemStack;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.modules.render.Tooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Redirect(method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemStack.isItemDamaged()Z"
            )
    )
    private boolean isItemDamaged(ItemStack itemStack) {
        try {
            if (Main.moduleManager.getModuleByName("Tooltip").isEnabled() && Tooltip.getInstance().alwaysDurability.getValue()) {
                return true;
            }
        } catch (NullPointerException ignored) {

        }

        return itemStack.isItemDamaged();
    }
}
