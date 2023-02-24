package nl.snoworange.cranberry.features.module.modules.misc;

import nl.snoworange.cranberry.features.module.Category;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.setting.Setting;
import nl.snoworange.cranberry.misc.java.discord.Discord;
import nl.snoworange.cranberry.util.FileUtils;
import nl.snoworange.cranberry.util.minecraft.ChatUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DiscordRPC extends Module {

    private static DiscordRPC instance;

    public DiscordRPC() {
        super("DiscordRPC",
                "shows in discord you're playing on Cranberry",
                Category.MISC
        );

        instance = this;
    }

    public static DiscordRPC getInstance() {
        return instance != null ? instance : new DiscordRPC();
    }

    public static String rptext = "Enjoying $clientname$ $version$";

    @Override
    public void onEnable() {
        super.onEnable();

        loadRPCText();
        Discord.startRPC();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stopRPC();
    }

    public void loadRPCText() {
        try {
            File file = new File(FileUtils.cranberry, "discordrpc.txt");

            if (!file.exists()) {
                file.createNewFile();

                if (!n()) {
                    ChatUtils.sendMessage("No discordrpc.txt found in Cranberry folder! Creating one...");
                    return;
                }
            }

            FileReader fileReader = new FileReader(file);
            List<String> linezz = Files.readAllLines(file.toPath());

            if (linezz.size() <= 0) {
                ChatUtils.sendMessage("Empty discordrpc.txt found in Cranberry folder!");
            } else {
                rptext = linezz.get(0);
            }

            fileReader.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}