package nl.snoworange.cranberry.features.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.snoworange.cranberry.features.command.commands.Friend;
import nl.snoworange.cranberry.features.command.commands.Load;
import nl.snoworange.cranberry.features.command.commands.Prefix;
import nl.snoworange.cranberry.features.command.commands.Save;
import nl.snoworange.cranberry.util.FileUtils;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    public static List<Command> commands = new ArrayList<>();
    public static String prefix = "^";

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);

        commands.add(new Save());
        commands.add(new Load());

        commands.add(new Prefix());
        commands.add(new Friend());
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String message = event.getMessage();

        if (!message.startsWith(prefix)) {
            return;
        }

        event.setCanceled(true);
        message = message.substring(prefix.length());

        if (message.split(" ").length > 0) {

            boolean commandFound = false;
            String commandName = message.split(" ")[0];

            if (commandName.equals("") || commandName.equals("help")) {
                sendCommandDescriptions();
            } else {
                for (Command command : commands) {
                    if (command.name.equalsIgnoreCase(commandName)) {
                        command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                        commandFound = true;
                        break;
                    }
                }
                if (!commandFound) {
                    ChatUtils.sendMessage(ChatFormatting.DARK_RED + "command does not exist, use " + ChatFormatting.ITALIC + prefix + "help " + ChatFormatting.RESET + "" + ChatFormatting.DARK_RED + "for help.");
                }
            }
        }
    }

    private void sendCommandDescriptions() {
        ChatUtils.sendMessage("Commands:");
        for (Command command : commands) {
            ChatUtils.sendMessage(command.name + " - " + command.description + " [" + command.syntax + "]");
        }
    }

    public void setCommandPrefix(String newPrefix) {
        prefix = newPrefix;

        FileUtils.savePrefix(FileUtils.cranberry, newPrefix);
    }

    public void sendCorrectionMessage(String name, String syntax) {
        String correction = "correct usage of " + ChatFormatting.WHITE + name + ChatFormatting.GRAY + " command -> " + ChatFormatting.WHITE + prefix + syntax + ChatFormatting.GRAY + ".";
        ChatUtils.sendMessage(correction);
    }
}
