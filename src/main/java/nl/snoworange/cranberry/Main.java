package nl.snoworange.cranberry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nl.snoworange.cranberry.features.command.CommandManager;
import nl.snoworange.cranberry.features.friend.Friend;
import nl.snoworange.cranberry.features.module.ModuleManager;
import nl.snoworange.cranberry.util.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION, acceptedMinecraftVersions = Main.ACCEPTED_MINECRAFT_VERSIONS)
public class Main {

    //Refrences
    public static final String MOD_ID = "cranberry";
    public static final String NAME = "Cranberry";
    public static final String VERSION = "v0.6.1";
    public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";
    public static final Logger LOGGER = LogManager.getLogger("Cranberry");

    public static ModuleManager moduleManager;
    public static Friend friendManager;
    public static CommandManager commandManager;
    public static KeyBinding clickGuiKeybind;


    @Mod.Instance
    public static Main instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        friendManager = new Friend(); //sounds weird, why?

        clickGuiKeybind = new KeyBinding("ClickGUI", Keyboard.KEY_NONE, Main.NAME);
        ClientRegistry.registerKeyBinding(clickGuiKeybind);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FileUtils.init();
        FileUtils.loadAll(FileUtils.cranberry);
    }
}