package nl.snoworange.cranberry.features.command;

import net.minecraft.client.Minecraft;

public class Command {

    public String name, description, syntax;

    public static final Minecraft mc = Minecraft.getMinecraft();

    public Command(String name, String description, String syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
    }

    public void onCommand(String[] args, String command) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

}
