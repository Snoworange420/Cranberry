package nl.snoworange.cranberry.util.minecraft;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import nl.snoworange.cranberry.Main;

import java.util.regex.Pattern;

public class ChatUtils {

    private static Minecraft mc = Minecraft.getMinecraft();
    public static final String prefix = TextFormatting.DARK_RED + "[" + Main.NAME + "] " + TextFormatting.RESET;

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(prefix + message + ChatFormatting.RESET));
    }

    public static void sendTempMessage(Object message, Object... arguments) {

        if (mc.world == null || mc.player == null) return;

        sendMessageWithDeletionID(message, 6942069, arguments);
    }

    public static void sendMessageWithDeletionID(Object message, int id, Object... arguments) {
        String stringMessage = message.toString();
        for (Object argument : arguments) {
            String regex = Pattern.quote("{}");
            stringMessage = stringMessage.replaceFirst(regex, argument.toString());
        }
        TextComponentString textComponent = new TextComponentString(prefix + stringMessage);
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textComponent, id);
    }
}
