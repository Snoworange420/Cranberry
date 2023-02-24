package nl.snoworange.cranberry.features.module;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.gui.clickgui.CranberryClickGUI;
import nl.snoworange.cranberry.features.module.modules.combat.AutoTotem;
import nl.snoworange.cranberry.features.module.modules.combat.PistonElevator;
import nl.snoworange.cranberry.features.module.modules.combat.Surround;
import nl.snoworange.cranberry.features.module.modules.exploit.SecretClose;
import nl.snoworange.cranberry.features.module.modules.hud.Watermark;
import nl.snoworange.cranberry.features.module.modules.misc.BetterConnectingGUI;
import nl.snoworange.cranberry.features.module.modules.misc.DiscordRPC;
import nl.snoworange.cranberry.features.module.modules.movement.AutoSprint;
import nl.snoworange.cranberry.features.module.modules.movement.ElytraFly;
import nl.snoworange.cranberry.features.module.modules.movement.LiquidSpeed;
import nl.snoworange.cranberry.features.module.modules.player.FastXP;
import nl.snoworange.cranberry.features.module.modules.render.*;
import nl.snoworange.cranberry.features.module.modules.stronkswordmeta.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);
        modules = new ArrayList<>();

        modules.add(new Auto32k());
        modules.add(new Aura32k());
        modules.add(new AutoTotem());
        modules.add(new Surround());
        modules.add(new PistonElevator());

        modules.add(new SecretClose());

        modules.add(new HopperRadius());
        modules.add(new Info32k());
        modules.add(new Tooltip());
        modules.add(new DeathEffects());
        modules.add(new CleanGUI());
        modules.add(new Particles());
        modules.add(new DVDIcon());
        modules.add(new FullBright());
        modules.add(new SelectionHighlight());

        modules.add(new BetterConnectingGUI());
        modules.add(new DiscordRPC());

        modules.add(new AutoSprint());
        modules.add(new ElytraFly());
        modules.add(new LiquidSpeed());

        modules.add(new FastXP());

        modules.add(new nl.snoworange.cranberry.features.module.modules.hud.ArrayList());
        modules.add(new Watermark());
    }

    //lambda expression go brr
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        modules.forEach(m -> {
            if (m.isEnabled() && !m.n()) {
                try {
                    m.onTick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void onFastTick(TickEvent event) {
        modules.forEach(m -> {
            if (m.isEnabled() && !m.n()) {
                try {
                    m.onFastTick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        modules.forEach(m -> {
            if (m.isEnabled() && event.getEntityLiving() instanceof EntityPlayer) {
                try {
                    m.onLiving();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        modules.forEach(m -> {
            if (m.isEnabled() & Minecraft.getMinecraft().world != null) {
                try {
                    m.onRender3d();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        try {
            if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();

                if (keyCode <= 0) return;

                modules.forEach(m -> {
                    if (m.getKeybind().getKey() == keyCode) {
                        m.toggle();
                    }
                });

                //without this check it throws NullPointerException
                if (Minecraft.getMinecraft().player == null ||
                        Minecraft.getMinecraft().world == null ||
                Main.clickGuiKeybind == null) return;

                if (Main.clickGuiKeybind.getKeyCode() == keyCode) {
                    Minecraft.getMinecraft().displayGuiScreen(new CranberryClickGUI());
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public ArrayList<Module> getActiveModules() {

        ArrayList<Module> activeModules = new ArrayList<>();

        modules.forEach(module -> {
            if (module.isEnabled()) {
                activeModules.add(module);
            }
        });

        return activeModules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }

        return null;
    }
    public ArrayList<Module> getModulesByCategory(Category category) {

        ArrayList<Module> modulesCategory = new ArrayList<>();

        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });

        return modulesCategory;
    }
}