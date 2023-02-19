package nl.snoworange.cranberry.features.command.commands;

import nl.snoworange.cranberry.features.command.Command;
import nl.snoworange.cranberry.util.FileUtils;

public class Load extends Command {

    public Load() {
        super("Load", "loads cranberry files e.g. friends", "load");
    }

    @Override
    public void onCommand(String[] args, String command) {
        FileUtils.loadAll(FileUtils.cranberry);
    }
}
