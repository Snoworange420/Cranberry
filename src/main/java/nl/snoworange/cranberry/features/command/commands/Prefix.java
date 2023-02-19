package nl.snoworange.cranberry.features.command.commands;

import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.command.Command;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

public class Prefix extends Command {

    public Prefix() {
        super("Prefix", "changes prefix", "prefix");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length >= 1) {
            String prefix = args[0];
            Main.commandManager.setCommandPrefix(prefix);
            ChatUtils.sendMessage("Set prefix to " + prefix);
        }
    }
}
