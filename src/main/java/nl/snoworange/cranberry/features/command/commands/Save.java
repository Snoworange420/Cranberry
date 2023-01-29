package nl.snoworange.cranberry.features.command.commands;

import nl.snoworange.cranberry.features.command.Command;
import nl.snoworange.cranberry.util.FileUtils;

public class Save extends Command {

    public Save() {
        super("Save", "saves cranberry files e.g. friends", "save");
    }

    @Override
    public void onCommand(String[] args, String command) {
        FileUtils.saveAll(FileUtils.cranberry);
    }
}
