package nl.snoworange.cranberry.features.module;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.gui.clickgui.CranberryClickGUI;
import nl.snoworange.cranberry.features.module.modules.combat.*;
import nl.snoworange.cranberry.features.module.modules.exploit.SecretClose;
import nl.snoworange.cranberry.features.module.modules.hud.Coordinates;
import nl.snoworange.cranberry.features.module.modules.hud.Watermark;
import nl.snoworange.cranberry.features.module.modules.misc.*;
import nl.snoworange.cranberry.features.module.modules.movement.AutoSprint;
import nl.snoworange.cranberry.features.module.modules.movement.ElytraFly;
import nl.snoworange.cranberry.features.module.modules.movement.LiquidSpeed;
import nl.snoworange.cranberry.features.module.modules.player.FastXP;
import nl.snoworange.cranberry.features.module.modules.render.*;
import nl.snoworange.cranberry.features.module.modules.render.popchams.PopChams;
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
        modules.add(new SelfAnvil());
        modules.add(new AutoAnvil());
        modules.add(new EntityAura());

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
        modules.add(new PopChams());

        modules.add(new BetterConnectingGUI());
        modules.add(new DiscordRPC());
        modules.add(new FakePlayer());
        modules.add(new Announcer());

        modules.add(new AutoSprint());
        modules.add(new ElytraFly());
        modules.add(new LiquidSpeed());

        modules.add(new FastXP());

        modules.add(new nl.snoworange.cranberry.features.module.modules.hud.ArrayList());
        modules.add(new Watermark());
        modules.add(new Coordinates());
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

    public ItemStack getDisplayStack(Module module) {

        if (module.getModuleStack() == null) {

            if (module.getCategory() == null) return new ItemStack(Items.SHULKER_SHELL);

            if (module.getCategory().equals(Category.COMBAT)) return new ItemStack(Items.IRON_SWORD);
            if (module.getCategory().equals(Category.EXPLOIT)) return new ItemStack(Item.getItemFromBlock(Blocks.MOB_SPAWNER));
            if (module.getCategory().equals(Category.RENDER)) return new ItemStack(Items.ENDER_EYE);
            if (module.getCategory().equals(Category.MOVEMENT)) return new ItemStack(Items.MINECART);
            if (module.getCategory().equals(Category.PLAYER)) return new ItemStack(Items.SKULL, 1, 3);
            if (module.getCategory().equals(Category.MISC)) return new ItemStack(Item.getItemFromBlock(Blocks.REPEATING_COMMAND_BLOCK), 1, 1);
            if (module.getCategory().equals(Category.STRONKSWORDMETA)) return new ItemStack(Item.getItemFromBlock(Blocks.DISPENSER));
            if (module.getCategory().equals(Category.HUD)) return new ItemStack(Items.ENDER_PEARL);
        }

        return module.getModuleStack();
    }
}