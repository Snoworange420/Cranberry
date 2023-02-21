package nl.snoworange.cranberry.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class EntityUtils {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static void moveEntityWithSpeed(Entity entity, double horizontalSpeed, double verticalSpeed,  boolean moveY) {

        float yaw = (float) Math.toRadians(mc.player.rotationYaw);

        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            entity.motionX = -(MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionZ = MathHelper.cos(yaw) * horizontalSpeed;
        } else if (mc.gameSettings.keyBindBack.isKeyDown()) {
            entity.motionX = MathHelper.sin(yaw) * horizontalSpeed;
            entity.motionZ = -(MathHelper.cos(yaw) * horizontalSpeed);
        }

        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            entity.motionZ = MathHelper.sin(yaw) * horizontalSpeed;
            entity.motionX = MathHelper.cos(yaw) * horizontalSpeed;
        } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
            entity.motionZ = -(MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionX = -(MathHelper.cos(yaw) * horizontalSpeed);
        }

        if (moveY) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                entity.motionY = verticalSpeed;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                entity.motionY = -verticalSpeed;
            }
        }

        if (mc.gameSettings.keyBindForward.isKeyDown() && mc.gameSettings.keyBindLeft.isKeyDown()) {
            entity.motionX = (MathHelper.cos(yaw) * horizontalSpeed) - (MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionZ = (MathHelper.cos(yaw) * horizontalSpeed) + (MathHelper.sin(yaw) * horizontalSpeed);
        } else if (mc.gameSettings.keyBindLeft.isKeyDown() && mc.gameSettings.keyBindBack.isKeyDown()) {
            entity.motionX = (MathHelper.cos(yaw) * horizontalSpeed) + (MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionZ = -(MathHelper.cos(yaw) * horizontalSpeed) + (MathHelper.sin(yaw) * horizontalSpeed);
        } else if (mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown()) {
            entity.motionX = -(MathHelper.cos(yaw) * horizontalSpeed) + (MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionZ = -(MathHelper.cos(yaw) * horizontalSpeed) - (MathHelper.sin(yaw) * horizontalSpeed);
        } else if (mc.gameSettings.keyBindRight.isKeyDown() && mc.gameSettings.keyBindForward.isKeyDown()) {
            entity.motionX = -(MathHelper.cos(yaw) * horizontalSpeed) - (MathHelper.sin(yaw) * horizontalSpeed);
            entity.motionZ = (MathHelper.cos(yaw) * horizontalSpeed) - (MathHelper.sin(yaw) * horizontalSpeed);
        }
    }
}
