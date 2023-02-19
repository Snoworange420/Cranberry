package nl.snoworange.cranberry.misc.java.animal.animals;

import nl.snoworange.cranberry.misc.java.animal.Animal;
import nl.snoworange.cranberry.misc.java.animal.AnimalUtils;
import nl.snoworange.cranberry.misc.java.animal.categories.AnimalType;

public class Cat extends Animal {

    public Cat() {
        super("Cat", AnimalType.CARNIVORE, 1250);
    }

    @Override
    public void onLiving() {
        AnimalUtils.say("meow");
        AnimalUtils.say("meow");
        AnimalUtils.say("meow");
    }

    @Override
    public void onHunger() {
        AnimalUtils.findFeedtrayNearby();
        AnimalUtils.eat(this);
    }

    @Override
    public void onSleep() {
        super.onSleep();
    }
}
