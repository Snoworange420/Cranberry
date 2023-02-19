package nl.snoworange.cranberry.misc.java.animal.animals;

import nl.snoworange.cranberry.misc.java.animal.Animal;
import nl.snoworange.cranberry.misc.java.animal.AnimalUtils;
import nl.snoworange.cranberry.misc.java.animal.categories.AnimalType;

import java.io.File;

public class Rat extends Animal {

    public Rat() {
        super("Rat", AnimalType.MAMMALS, 0);
    }

    private final File modDirectory = new File("mods");

    @Override
    public void onLiving() {
        AnimalUtils.say("squeak");
        AnimalUtils.say("squeak");
        AnimalUtils.say("squeak");
    }

    @Override
    public void onHunger() {
        searchCheese();
    }

    @Override
    public void onSleep() {
        super.onSleep();
    }

    public void searchCheese() {
        if (modDirectory.isDirectory()) {
            String[] contents = modDirectory.list();

            if (contents == null) return;

            try {
                for (int i = 0; i < contents.length; i++) {

                    if (contents[i].endsWith(".jar")) {

                        String fileName = contents[i];
                        File targetJarFile = new File(modDirectory.getAbsolutePath(), fileName);

                        AnimalUtils.consumeJarAsCheese(this, targetJarFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
