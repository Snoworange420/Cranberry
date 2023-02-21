package nl.snoworange.cranberry.mixin.mixins;

import net.minecraft.block.BlockShulkerBox;
import nl.snoworange.cranberry.features.module.modules.render.Tooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockShulkerBox.class)
public class MixinBlockShulkerBox {

    @ModifyConstant(
            method = "addInformation",
            constant = @Constant(
                    intValue = 4
            )
    )
    private int tooltipLength(int length) {
        if (Tooltip.getInstance().isEnabled() && Tooltip.getInstance().modifyShulkerInfoLength.getValue()) {
            return Tooltip.getInstance().shulkerInfoMaxLength.getValue();
        }

        return 4;
    }
}
