package nl.snoworange.cranberry.features.module.modules.render.popchams;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.util.minecraft.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PopChamsRenderer {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public EntityOtherPlayerMP player;
    public ModelPlayer playerModel;
    public Color fillColor;
    public Color outlineColor;
    public int fillAlpha;
    public int outlineAlpha;
    public int fillFadeSpeed;
    public int outlineFadeSpeed;
    public float lineWidth;
    public boolean finishedFill = false;
    public boolean finishedOutline = false;

    public PopChamsRenderer(EntityOtherPlayerMP player, ModelPlayer playerModel, Color fillColor, Color outlineColor, int fillFadeSpeed, int outlineFadeSpeed, float lineWidth) {
        MinecraftForge.EVENT_BUS.register(this);

        this.player = player;
        this.playerModel = playerModel;
        this.fillColor = fillColor;
        this.outlineColor = outlineColor;
        this.fillFadeSpeed = fillFadeSpeed;
        this.outlineFadeSpeed = outlineFadeSpeed;
        this.lineWidth = lineWidth;

        fillAlpha = fillColor.getAlpha();
        outlineAlpha = outlineColor.getAlpha();
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {

        if (finishedFill && finishedOutline) {
            MinecraftForge.EVENT_BUS.unregister(this);
            return;
        }

        if (PopChams.getInstance().isEnabled()) {

            if (mc.world == null || mc.player == null || player == null) return;

            GL11.glLineWidth(lineWidth);

            RenderUtils.prepareGL();

            if (playerModel != null) {

                //decrease alphas
                if (fillAlpha > 0 && fillAlpha - fillFadeSpeed >= 0) {
                    fillAlpha -= fillFadeSpeed;
                } else {
                    finishedFill = true;
                }

                if (outlineAlpha > 0 && outlineAlpha - outlineFadeSpeed >= 0) {
                    outlineAlpha -= outlineFadeSpeed;
                } else {
                    finishedOutline = true;
                }

                Color finalFillColor = newAlpha(fillColor, fillAlpha);
                Color finalOutlineColor = newAlpha(outlineColor, outlineAlpha);

                if (PopChams.getInstance().moveMode.getValue().equals(PopChams.MoveMode.UP)) {
                    player.posY += 0.001 * PopChams.getInstance().moveSpeed.getValue();
                } else if (PopChams.getInstance().moveMode.getValue().equals(PopChams.MoveMode.DOWN)) {
                    player.posY -= 0.001 * PopChams.getInstance().moveSpeed.getValue();
                }

                RenderUtils.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                RenderUtils.glColor(finalFillColor);
                GL11.glPolygonMode(1032, 6914);
                renderEntity(player, playerModel, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYawHead, player.rotationPitch, 1);
                RenderUtils.glColor(finalOutlineColor);
                GL11.glPolygonMode(1032, 6913);
                renderEntity(player, playerModel, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYawHead, player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                GL11.glDisable(2881);
                GL11.glDisable(2848);
                RenderUtils.releaseGL();
            }
        }
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, int scale) {

        mc.getRenderManager();

        float partialTicks = mc.getRenderPartialTicks();

        double x = entity.posX - mc.getRenderManager().viewerPosX;
        double y = entity.posY - mc.getRenderManager().viewerPosY;
        double z = entity.posZ - mc.getRenderManager().viewerPosZ;

        GlStateManager.pushMatrix();

        if (entity.isSneaking()) {
            y -= 0.125;
        }

        renderLivingAt(x, y, z);
        prepareRotations(entity);
        float sc = prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, sc, entity);
        modelBase.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, sc);
        GlStateManager.popMatrix();
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate(((float) x), ((float) y), ((float) z));
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(((double) scale + widthX), (scale * entity.height), ((double) scale + widthZ));
        float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate((180.0f - entityLivingBase.rotationYaw), 0.0f, 1.0f, 0.0f);
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
