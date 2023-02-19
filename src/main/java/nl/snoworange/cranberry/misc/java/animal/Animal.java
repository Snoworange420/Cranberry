package nl.snoworange.cranberry.misc.java.animal;

import nl.snoworange.cranberry.misc.java.animal.categories.AnimalType;

public class Animal {

    public String name;
    public AnimalType animalType;
    public int hungerStrength;
    public int saturation;

    public boolean isSleeping;
    public boolean hasHunger;
    public boolean hasmouth;

    public Animal(String name, AnimalType animalType, int hungerStrength) {
        this.name = name;
        this.animalType = animalType;
        this.hungerStrength = hungerStrength;
        this.saturation = hungerStrength;
    }

    public String getName() {
        return this.name;
    }

    public boolean isHasmouth() {
        return hasmouth;
    }

    public void onLiving() {
        //saturation--;

        hasHunger = saturation <= 0;
    }
    public void onSleep() {}
    public void onHunger() {}
}
