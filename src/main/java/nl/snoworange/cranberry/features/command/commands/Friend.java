package nl.snoworange.cranberry.features.command.commands;

import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.command.Command;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

public class Friend extends Command {

    public Friend() {
        super("Friend", "friends people", "friend");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length >= 1) {
            String friendname = args[0];
            Main.friendManager.addFriend(friendname);
            ChatUtils.sendMessage("Added friend " + friendname);
        }
    }
}
