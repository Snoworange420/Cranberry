package nl.snoworange.cranberry.misc.java.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.modules.misc.BetterConnectingGUI;

public class Discord {

    private static final String discordID = "1077977771311124581";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            discordRichPresence.state = "Logged in as " + Minecraft.getMinecraft().player.getName();
        } else discordRichPresence.state = "In the memu";

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = "cranberry-icon";
        discordRichPresence.details = nl.snoworange.cranberry.features.module.modules.misc.DiscordRPC.rptext
                .replace("$clientname$", Main.NAME)
                .replace("$version$", Main.VERSION)
                .replace("$name$", Minecraft.getMinecraft().getSession().getUsername())
                .replace("$hp$", Minecraft.getMinecraft().player != null ? String.valueOf(Minecraft.getMinecraft().player.getHealth()) : "-1")
                .replace("$server$", BetterConnectingGUI.currentServerData != null ? BetterConnectingGUI.currentServerData.serverIP : "server");
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }

}
