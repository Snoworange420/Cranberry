package nl.snoworange.cranberry.features.module.modules.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;

import java.util.UUID;

public class DeathEffects extends Module {

    public DeathEffects() {
        super("DeathEffects", Category.RENDER);
    }

    public final Setting<Enum> effect = register(new Setting("Effect", Effects.LIGHTNING));

    public enum Effects {
        LIGHTNING,
        TEST;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onLiving() {

        if (mc.world == null) return;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.deathTime == 1) {
                if (effect.getValue().equals(Effects.LIGHTNING)) {
                    summonLightning(player.posX, player.posY, player.posZ);
                } else if (effect.getValue().equals(Effects.TEST)) {

                }
            }
        }
    }

    public void summonLightning(double x, double y, double z) {
        mc.world.addWeatherEffect(new EntityLightningBolt(mc.world,
                x,
                y,
                z,
                false)
        );
    }
}
