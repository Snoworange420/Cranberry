package nl.snoworange.cranberry.misc.java.animal;

import nl.snoworange.cranberry.misc.java.animal.animals.Cat;
import nl.snoworange.cranberry.misc.java.animal.animals.Dog;
import nl.snoworange.cranberry.misc.java.animal.animals.Rat;

import java.util.ArrayList;

public class AnimalManager {

    private final ArrayList<Animal> animals;

    public AnimalManager() {
        animals = new ArrayList<>();

        animals.add(new Cat());
        animals.add(new Dog());
        animals.add(new Rat());
    }

    public void onUpdateLiving() {
        animals.forEach(Animal::onLiving);
    }

    public void onUpdateHungerStatus() {
        animals.forEach(animal -> {
            if (animal.hasHunger) {
                animal.onHunger();
            }
        });
    }

    public void updateAll() {
        onUpdateHungerStatus();
        onUpdateLiving();
    }
}
