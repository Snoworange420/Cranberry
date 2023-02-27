package nl.snoworange.cranberry.features.module.modules.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import nl.snoworange.cranberry.event.events.PacketEvent;
import nl.snoworange.cranberry.event.events.TotemPopEvent;
import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class FakePlayer extends Module {

    public int randomID = -420;
    public EntityOtherPlayerMP fakeplayer;

    //TODO: make damageable
    public FakePlayer() {
        super("FakePlayer",
                "summons a fake client-side player",
                Category.MISC
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "FakePlayer");

        randomID = ThreadLocalRandom.current().nextInt(-69420, -69);

        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, profile);

        fakePlayer.setPrimaryHand(mc.player.getPrimaryHand());
        fakePlayer.inventory = mc.player.inventory;
        fakePlayer.inventoryContainer = mc.player.inventoryContainer;
        fakePlayer.setPositionAndRotation(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        fakePlayer.onGround = mc.player.onGround;
        fakePlayer.setSneaking(mc.player.isSneaking());
        fakePlayer.setHealth(mc.player.getHealth());
        fakePlayer.setAbsorptionAmount(mc.player.getAbsorptionAmount());

        mc.player.getActivePotionEffects().forEach(fakePlayer::addPotionEffect);

        mc.world.addEntityToWorld(randomID, fakePlayer);

        fakeplayer = fakePlayer;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.world.removeEntityFromWorld(randomID);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.isEnabled() && fakeplayer != null) {
            if (event.getPacket() instanceof CPacketUseEntity) {

                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

                if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                    Entity entity = packet.getEntityFromWorld(mc.world);

                    if (entity != null) {
                        if (entity.equals(fakeplayer)) {
                            event.setCanceled(true);

                            if (mc.player.getCooledAttackStrength(0.5f) > 0.9) {

                                mc.world.playSound(
                                        mc.player,
                                        mc.player.posX,
                                        mc.player.posY,
                                        mc.player.posZ,
                                        SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                                        mc.player.getSoundCategory(),
                                        1.0f, 1.0f
                                );

                                attackFakePlayer();

                            } else {

                                mc.world.playSound(
                                        mc.player,
                                        mc.player.posX,
                                        mc.player.posY,
                                        mc.player.posZ,
                                        SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                                        mc.player.getSoundCategory(),
                                        1.0f, 1.0f
                                );

                                attackFakePlayer();
                            }
                        }
                    }
                }
            }
        }
    }

    public void popPlayer(EntityOtherPlayerMP playerMP) {;

        MinecraftForge.EVENT_BUS.post(new PacketEvent.Receive(new SPacketEntityStatus(playerMP, (byte) 35)));

        mc.effectRenderer.emitParticleAtEntity(playerMP, EnumParticleTypes.TOTEM, 30);
        mc.world.playSound(playerMP.posX,
                playerMP.posY,
                playerMP.posZ,
                SoundEvents.ITEM_TOTEM_USE,
                playerMP.getSoundCategory(),
                1.0F, 1.0F, false
        );

        playerMP.setHealth(1.0f);
        playerMP.setAbsorptionAmount(8.0f);
        playerMP.clearActivePotions();
        playerMP.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
        playerMP.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
    }

    public void attackFakePlayer() {

        double damage = 0.1f;

        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            damage = ((ItemSword) mc.player.inventory.getCurrentItem().getItem()).getAttackDamage() + (double) EnchantmentHelper.getModifierForCreature(mc.player.inventory.getCurrentItem(), EnumCreatureAttribute.UNDEFINED);
        }

        fakeplayer.setHealth(fakeplayer.getHealth() - (float) damage);

        if (fakeplayer.getHealth() <= 0.0f) popPlayer(fakeplayer);
    }
}
