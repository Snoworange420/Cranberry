package nl.snoworange.cranberry.features.module;

public enum Category {

    COMBAT("Combat"),
    EXPLOIT("Exploit"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISC("Misc"),
    STRONKSWORDMETA("32k"),
    HUD("HUD");

    public String name;

    Category(String name) {
        this.name = name;
    }
}
