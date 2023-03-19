package nl.snoworange.cranberry.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void drawCircle(BlockPos blockPos, double radius, double height, Color color, float lineWidth) {

        if (blockPos == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(2f);

        AxisAlignedBB box = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX,
                blockPos.getY() - mc.getRenderManager().viewerPosY,
                blockPos.getZ() - mc.getRenderManager().viewerPosZ,
                blockPos.getX() + 1 - mc.getRenderManager().viewerPosX,
                blockPos.getY() + 1 - mc.getRenderManager().viewerPosY,
                blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ
        );

        renderCircle(box, radius, height, color, lineWidth);

        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    //a4bva98738va38098j5vja35v35ai
    public static void renderCircle(AxisAlignedBB axisAlignedBB, double n, double n2, Color color, float lineWidth) {

        final float red = color.getRed() / 255f;
        final float green = color.getGreen() / 255f;
        final float blue = color.getBlue() / 255f;

        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(lineWidth);

        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(3.3258652357410368) ^ 0x7FEA9B5F3B9349E6L), axisAlignedBB.minY, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(3.0713387425351564) ^ 0x7FE8921A0BF10295L) + n).color(red, green, blue, Float.intBitsToFloat(Float.floatToIntBits(12.126821f) ^ 0x7EC20775)).endVertex();

        for (int i = 0; i <= 360; ++i) {
            bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(3.4943924475908426) ^ 0x7FEBF484070E5625L) + Math.sin(Math.toRadians(i)) * n, axisAlignedBB.minY, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(2.180035505873708) ^ 0x7FE170B6748EC569L) + Math.cos(Math.toRadians(i)) * n).color(color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.09911574f) ^ 0x7EB5FD31), color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.011103338f) ^ 0x7F4AEAC6), color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.011012881f) ^ 0x7F4B6F5F), Float.intBitsToFloat(Float.floatToIntBits(13.719199f) ^ 0x7EDB81D7)).endVertex();

        }
        tessellator.draw();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < 360; ++i) {
            bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(21.168408199457257) ^ 0x7FD52B1CCCBD0C13L) + Math.sin(Math.toRadians(i)) * n, axisAlignedBB.minY, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(17.860148308328778) ^ 0x7FD1DC32ADF5FB59L) + Math.cos(Math.toRadians(i)) * n).color(color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.009253376f) ^ 0x7F689B79), color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.015045835f) ^ 0x7F0982CE), color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.10712188f) ^ 0x7EA462B7), Float.intBitsToFloat(Float.floatToIntBits(8.495716f) ^ 0x7E34DD47)).endVertex();
            bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(12.511052483536039) ^ 0x7FC905A8ABCEA75BL) + Math.sin(Math.toRadians(i)) * n, axisAlignedBB.minY + n2, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(22.499547648576335) ^ 0x7FD67FE25ACD6DD7L) + Math.cos(Math.toRadians(i)) * n).color(color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.11554704f) ^ 0x7E93A3ED), color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.4653487f) ^ 0x7D91422F), color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.011744561f) ^ 0x7F3F6C42), Float.intBitsToFloat(Float.floatToIntBits(3.2699992E38f) ^ 0x7F7601E5)).endVertex();
            bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(43.823459124280355) ^ 0x7FA5E9671BCC303FL) + Math.sin(Math.toRadians(i + 1)) * n, axisAlignedBB.minY, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(3.8602556055677395) ^ 0x7FEEE1CDB0E0E9B6L) + Math.cos(Math.toRadians(i + 1)) * n).color(color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.010982755f) ^ 0x7F4CF103), color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.11211056f) ^ 0x7E9A9A39), color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.010084451f) ^ 0x7F5A3941), Float.intBitsToFloat(Float.floatToIntBits(357.41248f) ^ 0x7C8187FF)).endVertex();
            bufferbuilder.pos(axisAlignedBB.maxX - Double.longBitsToDouble(Double.doubleToLongBits(18.12502371312355) ^ 0x7FD220018DD71713L) + Math.sin(Math.toRadians(i + 1)) * n, axisAlignedBB.minY + n2, axisAlignedBB.maxZ - Double.longBitsToDouble(Double.doubleToLongBits(31.04659279765212) ^ 0x7FDF0BED816E251FL) + Math.cos(Math.toRadians(i + 1)) * n).color(color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.1610062f) ^ 0x7D5BDECF), color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.012648355f) ^ 0x7F303B0C), color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.010003909f) ^ 0x7F5CE76F), Float.intBitsToFloat(Float.floatToIntBits(2.5075545E38f) ^ 0x7F3CA5BE)).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
    }

    public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo, float lineWidth, int color) {

        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        glPushMatrix();
        glEnable(GL_LINE_SMOOTH);
        glColor4f(red, green, blue, alpha);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(lineWidth);
        glBegin(GL_LINES);
        glVertex2f(xOne, yOne);
        glVertex2f(xTwo, yTwo);
        glEnd();
        glColor4f(red, green, blue, alpha);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void drawCircle2d(float x, float y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        glColor4f(red, green, blue, alpha);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);

        for (int i = 0; i <= 360; i++) {
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        }

        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1F, 1F, 1F, 1F);
    }

    public static void drawBlockOutline(BlockPos blockPos, double height, float lineWidth, Color color, Color endColor) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);

        final AxisAlignedBB box = new AxisAlignedBB(
                blockPos.getX() - mc.getRenderManager().viewerPosX,
                blockPos.getY() - mc.getRenderManager().viewerPosY,
                blockPos.getZ() - mc.getRenderManager().viewerPosZ,
                blockPos.getX() + 1 - mc.getRenderManager().viewerPosX,
                blockPos.getY() + 1 - mc.getRenderManager().viewerPosY,
                blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ
        );

        drawGradientBlockOutline(bufferbuilder,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY + height, box.maxZ,
                color,
                endColor
        );

        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientBlockOutline(final BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color, Color endColor) {

        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;

        final float red2 = endColor.getRed() / 255.0f;
        final float green2 = endColor.getGreen() / 255.0f;
        final float blue2 = endColor.getBlue() / 255.0f;

        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red2, green2, blue2, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red2, green2, blue2, alpha).endVertex();

        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
    }

    public static void drawFilledBox(BlockPos blockPos, double height, Color color, float lineWidth) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);

        final AxisAlignedBB box = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() + 1 - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        drawFilledBox(bufferbuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY + height, box.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawFilledBox(final BufferBuilder builder, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final float red, final float green, final float blue, final float alpha) {
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }

    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    public static void glColor(Color color) {
        GL11.glColor4f(((float) color.getRed() / 255f), ((float) color.getGreen() / 255f), (float)((float) color.getBlue() / 255f), ((float) color.getAlpha() / 255f));
    }
}